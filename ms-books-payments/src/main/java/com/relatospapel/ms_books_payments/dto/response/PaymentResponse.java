package com.relatospapel.ms_books_payments.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.PaymentEntity;
import com.relatospapel.ms_books_payments.enums.PaymentMethod;
import com.relatospapel.ms_books_payments.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta con información del pago")
public class PaymentResponse {

  @Schema(description = "Identificador del pago")
  private UUID id;

  @Schema(description = "Identificador del pedido asociado")
  private UUID orderId;

  @Schema(description = "Método de pago")
  private PaymentMethod method;

  @Schema(description = "Estado del pago")
  private PaymentStatus status;

  @Schema(description = "Importe pagado")
  private BigDecimal amount;

  @Schema(description = "Moneda")
  private String currency;

  @Schema(description = "ID de transacción del proveedor")
  private String providerTxId;

  @Schema(description = "Fecha de creación del pago")
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
