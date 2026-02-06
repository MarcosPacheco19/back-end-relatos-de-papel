package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.DeliveryType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un derecho digital")
public class DigitalEntitlementCreateRequest {

    @NotNull(message = "El ID del OrderItem es obligatorio")
    @Schema(description = "Identificador del item del pedido asociado", required = true)
    private UUID orderItemId;

    @NotNull(message = "El tipo de entrega es obligatorio")
    @Schema(description = "Tipo de entrega", required = true)
    private DeliveryType deliveryType;

    @NotBlank(message = "La referencia del recurso es obligatoria")
    @Schema(description = "Referencia al recurso (URL, key)")
    private String resourceRef;
}
