package com.relatospapel.ms_books_payments.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderCreateRequest {

    @NotNull(message = "El cliente es obligatorio")
    private UUID customerId;

    @NotNull(message = "El total de la orden es obligatorio")
    @Positive(message = "El total debe ser positivo")
    private BigDecimal totalAmount;

    @Size(max = 3, message = "La moneda debe tener máximo 3 caracteres")
    private String currency;

    @Size(max = 50, message = "El código de cupón no puede exceder 50 caracteres")
    private String couponCodeApplied;

    @NotEmpty(message = "La orden debe tener al menos un item")
    @Valid
    private List<OrderItemRequest> items;
}
