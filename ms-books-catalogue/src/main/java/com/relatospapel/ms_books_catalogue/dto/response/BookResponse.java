package com.relatospapel.ms_books_catalogue.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Respuesta con información del libro")
public class BookResponse {
  @Schema(description = "Identificador del libro")
  UUID id;

  @Schema(description = "Título del libro")
  String title;

  @Schema(description = "Autor del libro")
  String author;

  @Schema(description = "Fecha de publicación")
  LocalDate publicationDate;

  @Schema(description = "Identificador de la categoría")
  UUID categoryId;

  @Schema(description = "Nombre de la categoría")
  String categoryName;

  @Schema(description = "ISBN del libro")
  String isbn;

  @Schema(description = "Valoración promedio")
  Integer rating;

  @Schema(description = "Si el libro es visible en catálogo")
  Boolean visible;

  @Schema(description = "Stock disponible")
  Integer stock;
}
