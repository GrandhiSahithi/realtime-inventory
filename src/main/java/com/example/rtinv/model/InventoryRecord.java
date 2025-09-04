package com.example.rtinv.model;

import java.time.Instant;

public record InventoryRecord(
        String storeId,
        String productId,
        int quantity,
        Instant updatedAt
) {}
