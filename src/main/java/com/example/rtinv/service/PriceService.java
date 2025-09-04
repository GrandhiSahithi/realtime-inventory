package com.example.rtinv.service;

import com.example.rtinv.model.PriceRecord;
import com.example.rtinv.repo.PriceRepo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final PriceRepo repo;
    private final Cache<String, PriceRecord> cache;

    public PriceService(PriceRepo repo, Caffeine<Object, Object> caffeine) {
        this.repo = repo;
        this.cache = caffeine.build();
    }

    public PriceRecord get(String regionId, String productId) {
        String key = regionId + "|" + productId;
        return cache.get(key, k -> repo.get(regionId, productId));
    }
}
