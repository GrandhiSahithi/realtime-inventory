package com.example.rtinv.kafka;

import com.example.rtinv.repo.InventoryRepo;
import com.example.rtinv.repo.PriceRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class EventConsumer {
    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final InventoryRepo inventoryRepo;
    private final PriceRepo priceRepo;

    public EventConsumer(InventoryRepo inventoryRepo, PriceRepo priceRepo) {
        this.inventoryRepo = inventoryRepo;
        this.priceRepo = priceRepo;
    }

    @KafkaListener(topics = "${app.kafka.topic:inventory_price_events}", groupId = "${spring.application.name}")
    public void onMessage(ConsumerRecord<String, String> record) {
        String payload = record.value();
        try {
            Map<String, Object> m = mapper.readValue(payload, new TypeReference<>() {});
            String type = eventTypeOf(m);

            switch (type) {
                case "InventoryChanged" -> handleInventoryChanged(m);
                case "PriceChanged"     -> handlePriceChanged(m);
                default -> log.warn("Unknown event type: {} payload={}", type, payload);
            }
        } catch (Exception e) {
            log.error("Failed to process message: {}", payload, e);
        }
    }

    private void handleInventoryChanged(Map<String, Object> m) {
        String storeId   = str(m.get("storeId"));
        String productId = str(m.get("productId"));
        int quantity     = ((Number) m.get("quantity")).intValue();
        Instant ts       = toInstant(m.get("updatedAt"));

        inventoryRepo.upsert(storeId, productId, quantity, ts);
        log.info("Inventory upserted store={} product={} qty={} ts={}", storeId, productId, quantity, ts);
    }

    private void handlePriceChanged(Map<String, Object> m) {
        String regionId  = str(m.get("regionId"));
        String productId = str(m.get("productId"));
        BigDecimal price = new BigDecimal(m.get("price").toString());
        String currency  = str(m.get("currency"));
        Instant ts       = toInstant(m.get("updatedAt"));

        priceRepo.upsert(regionId, productId, price, currency, ts);
        log.info("Price upserted region={} product={} price={} {} ts={}", regionId, productId, price, currency, ts);
    }

    // helpers

    private static String eventTypeOf(Map<String, Object> m) {
        Object t = m.get("type");
        return t == null ? "" : t.toString();
    }

    private static String str(Object o) { return o == null ? null : o.toString(); }

    private static Instant toInstant(Object o) {
        if (o == null) return Instant.now();
        try {
            if (o instanceof Number n) return Instant.ofEpochMilli(n.longValue());
            return Instant.parse(o.toString());
        } catch (Exception e) {
            return Instant.now();
        }
    }
}
