package com.relatospapel.ms_books_payments.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailabilityResponse {
    UUID bookId;
    boolean exists;
    boolean visible;
    int currentStock;
    boolean available;
    String reason;
}
