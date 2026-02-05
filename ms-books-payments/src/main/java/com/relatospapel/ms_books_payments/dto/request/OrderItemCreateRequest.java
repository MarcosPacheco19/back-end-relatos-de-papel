package com.relatospapel.ms_books_payments.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.BookFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderItemCreateRequest {

    @NotNull(message = "El ID de la orden es obligatorio")
    private UUID orderId;

    @NotNull(message = "El ID del libro es obligatorio")
    private UUID bookIdRef;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20)
    private String isbnRef;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String titleSnapshot;

    @NotNull(message = "El formato es obligatorio")
    private BookFormat formatSnapshot;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal unitPrice;
}
