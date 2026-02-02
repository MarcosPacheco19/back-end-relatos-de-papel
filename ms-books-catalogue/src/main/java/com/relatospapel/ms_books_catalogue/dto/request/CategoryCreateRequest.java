package com.relatospapel.ms_books_catalogue.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequest {
  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
  private String name;

  @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
  private String description;
}
