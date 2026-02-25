package com.relatospapel.ms_books_payments.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerCreateRequest {

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 120)
    private String email;

    @Size(max = 10)
    private String preferredLanguage;
}
