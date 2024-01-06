package org.speer.core.util;

import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import org.speer.core.entities.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class RateLimiter {

    private static final Map<String, io.dropwizard.logback.shaded.guava.util.concurrent.RateLimiter> userRateLimiters = new ConcurrentHashMap<>();
    private float permitsPerSecond;

    public void init(float permitsPerSecond){
        RateLimiter.permitsPerSecond = permitsPerSecond;
    }

    public void handleRateLimiting(String username){
        io.dropwizard.logback.shaded.guava.util.concurrent.RateLimiter userRateLimiter =
                userRateLimiters.computeIfAbsent(username, k ->
                        io.dropwizard.logback.shaded.guava.util.concurrent.RateLimiter.create(permitsPerSecond));
        if(!userRateLimiter.tryAcquire()) {
            throw new RuntimeException("TOO many requests!!");
        }

    }
}
