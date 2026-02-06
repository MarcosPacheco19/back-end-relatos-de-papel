package com.relatospapel.ms_books_payments.dto.request;

import com.relatospapel.ms_books_payments.enums.DeliveryType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para capturar un pago y entregar contenido digital")
public class PaymentCaptureRequest {

  @NotBlank(message = "El id de transacción del proveedor es obligatorio")
  @Size(max = 100, message = "El id de transacción no puede exceder 100 caracteres")
  @Schema(description = "ID de la transacción en el proveedor", example = "tx_12345")
  private String providerTxId;

  @NotNull(message = "El tipo de entrega es obligatorio para productos digitales")
  @Schema(description = "Tipo de entrega para el contenido digital")
  private DeliveryType deliveryType;

  @NotBlank(message = "La referencia del recurso es obligatoria")
  @Size(max = 255, message = "La referencia del recurso no puede exceder 255 caracteres")
  @Schema(description = "Referencia al recurso entregado (URL, key)")
  private String resourceRef;
}
