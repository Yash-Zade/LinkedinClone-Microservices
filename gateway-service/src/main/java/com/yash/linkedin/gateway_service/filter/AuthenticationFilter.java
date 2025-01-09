package com.yash.linkedin.gateway_service.filter;


import com.yash.linkedin.gateway_service.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {


        return ((exchange, chain) -> {
            final String tokenHeader = exchange.getResponse().getHeaders().getFirst("Authorization");
            if(tokenHeader == null || !tokenHeader.startsWith("Bearer") ){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            final String token = tokenHeader.split("Bearer ")[1];
            String userId = jwtService.getUserIdFromToken(token);
            ServerWebExchange modifiedExchange  =exchange
                    .mutate()
                    .request(req -> req.header("X-User-Id",userId))
                    .build();

            return chain.filter(modifiedExchange);
        });
    }

    public static class Config {
    }
}


