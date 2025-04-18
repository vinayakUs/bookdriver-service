package org.example.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.gateway.exception.JwtValidationException;
import org.example.gateway.service.JwtService;
import org.example.sharedlibs.payload.ApiResponseDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private static final String API_HEADER_INVALID_VALUE_MESSAGE = "GW : Invalid or missing JWT token";
    private static final String UNKNOWN_ERROR_MESSAGE = "GW : Unknown error occurred";
    private final JwtService jwtService;


    public JwtAuthFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Implement JWT authentication logic here

            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                try {
                    return handleUnauthorized(exchange, API_HEADER_INVALID_VALUE_MESSAGE);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            //Extract token
            String token = auth.substring(7);
            // Do the logic to validate Token;

            ServerHttpRequest mutatedReq;
            try {
                Claims claims = jwtService.validateToken(token);
                mutatedReq = exchange.getRequest().mutate().header("X-USER-ID", claims.getSubject()).header("X-AUTHORITIES", claims.get("authorities", String.class)).build();

            } catch (JwtValidationException e) {
                log.warn("JWT validation failed: {}", e.getMessage());
                try {
                    return handleUnauthorized(exchange, e.getMessage());
                } catch (JsonProcessingException jpx) {
                    throw new RuntimeException(jpx);
                }
            }
            return chain.filter(exchange.mutate().request(mutatedReq).build());
        };
    }

    // Method to handle missing Authorization header
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) throws JsonProcessingException {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ApiResponseDto<String> responseDto = new ApiResponseDto<>(false, message);
        byte[] bytes = new ObjectMapper().writeValueAsBytes(responseDto);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Data
    public static class Config {
        // Configuration properties for JWT authentication can be added here

    }

}
