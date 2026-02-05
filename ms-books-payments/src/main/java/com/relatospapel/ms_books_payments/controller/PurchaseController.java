package com.relatospapel.ms_books_payments.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_payments.dto.request.PurchaseCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.PurchaseResponse;
import com.relatospapel.ms_books_payments.service.PurchaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestion de compras.
 * Implementa operaciones de creacion y consulta de ordenes.
 * 
 * <p>Endpoints base: /api/v1/payments/purchases
 * <p>Version de la API: v1
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/payments/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResponse create(@Valid @RequestBody PurchaseCreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public PurchaseResponse get(@PathVariable UUID id) {
        return service.getById(id);
    }
}
