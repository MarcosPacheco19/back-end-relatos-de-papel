package com.relatospapel.ms_books_payments.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.DigitalEntitlementEntity;
import com.relatospapel.ms_books_payments.enums.DeliveryType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Información de un derecho digital (descarga/entitlement)")
public class DigitalEntitlementResponse {

    @Schema(description = "Identificador del derecho digital")
    private UUID id;

    @Schema(description = "Identificador del item del pedido asociado")
    private UUID orderItemId;

    @Schema(description = "Tipo de entrega")
    private DeliveryType deliveryType;

    @Schema(description = "Referencia al recurso entregado (URL, key, etc.)")
    private String resourceRef;

    @Schema(description = "Fecha de activación")
    private LocalDateTime activatedAt;

    public static DigitalEntitlementResponse from(DigitalEntitlementEntity e) {
        return DigitalEntitlementResponse.builder()
                .id(e.getId())
                .orderItemId(e.getOrderItemId())
                .deliveryType(e.getDeliveryType())
                .resourceRef(e.getResourceRef())
                .activatedAt(e.getActivatedAt())
                .build();
    }
}
