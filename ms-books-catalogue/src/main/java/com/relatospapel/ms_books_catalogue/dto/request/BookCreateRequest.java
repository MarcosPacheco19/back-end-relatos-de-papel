package com.relatospapel.ms_books_catalogue.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookCreateRequest {
  @NotBlank(message = "El título es obligatorio")
  @Size(max = 200, message = "El título no puede exceder 200 caracteres")
  private String title;

  @NotBlank(message = "El autor es obligatorio")
  @Size(max = 150, message = "El autor no puede exceder 150 caracteres")
  private String author;

  private LocalDate publicationDate;
  private UUID categoryId;

  @NotBlank(message = "El ISBN es obligatorio")
  @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
  private String isbn;

  @NotNull(message = "La calificación es obligatoria")
  @Min(value = 1, message = "La calificación mínima es 1")
  @Max(value = 5, message = "La calificación máxima es 5")
  private Integer rating;

  @NotNull(message = "El campo visible es obligatorio")
  private Boolean visible;

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;
}
