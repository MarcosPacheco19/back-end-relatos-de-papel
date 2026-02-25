package com.relatospapel.ms_books_payments.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.OrderItemEntity;
import com.relatospapel.ms_books_payments.enums.BookFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private UUID id;
    private UUID orderId;
    private UUID bookIdRef;
    private String isbnRef;
    private String titleSnapshot;
    private BookFormat formatSnapshot;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public static OrderItemResponse from(OrderItemEntity entity) {
        return OrderItemResponse.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .bookIdRef(entity.getBookIdRef())
                .isbnRef(entity.getIsbnRef())
                .titleSnapshot(entity.getTitleSnapshot())
                .formatSnapshot(entity.getFormatSnapshot())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .lineTotal(entity.getLineTotal())
                .build();
    }
}
