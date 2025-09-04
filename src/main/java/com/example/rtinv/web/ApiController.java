package com.example.rtinv.web;

import com.example.rtinv.model.InventoryRecord;
import com.example.rtinv.model.PriceRecord;
import com.example.rtinv.service.InventoryService;
import com.example.rtinv.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final InventoryService inventoryService;
    private final PriceService priceService;

    public ApiController(InventoryService inventoryService, PriceService priceService) {
        this.inventoryService = inventoryService;
        this.priceService = priceService;
    }

    // GET api v1 inventory storeId S productId P
    @GetMapping("/inventory")
    public ResponseEntity<?> getInventory(
            @RequestParam String storeId,
            @RequestParam String productId
    ) {
        InventoryRecord rec = inventoryService.get(storeId, productId);
        return rec == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(rec);
    }

    // GET api v1 price region R productId P
    @GetMapping("/price")
    public ResponseEntity<?> getPrice(
            @RequestParam String region,
            @RequestParam String productId
    ) {
        PriceRecord rec = priceService.get(region, productId);
        return rec == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(rec);
    }
}
