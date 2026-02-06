package com.relatospapel.ms_books_catalogue.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_catalogue.dto.request.CategoryCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.response.CategoryResponse;
import com.relatospapel.ms_books_catalogue.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de categorías de libros.
 * Permite crear, listar y consultar categorías.
 * 
 * <p>Endpoints base: /api/v1/catalogue/categories
 * <p>Versión de la API: v1
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/catalogue/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "API para la gestión de categorías de libros")
public class CategoryController {

  private final CategoryService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Crear categoría")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoría creada",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest req) {
    return service.create(req);
  }

  @GetMapping
  @Operation(summary = "Listar categorías")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de categorías",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public List<CategoryResponse> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener categoría por id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría encontrada",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
  })
  public CategoryResponse get(@PathVariable UUID id) {
    return service.get(id);
  }
}

