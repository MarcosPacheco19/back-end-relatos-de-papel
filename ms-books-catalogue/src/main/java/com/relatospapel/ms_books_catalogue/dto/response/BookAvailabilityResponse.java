package com.relatospapel.ms_books_catalogue.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookAvailabilityResponse {
  UUID bookId;
  boolean exists;
  boolean visible;
  int currentStock;
  boolean available;
  String reason;
}
