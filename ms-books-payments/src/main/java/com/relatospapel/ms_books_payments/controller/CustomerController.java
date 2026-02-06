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

import com.relatospapel.ms_books_payments.dto.request.CustomerCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.CustomerResponse;
import com.relatospapel.ms_books_payments.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
        @Operation(summary = "Crear cliente")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
        CustomerResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Obtener cliente por id")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<CustomerResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
        @Operation(summary = "Listar clientes")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<List<CustomerResponse>> list() {
        return ResponseEntity.ok(service.list());
    }
}
