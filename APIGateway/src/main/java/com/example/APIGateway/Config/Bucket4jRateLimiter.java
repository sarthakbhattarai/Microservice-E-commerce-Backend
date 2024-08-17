package com.example.APIGateway.Config;
import io.github.bucket4j.*;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Bucket4jRateLimiter implements RateLimiter {

    private final Map<String, Bucket> buckets = new HashMap<>();

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        // Create or retrieve the bucket
        Bucket bucket = buckets.computeIfAbsent(id, key -> Bucket4j.builder()
                .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1))) // 10 requests per minute
                .build());

        // Consume a token and check availability
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        boolean allowed = probe.isConsumed();
        long remaining = probe.getRemainingTokens();

        return Mono.just(new Response(allowed, remaining));
    }

    @Override
    public Map getConfig() {
        return Map.of();
    }

    @Override
    public Class getConfigClass() {
        return null;
    }

    @Override
    public Object newConfig() {
        return null;
    }
}
