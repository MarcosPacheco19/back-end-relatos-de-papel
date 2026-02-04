package com.relatospapel.ms_books_payments.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.Value;

@Value
public class CustomerResponse {
    UUID id;
    String email;
    String preferredLanguage;
    Instant createdAt;
}
