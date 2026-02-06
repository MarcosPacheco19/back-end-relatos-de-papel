package com.relatospapel.ms_books_payments.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear una orden")
public class OrderCreateRequest {

    @NotNull(message = "El cliente es obligatorio")
    @Schema(description = "Identificador del cliente", required = true)
    private UUID customerId;

    @NotNull(message = "El total de la orden es obligatorio")
    @Positive(message = "El total debe ser positivo")
    @Schema(description = "Importe total de la orden", required = true)
    private BigDecimal totalAmount;

    @Size(max = 3, message = "La moneda debe tener máximo 3 caracteres")
    @Schema(description = "Moneda", example = "EUR")
    private String currency;

    @Size(max = 50, message = "El código de cupón no puede exceder 50 caracteres")
    @Schema(description = "Código de cupón aplicado")
    private String couponCodeApplied;

    @NotEmpty(message = "La orden debe tener al menos un item")
    @Valid
    @Schema(description = "Lista de items de la orden", required = true)
    private List<OrderItemRequest> items;
}
