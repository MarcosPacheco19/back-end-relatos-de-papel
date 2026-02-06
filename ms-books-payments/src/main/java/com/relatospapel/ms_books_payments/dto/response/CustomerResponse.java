package com.relatospapel.ms_books_payments.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.CustomerEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta con datos del cliente")
public class CustomerResponse {
    @Schema(description = "Identificador único del cliente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Email del cliente", example = "user@example.com")
    private String email;

    @Schema(description = "Idioma preferido del cliente", example = "es")
    private String preferredLanguage;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    public static CustomerResponse from(CustomerEntity entity) {
        return CustomerResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .preferredLanguage(entity.getPreferredLanguage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
