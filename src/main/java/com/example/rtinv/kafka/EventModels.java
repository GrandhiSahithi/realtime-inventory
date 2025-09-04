package com.example.rtinv.kafka;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/** Simple POJOs representing events coming from Kafka. */
public class EventModels {
    public record InventoryChanged(String storeId, String productId, int quantity, Instant updatedAt) {}
    public record PriceChanged(String regionId, String productId, BigDecimal price, String currency, Instant updatedAt) {}

    /** Helper to detect the event type from a parsed JSON map. */
    public static String eventTypeOf(Map<String, Object> m) {
        Object t = m.get("type");
        return t == null ? "" : t.toString();
    }
}
