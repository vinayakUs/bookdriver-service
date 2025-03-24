//package org.example.cloudgateway;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//public class GraphQLRoutingFilter implements GlobalFilter , Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest req = exchange.getRequest();
//        return exchange.getRequest().getBody()
//                .collectList()
//                .flatMap(body->)
//
//        return null;
//    }
//}
