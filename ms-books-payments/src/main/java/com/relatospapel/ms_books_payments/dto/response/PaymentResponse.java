package com.relatospapel.ms_books_payments.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.PaymentEntity;
import com.relatospapel.ms_books_payments.enums.PaymentMethod;
import com.relatospapel.ms_books_payments.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

  private UUID id;
  private UUID orderId;
  private PaymentMethod method;
  private PaymentStatus status;
  private BigDecimal amount;
  private String currency;
  private String providerTxId;
  private LocalDateTime createdAt;

  public static PaymentResponse from(PaymentEntity payment) {
    return PaymentResponse.builder()
        .id(payment.getId())
        .orderId(payment.getOrderId())
        .method(payment.getMethod())
        .status(payment.getStatus())
        .amount(payment.getAmount())
        .currency(payment.getCurrency())
        .providerTxId(payment.getProviderTxId())
        .createdAt(payment.getCreatedAt())
        .build();
  }
}
