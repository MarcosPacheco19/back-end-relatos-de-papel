package com.relatospapel.ms_books_catalogue.search;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookDocument {
  String id;
  String title;
  String author;
  LocalDate publicationDate;
  String categoryId;
  String categoryName;
  String isbn;
  Integer rating;
  Boolean visible;
  Integer stock;
}