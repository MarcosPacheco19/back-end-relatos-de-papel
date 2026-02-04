package com.relatospapel.ms_books_payments.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurchaseResponse {
    UUID orderId;
    String status;
    Instant createdAt;
    List<Item> items;

    @Value
    @Builder
    public static class Item {
        UUID bookId;
        Integer quantity;
        String validationReason;
    }
}
