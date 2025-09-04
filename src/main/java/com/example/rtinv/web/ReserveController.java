package com.example.rtinv.web;

import com.example.rtinv.service.ReserveService;
import com.example.rtinv.web.ReserveDtos.ReserveRequest;
import com.example.rtinv.web.ReserveDtos.ReserveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveResponse> reserve(
            @RequestBody ReserveRequest req,
            @RequestHeader(value = "Idempotency-Key", required = false) String idemHeader) {

        String key = (idemHeader != null && !idemHeader.isBlank())
                ? idemHeader
                : req.idempotencyKey();

        ReserveRequest effective = new ReserveRequest(req.storeId(), key, req.items());
        ReserveResponse resp = reserveService.reserve(effective);

        return ResponseEntity.ok(resp);
    }
}
