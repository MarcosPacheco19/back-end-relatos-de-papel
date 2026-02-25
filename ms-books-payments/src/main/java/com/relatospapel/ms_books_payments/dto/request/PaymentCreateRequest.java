package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateRequest {

  @NotNull(message = "El id de la orden es obligatorio")
  private UUID orderId;

  @NotNull(message = "El método de pago es obligatorio")
  private PaymentMethod method;
}
