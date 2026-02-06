package com.relatospapel.ms_books_catalogue.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Respuesta con información de categoría")
public class CategoryResponse {
  @Schema(description = "Identificador de la categoría")
  UUID id;

  @Schema(description = "Nombre de la categoría")
  String name;

  @Schema(description = "Descripción de la categoría")
  String description;
}
