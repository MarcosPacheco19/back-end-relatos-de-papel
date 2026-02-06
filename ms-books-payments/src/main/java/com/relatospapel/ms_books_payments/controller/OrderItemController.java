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

import com.relatospapel.ms_books_payments.dto.request.OrderItemCreateRequest;
import com.relatospapel.ms_books_payments.dto.response.OrderItemResponse;
import com.relatospapel.ms_books_payments.service.OrderItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
        @Operation(summary = "Crear item de orden")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item creado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<OrderItemResponse> create(@Valid @RequestBody OrderItemCreateRequest req) {
        OrderItemResponse item = orderItemService.create(req);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
        @Operation(summary = "Listar items por orden")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de items",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<List<OrderItemResponse>> listByOrder(@PathVariable UUID orderId) {
        List<OrderItemResponse> items = orderItemService.listByOrder(orderId);
        return ResponseEntity.ok(items);
    }
}
