package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Item básico para crear/actualizar en una orden")
public class OrderItemRequest {
    
    @NotNull(message = "El libro es obligatorio")
    @Schema(description = "Identificador del libro en catálogo")
    private UUID bookIdRef;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    @Schema(description = "Cantidad solicitada")
    private Integer quantity;

    @Size(max = 10)
    @Schema(description = "Formato: PHYSICAL o DIGITAL")
    private String format; // PHYSICAL o DIGITAL
}
