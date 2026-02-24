package com.relatospapel.ms_books_catalogue.search;

import java.util.UUID;

import com.relatospapel.ms_books_catalogue.dto.response.BookResponse;

public class BookSearchMapper {

  private BookSearchMapper() {}

  // CAMBIO: BookResponse -> BookDocument (para indexación)
  public static BookDocument toDocument(BookResponse r) {
    return BookDocument.builder()
        .id(r.getId().toString())
        .title(r.getTitle())
        .author(r.getAuthor())
        .publicationDate(r.getPublicationDate())
        .categoryId(r.getCategoryId() != null ? r.getCategoryId().toString() : null)
        .categoryName(r.getCategoryName())
        .isbn(r.getIsbn())
        .rating(r.getRating())
        .visible(r.getVisible())
        .stock(r.getStock())
        .build();
  }

  // CAMBIO: BookDocument -> BookResponse (respuesta de endpoints)
  public static BookResponse toResponse(BookDocument d) {
    return BookResponse.builder()
        .id(d.getId() != null ? UUID.fromString(d.getId()) : null)
        .title(d.getTitle())
        .author(d.getAuthor())
        .publicationDate(d.getPublicationDate())
        .categoryId(d.getCategoryId() != null ? UUID.fromString(d.getCategoryId()) : null)
        .categoryName(d.getCategoryName())
        .isbn(d.getIsbn())
        .rating(d.getRating())
        .visible(d.getVisible())
        .stock(d.getStock())
        .build();
  }
}