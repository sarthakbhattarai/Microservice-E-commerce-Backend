package com.example.APIGateway.Config;

import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product_service", r -> r.path("/api/products/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://PRODUCT-SERVICE"))
                .route("user_service", r -> r.path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("cart_service", r -> r.path("/api/carts/**")
                        .uri("lb://CART-SERVICE"))
                .route("order_service", r -> r.path("/api/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // Example configuration: 5 requests per second with a burst capacity of 10
        return new RedisRateLimiter(5, 10);
    }
}
