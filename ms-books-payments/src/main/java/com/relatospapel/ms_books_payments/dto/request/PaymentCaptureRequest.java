package com.relatospapel.ms_books_payments.dto.request;

import com.relatospapel.ms_books_payments.enums.DeliveryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentCaptureRequest {

  @NotBlank(message = "El id de transacción del proveedor es obligatorio")
  @Size(max = 100, message = "El id de transacción no puede exceder 100 caracteres")
  private String providerTxId;

  @NotNull(message = "El tipo de entrega es obligatorio para productos digitales")
  private DeliveryType deliveryType;

  @NotBlank(message = "La referencia del recurso es obligatoria")
  @Size(max = 255, message = "La referencia del recurso no puede exceder 255 caracteres")
  private String resourceRef;
}
