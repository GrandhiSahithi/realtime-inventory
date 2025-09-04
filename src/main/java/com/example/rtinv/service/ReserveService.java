package com.example.rtinv.service;

import com.example.rtinv.web.ReserveDtos.ReserveRequest;
import com.example.rtinv.web.ReserveDtos.ReserveResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ReserveService {

    private final Cache<String, ReserveResponse> idempotencyCache;
    private final long ttlSeconds;

    public ReserveService(@Value("${app.reserve.ttl-seconds:300}") long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
        this.idempotencyCache = Caffeine.newBuilder()
                .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                .maximumSize(50_000)
                .build();
    }

    public ReserveResponse reserve(ReserveRequest req) {
        var key = req.idempotencyKey();
        var cached = key == null ? null : idempotencyCache.getIfPresent(key);
        if (cached != null) return cached;

        var holdId = UUID.randomUUID().toString();
        var expiresAt = Instant.now().plusSeconds(ttlSeconds);
        var resp = new ReserveResponse(holdId, "HELD", expiresAt, req.items());
        if (key != null && !key.isBlank()) {
            idempotencyCache.put(key, resp);
        }
        return resp;
    }
}
