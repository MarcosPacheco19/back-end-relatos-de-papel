package com.relatospapel.ms_books_catalogue.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Disponibilidad del libro en catálogo")
public class BookAvailabilityResponse {
  @Schema(description = "Identificador del libro")
  UUID bookId;

  @Schema(description = "Si el libro existe en catálogo")
  boolean exists;

  @Schema(description = "Si el libro es visible en catálogo")
  boolean visible;

  @Schema(description = "Stock actual")
  int currentStock;

  @Schema(description = "Si el libro está disponible para compra")
  boolean available;

  @Schema(description = "Razón si no está disponible")
  String reason;
}
