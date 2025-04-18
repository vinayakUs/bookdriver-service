package org.example.gateway.filter;

import java.time.Duration;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.Data;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class FixedWindowRateLimiterFilter extends AbstractGatewayFilterFactory<FixedWindowRateLimiterFilter.Config> {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public FixedWindowRateLimiterFilter(ReactiveRedisTemplate<String, String> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

 

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientId = resolveClientId(exchange);
            String redisKey = buildRedisKey(clientId,config);

            System.out.println("Redis Key: =====> " + redisKey);

        return   redisTemplate.opsForValue().increment(redisKey).flatMap(val->{
            if (val==1L) {
                return redisTemplate.expire(redisKey, Duration.ofSeconds(config.window)).thenReturn(val);
            }
            return Mono.just(val);
           }).doOnNext(x->log.info("Count ==> "+x))
           .flatMap(count->{
            if (count>config.maxRequests) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();    
            }
            return chain.filter(exchange);
           });
         };
    }
    private String resolveClientId(ServerWebExchange exchange) {
        return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
    }

    private String buildRedisKey(String clientId,Config config) {
        long windowStart = Instant.now().getEpochSecond() / Duration.ofSeconds(config.window).getSeconds();
        return "rl:fw:" + clientId + ":" + windowStart;
    }


    @Data
    public static class Config {
        private int maxRequests  ;        // max requests in burst
        private int window ;            // Window size (Milliseconds)
    }
}

 