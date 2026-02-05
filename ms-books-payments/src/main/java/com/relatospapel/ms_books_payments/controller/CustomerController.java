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

import com.relatospapel.ms_books_payments.dto.request.CustomerCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.CustomerResponse;
import com.relatospapel.ms_books_payments.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de clientes.
 * Implementa operaciones de creación y consulta de clientes.
 * 
 * <p>Endpoints base: /api/v1/payments/customers
 * <p>Versión de la API: v1
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/payments/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * @param req datos del cliente a crear
     * @return información del cliente creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerCreateRequest req) {
        return service.create(req);
    }

    /**
     * Obtiene la información de un cliente por su ID.
     * 
     * @param id identificador único del cliente
     * @return información del cliente
     */
    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }
}
