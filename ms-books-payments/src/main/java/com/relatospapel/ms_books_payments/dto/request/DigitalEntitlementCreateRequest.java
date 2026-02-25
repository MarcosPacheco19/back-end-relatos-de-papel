package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.DeliveryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DigitalEntitlementCreateRequest {

    @NotNull(message = "El ID del OrderItem es obligatorio")
    private UUID orderItemId;

    @NotNull(message = "El tipo de entrega es obligatorio")
    private DeliveryType deliveryType;

    @NotBlank(message = "La referencia del recurso es obligatoria")
    private String resourceRef;
}
