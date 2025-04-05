redis/redis-stack
To start a Redis Stack container using the redis-stack image, run the following command in your terminal:
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest

Docker Confluent Build:
wget https://raw.githubusercontent.com/confluentinc/cp-all-in-one/7.9.0-post/cp-all-in-one-kraft/docker-compose.yml
docker compose up -d

Sonarqube
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest


```bash
#!/bin/bash

# Hardened Confluent Kafka with Schema Registry Installation
# Using default system user (root)

# ========== HARDCODED CONFIGURATION ==========
INSTALL_DIR="/opt/confluent"
DATA_BASE="/var/lib/confluent"
CONFLUENT_VERSION="7.5.1"

# Network
KAFKA_ADVERTISED_HOST="$(curl -s http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/access-configs/0/external-ip -H 'Metadata-Flavor: Google')"
KAFKA_PORT="9092"
SCHEMA_REGISTRY_PORT="8081"
ZOOKEEPER_PORT="2181"

echo $KAFKA_ADVERTISED_HOST

# ========== INSTALLATION ==========
set -e  # Exit on error

# Update system and install JDK
echo "Updating system and installing JDK..."
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk jq netcat

# 1. Verify Java
echo "Using Java from:"
java -version

# 2. Create data directories (with sudo)
sudo mkdir -p $DATA_BASE/{kafka,zookeeper,schema-registry}
sudo chown -R $USER:$USER $DATA_BASE
sudo chmod -R 755 $DATA_BASE

# 3. Install Confluent
sudo wget -q https://packages.confluent.io/archive/7.5/confluent-${CONFLUENT_VERSION}.tar.gz -P /tmp
sudo tar -xzf /tmp/confluent-${CONFLUENT_VERSION}.tar.gz -C /opt
sudo mv /opt/confluent-${CONFLUENT_VERSION} $INSTALL_DIR
sudo rm /tmp/confluent-${CONFLUENT_VERSION}.tar.gz
sudo chown -R $USER:$USER $INSTALL_DIR



# 4. Configure ZooKeeper
sudo rm -rf $INSTALL_DIR/etc/kafka/zookeeper.properties

cat > $INSTALL_DIR/etc/kafka/zookeeper.properties <<EOF
dataDir=$DATA_BASE/zookeeper
clientPort=$ZOOKEEPER_PORT
maxClientCnxns=0
admin.enableServer=false
audit.enable=true
EOF

# 5. Configure Kafka
sudo rm -rf $INSTALL_DIR/etc/kafka/server.properties 

cat > $INSTALL_DIR/etc/kafka/server.properties <<EOF
broker.id=0
listeners=PLAINTEXT://0.0.0.0:$KAFKA_PORT
advertised.listeners=PLAINTEXT://$KAFKA_ADVERTISED_HOST:$KAFKA_PORT
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
log.dirs=$DATA_BASE/kafka
num.partitions=1
num.recovery.threads.per.data.dir=1
offsets.topic.replication.factor=1
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1
log.retention.hours=168
log.retention.check.interval.ms=300000
zookeeper.connect=localhost:$ZOOKEEPER_PORT
zookeeper.connection.timeout.ms=18000
group.initial.rebalance.delay.ms=0
confluent.license.topic.replication.factor=1
confluent.metadata.topic.replication.factor=1
confluent.security.event.logger.exporter.kafka.topic.replicas=1
confluent.balancer.enable=true
confluent.balancer.topic.replication.factor=1
EOF

# 6. Configure Schema Registry
sudo rm -rf $INSTALL_DIR/etc/schema-registry/schema-registry.properties

cat > $INSTALL_DIR/etc/schema-registry/schema-registry.properties <<EOF
listeners=http://0.0.0.0:$SCHEMA_REGISTRY_PORT
kafkastore.bootstrap.servers=PLAINTEXT://$KAFKA_ADVERTISED_HOST:$KAFKA_PORT
kafkastore.topic=_schemas
debug=false
metadata.encoder.secret=REPLACE_ME_WITH_HIGH_ENTROPY_STRING
resource.extension.class=io.confluent.dekregistry.DekRegistryResourceExtension
EOF

# 7. Create systemd services

cat <<EOF | sudo tee /etc/systemd/system/confluent-zookeeper.service > /dev/null
[Unit]
Description=Confluent ZooKeeper
After=network.target

[Service]
Type=simple
ExecStart=$INSTALL_DIR/bin/zookeeper-server-start $INSTALL_DIR/etc/kafka/zookeeper.properties
ExecStop=$INSTALL_DIR/bin/zookeeper-server-stop
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF
 

# Kafka Service
cat <<EOF | sudo tee /etc/systemd/system/confluent-kafka.service > /dev/null
[Unit]
Description=Confluent Kafka Server
After=network.target confluent-zookeeper.service

[Service]
Type=simple
ExecStart=$INSTALL_DIR/bin/kafka-server-start $INSTALL_DIR/etc/kafka/server.properties
ExecStop=$INSTALL_DIR/bin/kafka-server-stop
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF


# Schema Registry Service
cat <<EOF | sudo tee /etc/systemd/system/confluent-schema-registry.service > /dev/null
[Unit]
Description=Confluent Schema Registry
After=network.target confluent-kafka.service

[Service]
Type=simple
ExecStart=$INSTALL_DIR/bin/schema-registry-start $INSTALL_DIR/etc/schema-registry/schema-registry.properties
ExecStop=$INSTALL_DIR/bin/schema-registry-stop
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF


# 8. Enable and start services
sudo systemctl daemon-reload
sudo systemctl enable confluent-zookeeper
sudo systemctl enable confluent-kafka
sudo systemctl enable confluent-schema-registry

sudo systemctl start confluent-zookeeper
sleep 5  # Wait for ZooKeeper to start
sudo systemctl start confluent-kafka
sleep 5  # Wait for Kafka to start
sudo systemctl start confluent-schema-registry

# 9. Verify services
echo "ZooKeeper status: $(systemctl is-active confluent-zookeeper)"
echo "Kafka status: $(systemctl is-active confluent-kafka)"
echo "Schema Registry status: $(systemctl is-active confluent-schema-registry)"
echo "Kafka topics: $($INSTALL_DIR/bin/kafka-topics --list --bootstrap-server localhost:$KAFKA_PORT)"
echo "Schema Registry subjects: $(curl -s http://localhost:$SCHEMA_REGISTRY_PORT/subjects)"

echo "Installation complete!"
echo "Kafka is running on $KAFKA_ADVERTISED_HOST:$KAFKA_PORT"
echo "Schema Registry is running on port $SCHEMA_REGISTRY_PORT"

echo "Adding to Path"
echo 'export PATH=$PATH:/opt/confluent/bin' >> ~/.bashrc
source ~/.bashrc




wget https://github.com/provectus/kafka-ui/releases/download/v0.7.2/kafka-ui-api-v0.7.2.jar -O kafka-ui-api.jar


cat <<EOF > application.yml
auth:
  type: disabled
kafka:
  clusters:
    - name: local
      bootstrapServers: localhost:9092
      zookeeper: localhost:2181
      schemaRegistry: http://localhost:8081
EOF



sudo mkdir -p /opt/kafka-ui
sudo mv kafka-ui-api.jar /opt/kafka-ui/
sudo mv application.yml /opt/kafka-ui/
sudo chmod +x /opt/kafka-ui/kafka-ui-api.jar


# Kafka ui Service
cat <<EOF | sudo tee /etc/systemd/system/kafka-ui.service > /dev/null
[Unit]
Description=Kafka UI Service
After=network.target

[Service]
User=root
Group=root
ExecStart=/usr/bin/java -jar /opt/kafka-ui/kafka-ui-api.jar --spring.config.location=/opt/kafka-ui/application.yml
WorkingDirectory=/opt/kafka-ui
Restart=always
SuccessExitStatus=143
TimeoutStopSec=10
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable kafka-ui
sudo systemctl start kafka-ui

#INSTALL REDIS

wget https://packages.redis.io/redis-stack/redis-stack-server-7.4.0-v3.bullseye.x86_64.tar.gz && tar -xvzf redis-stack-server-7.4.0-v3.bullseye.x86_64.tar.gz
sudo mv redis-stack-server-7.4.0-v3 /opt/redis-stack



echo "" | sudo tee -a /opt/redis-stack/etc/redis-stack.conf
echo "bind 0.0.0.0" | sudo tee -a /opt/redis-stack/etc/redis-stack.conf
echo "protected-mode no" | sudo tee -a /opt/redis-stack/etc/redis-stack.conf
echo "requirepass admin" | sudo tee -a /opt/redis-stack/etc/redis-stack.conf




sudo tee /etc/systemd/system/redis-stack.service > /dev/null <<EOF
[Unit]
Description=Redis Stack Server
After=network.target

[Service]
ExecStart=/opt/redis-stack/bin/redis-stack-server
Restart=always
User=root
Group=root

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable redis-stack
sudo systemctl start redis-stack

echo 'export PATH=$PATH:/opt/redis-stack/bin' >> ~/.bashrc && source ~/.bashrc

```


