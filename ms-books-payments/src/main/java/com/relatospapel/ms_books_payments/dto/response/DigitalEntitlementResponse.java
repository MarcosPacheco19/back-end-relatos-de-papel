package com.relatospapel.ms_books_payments.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.DigitalEntitlementEntity;
import com.relatospapel.ms_books_payments.enums.DeliveryType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DigitalEntitlementResponse {

    private UUID id;
    private UUID orderItemId;
    private DeliveryType deliveryType;
    private String resourceRef;
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
