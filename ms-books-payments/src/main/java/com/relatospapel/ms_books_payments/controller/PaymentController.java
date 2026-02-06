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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
        @Operation(summary = "Crear pago")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/capture")
        @Operation(summary = "Capturar pago")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago capturado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID id,
                                                   @Valid @RequestBody PaymentCaptureRequest request) {
        PaymentResponse response = service.capture(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/fail")
        @Operation(summary = "Marcar pago como fallido")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago marcado como fallido",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<PaymentResponse> fail(@PathVariable UUID id) {
        PaymentResponse response = service.fail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
        @Operation(summary = "Listar pagos por orden")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
        })
    public ResponseEntity<List<PaymentResponse>> listByOrder(@PathVariable UUID orderId) {
        List<PaymentResponse> response = service.listByOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