```bash
#!/bin/bash

echo "====================================================="
echo "      🔍 Confluent Platform & Redis Services Status"
echo "====================================================="

# Function to get systemd service status
get_status() {
    systemctl is-active --quiet "$1" && echo "RUNNING ✅" || echo "NOT RUNNING ❌"
}

# Function to find open ports and their corresponding processes
get_open_port() {
    port=$(netstat -tulnp 2>/dev/null | grep "$1" | awk '{print $4}' | awk -F: '{print $NF}' | uniq)
    echo "${port:-N/A}"
}

# Function to check and display available services dynamically
check_services() {
    services=(
        "confluent-zookeeper"
        "confluent-kafka"
        "confluent-schema-registry"
        "kafka-ui"
        "redis-stack"
    )

    echo ""
    echo "------------------------------------------------------------"
    echo "| SERVICE                  | PORT  | STATUS  | URL         |"
    echo "------------------------------------------------------------"

    for service in "${services[@]}"; do
        case $service in
            confluent-zookeeper) port="2181" url="N/A" ;;
            confluent-kafka) port="9092" url="N/A" ;;
            confluent-schema-registry) port="8081" url="http://localhost:8081" ;;
            kafka-ui) port="8080" url="http://localhost:8080" ;;
            redis-stack) port="6379" url="N/A" ;;
            *) port="N/A" url="N/A" ;;
        esac

        runtime_port=$(get_open_port "$port")
        status=$(get_status "$service")

        printf "| %-24s | %-5s | %-8s | %-20s |\n" "$service" "$runtime_port" "$status" "$url"
    done

    echo "------------------------------------------------------------"
    echo ""
}

# Display the dynamic service status table
check_services

# Function to display detailed logs
display_details() {
    echo "====================================================="
    echo "🔍 Detailed Logs and Runtime Information"
    echo "====================================================="

    for service in "${services[@]}"; do
        echo "-----------------------------------------------"
        echo "📌 Service: $service"
        echo "-----------------------------------------------"
        
        # Show service status
        systemctl status "$service" --no-pager | grep -E 'Active|Loaded' || echo "Service not found"
        
        # Show latest logs
        echo "🔹 Recent Logs:"
        journalctl -u "$service" --no-pager --lines=5 | tail -n 5
        echo ""
    done
}

# Call function to show details
display_details

# Additional runtime information
echo "====================================================="
echo "🔍 Additional Debugging Information"
echo "====================================================="

# Kafka Topics
echo "🔹 Kafka Topics:"
/opt/confluent/bin/kafka-topics --list --bootstrap-server localhost:9092 || echo "Kafka not available"
echo ""

# Schema Registry Subjects
echo "🔹 Schema Registry Subjects:"
curl -s http://localhost:8081/subjects | jq || echo "Schema Registry API not responding"
echo ""

# Redis Modules
echo "🔹 Redis Modules:"
redis-cli --no-auth-warning --user default --pass YourSecurePassword MODULE LIST || echo "Could not retrieve Redis modules"
echo ""

# Active Network Ports
echo "🔹 Active Network Ports (Filtered):"
netstat -tulnp 2>/dev/null | grep -E '6379|9092|2181|8081|8080' || echo "No active Confluent/Redis ports found"
echo ""

echo "✅ Runtime service check complete! Use 'journalctl -u <service>' for more logs."

```


```bash
gcloud compute instances create stack ^
  --project=effortless-lock-450919-m1 ^
  --zone=asia-south1-b ^
  --machine-type=e2-medium ^
  --network-interface=network-tier=PREMIUM,stack-type=IPV4_ONLY,subnet=default ^
  --maintenance-policy=MIGRATE ^
  --provisioning-model=STANDARD ^
  --service-account=102870825273-compute@developer.gserviceaccount.com ^
  --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/trace.append ^
  --tags=redis,http-server,https-server ^
  --create-disk=auto-delete=yes,boot=yes,device-name=stack,image=projects/debian-cloud/global/images/debian-11-bullseye-v20250311,mode=rw,size=60,type=pd-balanced ^
  --no-shielded-secure-boot ^
  --shielded-vtpm ^
  --shielded-integrity-monitoring ^
  --labels=goog-ec-src=vm_add-gcloud ^
  --reservation-affinity=any
```