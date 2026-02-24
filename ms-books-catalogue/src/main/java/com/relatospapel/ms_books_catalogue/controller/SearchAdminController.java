package com.relatospapel.ms_books_catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_catalogue.repository.BookRepository;
import com.relatospapel.ms_books_catalogue.search.BookDocument;
import com.relatospapel.ms_books_catalogue.search.BookOpenSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchAdminController {

  private final BookRepository bookRepo;
  private final BookOpenSearchService os;

  @PostMapping("/admin/reindex")
  public ResponseEntity<String> reindex() {
    // CAMBIO: reindex masivo para sincronizar OpenSearch a partir de la BD
    var docs = bookRepo.findAll().stream().map(b ->
        BookDocument.builder()
            .id(b.getId().toString())
            .title(b.getTitle())
            .author(b.getAuthor())
            .publicationDate(b.getPublicationDate())
            .categoryId(b.getCategory() != null ? b.getCategory().getId().toString() : null)
            .categoryName(b.getCategory() != null ? b.getCategory().getName() : null)
            .isbn(b.getIsbn())
            .rating(b.getRating())
            .visible(b.getVisible())
            .stock(b.getStock())
            .build()
    ).toList();

    os.bulkUpsert(docs);
    return ResponseEntity.ok("Reindex OK. Docs=" + docs.size());
  }
}