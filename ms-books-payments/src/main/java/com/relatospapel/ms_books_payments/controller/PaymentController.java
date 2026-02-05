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

import com.relatospapel.ms_books_payments.dto.request.PaymentCaptureRequest;
import com.relatospapel.ms_books_payments.dto.request.PaymentCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.PaymentResponse;
import com.relatospapel.ms_books_payments.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID id,
                                                   @Valid @RequestBody PaymentCaptureRequest request) {
        PaymentResponse response = service.capture(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentResponse> fail(@PathVariable UUID id) {
        PaymentResponse response = service.fail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> listByOrder(@PathVariable UUID orderId) {
        List<PaymentResponse> response = service.listByOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
