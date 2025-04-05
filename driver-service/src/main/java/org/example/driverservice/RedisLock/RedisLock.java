package org.example.driverservice.RedisLock;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

public class RedisLock {

    private final LettuceConnectionFactory lettuceConnectionFactory;
    private final String lockPrefix;
    private final int type;



    private RedisLock(Builder builder) {
        this.lettuceConnectionFactory = builder.connectionFactory;
        this.lockPrefix = builder.lockPrefix;
        this.type = builder.type;
    }

    /**
     * blocking lock
     *
     * @param key
     * @param request
     */
    public void lock(String key, String request)  {
       Object connection = lettuceConnectionFactory.getConnection();
       for(;;){
           if(connection instanceof LettuceConnection){
               ((RedisProperties.Lettuce    )connection)
           }
       }
    }


        public static class Builder {

        private static final String DEFAULT_LOCK_PREFIX = "lock_";

        private final LettuceConnectionFactory connectionFactory;
        private final int type;
        private  String lockPrefix=DEFAULT_LOCK_PREFIX;

        public Builder(LettuceConnectionFactory connectionFactory,int type) {
            this.connectionFactory = connectionFactory;
            this.type = type;
        }

        public Builder lockPrefix(String lockPrefix) {
            this.lockPrefix= lockPrefix;
            return this;
        }
        public RedisLock build() {
            return new RedisLock(this);
        }
    }
}
