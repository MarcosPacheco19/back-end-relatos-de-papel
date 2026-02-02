package com.relatospapel.ms_books_catalogue.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryResponse {
  UUID id;
  String name;
  String description;
}
