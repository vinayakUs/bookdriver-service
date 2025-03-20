redis/redis-stack
To start a Redis Stack container using the redis-stack image, run the following command in your terminal:
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest

Docker Confluent Build:
wget https://raw.githubusercontent.com/confluentinc/cp-all-in-one/7.9.0-post/cp-all-in-one-kraft/docker-compose.yml
docker compose up -d
