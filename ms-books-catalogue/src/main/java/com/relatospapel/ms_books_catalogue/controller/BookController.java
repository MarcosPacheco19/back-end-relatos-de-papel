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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "API para la gestión del catálogo de libros")
public class BookController {

  private final BookService service;

  @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro en el catálogo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Libro creado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
  })
  @PostMapping
  public BookResponse create(@Valid @RequestBody BookCreateRequest req) {
    return service.create(req);
  }

  @Operation(summary = "Obtener libro por ID", description = "Recupera la información de un libro específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Libro encontrado"),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado")
  })
  @GetMapping("/{id}")
  public BookResponse get(
      @Parameter(description = "ID único del libro") @PathVariable UUID id) {
    return service.getById(id);
  }

  @Operation(summary = "Buscar libros", description = "Busca libros aplicando filtros opcionales")
  @ApiResponse(responseCode = "200", description = "Lista de libros encontrados")
  @GetMapping
  public List<BookResponse> search(
      @Parameter(description = "Filtrar por título (búsqueda parcial)") @RequestParam(required = false) String title,
      @Parameter(description = "Filtrar por autor (búsqueda parcial)") @RequestParam(required = false) String author,
      @Parameter(description = "Filtrar por fecha de publicación") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
      @Parameter(description = "Filtrar por ID de categoría") @RequestParam(required = false) UUID categoryId,
      @Parameter(description = "Filtrar por ISBN exacto") @RequestParam(required = false) String isbn,
      @Parameter(description = "Filtrar por calificación (1-5)") @RequestParam(required = false) Integer rating,
      @Parameter(description = "Filtrar por visibilidad") @RequestParam(required = false) Boolean visible
  ) {
    return service.search(title, author, publicationDate, categoryId, isbn, rating, visible);
  }

  @Operation(summary = "Actualizar libro", description = "Actualiza parcialmente la información de un libro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
  })
  @PatchMapping("/{id}")
  public BookResponse patch(
      @Parameter(description = "ID único del libro") @PathVariable UUID id,
      @Valid @RequestBody BookPatchRequest req) {
    return service.patch(id, req);
  }

  @Operation(summary = "Eliminar libro", description = "Elimina un libro del catálogo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
      @ApiResponse(responseCode = "404", description = "Libro no encontrado")
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @Parameter(description = "ID único del libro") @PathVariable UUID id) {
    service.delete(id);
  }

  @Operation(summary = "Verificar disponibilidad", description = "Verifica si hay stock suficiente de un libro")
  @ApiResponse(responseCode = "200", description = "Información de disponibilidad")
  @GetMapping("/{id}/availability")
  public BookAvailabilityResponse availability(
      @Parameter(description = "ID único del libro") @PathVariable UUID id,
      @Parameter(description = "Cantidad requerida") @RequestParam int quantity) {
    return service.availability(id, quantity);
  }
}
