package com.relatospapel.ms_books_catalogue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear una categoría")
public class CategoryCreateRequest {
  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
  @Schema(description = "Nombre de la categoría", example = "Ficción")
  private String name;

  @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
  @Schema(description = "Descripción de la categoría")
  private String description;
}
