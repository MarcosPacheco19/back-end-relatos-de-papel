package com.relatospapel.ms_books_payments.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.relatospapel.ms_books_payments.enums.BookFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un item de pedido")
public class OrderItemCreateRequest {

    @NotNull(message = "El ID de la orden es obligatorio")
    @Schema(description = "Identificador de la orden", required = true)
    private UUID orderId;

    @NotNull(message = "El ID del libro es obligatorio")
    @Schema(description = "Identificador del libro en catálogo", required = true)
    private UUID bookIdRef;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20)
    @Schema(description = "ISBN del libro", example = "978-3-16-148410-0")
    private String isbnRef;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    @Schema(description = "Título del libro")
    private String titleSnapshot;

    @NotNull(message = "El formato es obligatorio")
    @Schema(description = "Formato del libro (ej. EPUB, PDF)")
    private BookFormat formatSnapshot;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    @Schema(description = "Cantidad solicitada")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Schema(description = "Precio unitario")
    private BigDecimal unitPrice;
}
