package org.example.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import java.awt.image.DataBuffer;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GraphQLRouteFilter extends AbstractGatewayFilterFactory<GraphQLRouteFilter.Config> {

    public GraphQLRouteFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(GraphQLRouteFilter.Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if(request.getMethod() == HttpMethod.POST && request.getURI().getRawPath().equals("/graphql")) {


              return   DataBufferUtils.join(request.getBody()).flatMap(
                        dbf ->
                        {
                            byte[] bytes = new byte[dbf.readableByteCount()];
                            dbf.read(bytes);
                            DataBufferUtils.release(dbf);
                            String string = new String(bytes, StandardCharsets.UTF_8);

                            try {
                                URI uri = null;

                                ObjectMapper objectMapper = new ObjectMapper();
                                JsonNode jsonNode  = objectMapper.readTree(string);
                                String query = jsonNode.get("query").asText();
                                if(query.contains("tripRequest") ){
                                    System.out.println("tripRequest");
                                    uri = URI.create("http://localhost:8086/graphql");
                                }else {
                                    Mono.error(new RuntimeException("Invalid query parameter"));
                                }
                                assert uri != null;
                                ServerHttpRequest  newRequest = exchange.getRequest().mutate().uri(uri).build();

                                return chain.filter(exchange.mutate().request(newRequest).build());

                            } catch (Exception e) {
                                return Mono.error(e);
                            }

                        }
                );



            }
            return chain.filter(exchange);

        };
    }

    @Data
    public static class Config {
        // Configuration properties for JWT authentication can be added here

    }
}
