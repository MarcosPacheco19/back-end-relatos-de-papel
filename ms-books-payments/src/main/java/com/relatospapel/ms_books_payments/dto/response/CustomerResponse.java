package com.relatospapel.ms_books_payments.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.relatospapel.ms_books_payments.entity.CustomerEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
    private UUID id;
    private String email;
    private String preferredLanguage;
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
