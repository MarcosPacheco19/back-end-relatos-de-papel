package com.relatospapel.ms_books_payments.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.relatospapel.ms_books_payments.entity.OrderEntity;
import com.relatospapel.ms_books_payments.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta con información del pedido")
public class OrderResponse {
    @Schema(description = "Identificador del pedido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Identificador del cliente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID customerId;

    @Schema(description = "Estado del pedido")
    private OrderStatus status;

    @Schema(description = "Importe total del pedido")
    private BigDecimal totalAmount;

    @Schema(description = "Moneda del pedido", example = "EUR")
    private String currency;

    @Schema(description = "Código de cupón aplicado, si existe")
    private String couponCodeApplied;

    @Schema(description = "Fecha de creación del pedido")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización del pedido")
    private LocalDateTime updatedAt;

    @Schema(description = "Lista de items del pedido")
    private List<OrderItemResponse> items;

    public static OrderResponse from(OrderEntity entity) {
        return OrderResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .couponCodeApplied(entity.getCouponCodeApplied())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(entity.getItems() != null ? 
                    entity.getItems().stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList()) : 
                    null)
                .build();
    }
}
