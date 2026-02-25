package com.relatospapel.ms_books_catalogue.repository.spec;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.relatospapel.ms_books_catalogue.entity.BookEntity;

public class BookSpecifications {

     public static Specification<BookEntity> byFilters(
      String title,
      String author,
      LocalDate publicationDate,
      UUID categoryId,
      String isbn,
      Integer rating,
      Boolean visible
  ) {
    return Specification.where(titleContains(title))
        .and(authorContains(author))
        .and(publicationDateEquals(publicationDate))
        .and(categoryEquals(categoryId))
        .and(isbnEquals(isbn))
        .and(ratingEquals(rating))
        .and(visibleEquals(visible));
  }

  private static Specification<BookEntity> titleContains(String title) {
    return (root, query, cb) -> (title == null || title.isBlank())
        ? cb.conjunction()
        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
  }

  private static Specification<BookEntity> authorContains(String author) {
    return (root, query, cb) -> (author == null || author.isBlank())
        ? cb.conjunction()
        : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
  }

  private static Specification<BookEntity> publicationDateEquals(LocalDate date) {
    return (root, query, cb) -> (date == null)
        ? cb.conjunction()
        : cb.equal(root.get("publicationDate"), date);
  }

  private static Specification<BookEntity> categoryEquals(UUID categoryId) {
    return (root, query, cb) -> (categoryId == null)
        ? cb.conjunction()
        : cb.equal(root.get("category").get("id"), categoryId);
  }

  private static Specification<BookEntity> isbnEquals(String isbn) {
    return (root, query, cb) -> (isbn == null || isbn.isBlank())
        ? cb.conjunction()
        : cb.equal(root.get("isbn"), isbn);
  }

  private static Specification<BookEntity> ratingEquals(Integer rating) {
    return (root, query, cb) -> (rating == null)
        ? cb.conjunction()
        : cb.equal(root.get("rating"), rating);
  }

  private static Specification<BookEntity> visibleEquals(Boolean visible) {
    return (root, query, cb) -> (visible == null)
        ? cb.conjunction()
        : cb.equal(root.get("visible"), visible);
  }
}
