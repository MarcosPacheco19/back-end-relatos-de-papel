package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderItemRequest {
    
    @NotNull(message = "El libro es obligatorio")
    private UUID bookIdRef;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer quantity;

    @Size(max = 10)
    private String format; // PHYSICAL o DIGITAL
}
