package com.relatospapel.ms_books_catalogue.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_catalogue.dto.request.BookCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.request.BookPatchRequest;
import com.relatospapel.ms_books_catalogue.dto.response.BookAvailabilityResponse;
import com.relatospapel.ms_books_catalogue.dto.response.BookResponse;
import com.relatospapel.ms_books_catalogue.search.BookOpenSearchService; // CAMBIO: OpenSearch para suggest/facets
import com.relatospapel.ms_books_catalogue.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión del catálogo de libros.
 * Implementa operaciones CRUD completas y búsqueda avanzada.
 * 
 * <p>Endpoints base: /api/v1/catalogue/books
 * <p>Versión de la API: v1
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/catalogue/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService service;

  // CAMBIO: se inyecta OpenSearchService SOLO para endpoints adicionales (autocomplete y facets)
  private final BookOpenSearchService os;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookResponse create(@Valid @RequestBody BookCreateRequest req) {
    return service.create(req);
  }

  @GetMapping("/{id}")
  public BookResponse get(@PathVariable UUID id) {
    return service.getById(id);
  }

  @GetMapping
  public List<BookResponse> search(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
      @RequestParam(required = false) UUID categoryId,
      @RequestParam(required = false) String isbn,
      @RequestParam(required = false) Integer rating,
      @RequestParam(required = false) Boolean visible
  ) {
    // CAMBIO: aunque el controller no cambia, internamente service.search(...) ahora consulta OpenSearch (no JPA)
    return service.search(q,title, author, publicationDate, categoryId, isbn, rating, visible);
  }

  /**
   * Endpoint de sugerencias/autocompletado basado en OpenSearch (search_as_you_type).
   *
   * <p>Ejemplo: /api/v1/catalogue/books/suggest?prefix=har
   */
  @GetMapping("/suggest")
  public List<String> suggest(
      @RequestParam String prefix,
      @RequestParam(required = false) Boolean visible
  ) {
    // CAMBIO: autocomplete para cumplir rúbrica (search_as_you_type)
    return os.suggestTitle(prefix, visible);
  }

  /**
   * Endpoint de facets/agregaciones para filtros (por ejemplo, por categoría).
   *
   * <p>Ejemplo: /api/v1/catalogue/books/facets?q=harry&visible=true
   */
  @GetMapping("/facets")
  public BookOpenSearchService.FacetResponse facets(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean visible
  ) {
    // CAMBIO: facets para cumplir rúbrica (aggs/terms)
    return os.searchWithCategoryFacets(q, visible);
  }

  @PatchMapping("/{id}")
  public BookResponse patch(
      @PathVariable UUID id,
      @Valid @RequestBody BookPatchRequest req) {
    return service.patch(id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }

  @GetMapping("/{id}/availability")
  public BookAvailabilityResponse availability(
      @PathVariable UUID id,
      @RequestParam int quantity) {
    return service.availability(id, quantity);
  }
}