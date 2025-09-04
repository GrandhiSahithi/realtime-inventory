package com.example.rtinv.model;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceRecord(
        String regionId,
        String productId,
        BigDecimal price,
        String currency,
        Instant updatedAt
) {}
