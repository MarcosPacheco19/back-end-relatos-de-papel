package com.relatospapel.ms_books_payments.dto.request;

import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.PaymentMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un pago")
public class PaymentCreateRequest {

  @NotNull(message = "El id de la orden es obligatorio")
  @Schema(description = "Identificador de la orden", required = true)
  private UUID orderId;

  @NotNull(message = "El método de pago es obligatorio")
  @Schema(description = "Método de pago", required = true)
  private PaymentMethod method;
}
