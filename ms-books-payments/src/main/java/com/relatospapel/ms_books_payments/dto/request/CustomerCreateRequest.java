package com.relatospapel.ms_books_payments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un cliente")
public class CustomerCreateRequest {

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 120)
    @Schema(description = "Email del cliente", example = "user@example.com")
    private String email;

    @Size(max = 10)
    @Schema(description = "Idioma preferido del cliente", example = "es")
    private String preferredLanguage;
}
