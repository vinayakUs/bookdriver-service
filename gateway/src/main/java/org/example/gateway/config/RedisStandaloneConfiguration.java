package org.example.gateway.config;

import org.example.gateway.filter.FixedWindowRateLimiterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisStandaloneConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, Long> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory redisConnectionFactory) {

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        StringRedisSerializer stringRedisSerializer = StringRedisSerializer.UTF_8;

        GenericToStringSerializer<Long> longToStringSerializer = new GenericToStringSerializer<>(Long.class);

        return new ReactiveRedisTemplate<>(redisConnectionFactory,
                RedisSerializationContext.<String, Long>newSerializationContext(jdkSerializationRedisSerializer)
                        .key(stringRedisSerializer).value(longToStringSerializer).build());

  
    }


    @Bean
    public FixedWindowRateLimiterFilter fixedWindowRateLimiterFilter(ReactiveRedisTemplate<String, String> template) {
        return new FixedWindowRateLimiterFilter(template);
    }
}
