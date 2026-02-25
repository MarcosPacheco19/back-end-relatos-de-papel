package com.relatospapel.ms_books_payments.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_payments.dto.request.DigitalEntitlementCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.DigitalEntitlementResponse;
import com.relatospapel.ms_books_payments.service.DigitalEntitlementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/digital-entitlements")
@RequiredArgsConstructor
public class DigitalEntitlementController {

    private final DigitalEntitlementService service;

    @PostMapping
    public ResponseEntity<DigitalEntitlementResponse> create(@Valid @RequestBody DigitalEntitlementCreateRequest request) {
        DigitalEntitlementResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<DigitalEntitlementResponse> activate(@PathVariable("id") UUID entitlementId) {
        DigitalEntitlementResponse response = service.activate(entitlementId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DigitalEntitlementResponse>> listByOrder(@PathVariable UUID orderId) {
        List<DigitalEntitlementResponse> response = service.listByOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DigitalEntitlementResponse>> listByCustomer(@PathVariable UUID customerId) {
        List<DigitalEntitlementResponse> response = service.listByCustomer(customerId);
        return ResponseEntity.ok(response);
    }
}
