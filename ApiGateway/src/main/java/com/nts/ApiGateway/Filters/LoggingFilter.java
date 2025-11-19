package com.nts.ApiGateway.Filters;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String ip = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Forwarded-For");

        if (ip == null) {
            ip = exchange.getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }


        System.out.println("📥 Incoming request from IP: " + ip + " for path: " + path);



        return chain.filter(exchange).then(Mono.fromRunnable(() ->
                System.out.println("📤 Response sent for path: " + path)
        ));
    }


    @Override
    public int getOrder(){
        return -1;
    }

}
