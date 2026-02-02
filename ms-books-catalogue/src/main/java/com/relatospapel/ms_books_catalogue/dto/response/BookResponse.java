package com.relatospapel.ms_books_catalogue.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookResponse {
  UUID id;
  String title;
  String author;
  LocalDate publicationDate;
  UUID categoryId;
  String categoryName;
  String isbn;
  Integer rating;
  Boolean visible;
  Integer stock;
}
