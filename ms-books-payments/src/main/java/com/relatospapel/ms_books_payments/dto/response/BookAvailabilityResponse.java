package com.relatospapel.ms_books_payments.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Disponibilidad del libro")
public class BookAvailabilityResponse {
    @Schema(description = "Identificador del libro")
    UUID bookId;

    @Schema(description = "Indica si el libro existe en catálogo")
    boolean exists;

    @Schema(description = "Si el libro está visible en el catálogo")
    boolean visible;

    @Schema(description = "Stock actual")
    int currentStock;

    @Schema(description = "Si está disponible para compra")
    boolean available;

    @Schema(description = "Razón si no está disponible")
    String reason;
}
