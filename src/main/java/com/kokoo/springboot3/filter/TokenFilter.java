package com.kokoo.springboot3.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    public static class Config {

    }

    public TokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            //Token Validate
            HttpHeaders httpHeaders = request.getHeaders();
            List<String> accessToken = httpHeaders.get("X-Access-Token");
            if (ObjectUtils.isEmpty(accessToken)) {

            }

            log.info("Token Filter Request ID :: {}", request.getId());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Token Filter Response code :: {}", response.getStatusCode());
            }));
        };
    }
}
