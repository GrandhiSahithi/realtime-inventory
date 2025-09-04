package com.example.rtinv.service;

import com.example.rtinv.model.InventoryRecord;
import com.example.rtinv.repo.InventoryRepo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepo repo;
    private final Cache<String, InventoryRecord> cache;

    public InventoryService(InventoryRepo repo, Caffeine<Object, Object> caffeine) {
        this.repo = repo;
        this.cache = caffeine.build();
    }

    public InventoryRecord get(String storeId, String productId) {
        String key = storeId + "|" + productId;
        return cache.get(key, k -> repo.get(storeId, productId));
    }
}
