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

import com.relatospapel.ms_books_payments.dto.request.OrderCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderResponse;
import com.relatospapel.ms_books_payments.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
        @Operation(summary = "Crear orden")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest req) {
        OrderResponse order = orderService.create(req);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Obtener orden por id")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<OrderResponse> get(@PathVariable UUID id) {
        OrderResponse order = orderService.get(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
        @Operation(summary = "Listar órdenes por cliente")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<List<OrderResponse>> listByCustomer(@PathVariable UUID customerId) {
        List<OrderResponse> orders = orderService.listByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
}
