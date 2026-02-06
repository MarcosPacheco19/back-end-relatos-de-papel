package com.relatospapel.ms_books_payments.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Respuesta de la operación de compra")
public class PurchaseResponse {
    @Schema(description = "Identificador del pedido")
    UUID orderId;

    @Schema(description = "Estado de la compra")
    String status;

    @Schema(description = "Fecha de creación")
    Instant createdAt;

    @Schema(description = "Items procesados en la compra")
    List<Item> items;

    @Value
    @Builder
    @Schema(description = "Detalle de un item en la compra")
    public static class Item {
        @Schema(description = "Identificador del libro")
        UUID bookId;

        @Schema(description = "Cantidad solicitada")
        Integer quantity;

        @Schema(description = "Razón de validación si existe")
        String validationReason;
    }
}
