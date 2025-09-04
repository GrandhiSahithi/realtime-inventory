package com.example.rtinv.web;

import java.time.Instant;
import java.util.List;

public class ReserveDtos {
    public record ReserveItem(String productId, int quantity) {}
    public record ReserveRequest(String storeId, String idempotencyKey, List<ReserveItem> items) {}
    public record ReserveResponse(String holdId, String status, Instant expiresAt, List<ReserveItem> items) {}
}
