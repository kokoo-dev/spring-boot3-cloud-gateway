package com.kokoo.springboot3.config;

import com.kokoo.springboot3.filter.GlobalFilter;
import com.kokoo.springboot3.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final TokenFilter tokenFilter;
    private final GlobalFilter globalFilter;

    @Bean
    public RouteLocator routeGateway(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user/**")
                        .filters(f -> f.rewritePath("/user/(?<path>.*)", "/external/user/${path}")
                                .filters(getGlobalFilter()))
                        .uri("http://localhost:8081/")
                )
                .route(r -> r.path("/product/**")
                        .filters(f -> f.rewritePath("/product/(?<path>.*)", "/external/product/${path}")
                                .filters(getGlobalFilter(), getTokenFilter()))
                        .uri("http://localhost:8082/")
                )
                .build();
    }

    private GatewayFilter getGlobalFilter() {
        GlobalFilter.Config config = GlobalFilter.Config.builder()
                .baseMessage("base-message")
                .preLogger(true)
                .postLogger(true)
                .build();

        return globalFilter.apply(config);
    }

    private GatewayFilter getTokenFilter() {

        return tokenFilter.apply(new TokenFilter.Config());
    }
}
