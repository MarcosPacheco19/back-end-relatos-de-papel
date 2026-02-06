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
import com.relatospapel.ms_books_catalogue.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Crear libro en catálogo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Libro creado",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public BookResponse create(@Valid @RequestBody BookCreateRequest req) {
    return service.create(req);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener libro por id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Libro encontrado",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class))),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public BookResponse get(@PathVariable UUID id) {
    return service.getById(id);
  }

  @GetMapping
  public List<BookResponse> search(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
      @RequestParam(required = false) UUID categoryId,
      @RequestParam(required = false) String isbn,
      @RequestParam(required = false) Integer rating,
      @RequestParam(required = false) Boolean visible
  ) {
    return service.search(title, author, publicationDate, categoryId, isbn, rating, visible);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar parcialmente libro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Libro actualizado",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
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
  @Operation(summary = "Comprobar disponibilidad de un libro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Disponibilidad calculada",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookAvailabilityResponse.class))),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public BookAvailabilityResponse availability(
      @PathVariable UUID id,
      @RequestParam int quantity) {
    return service.availability(id, quantity);
  }
}
