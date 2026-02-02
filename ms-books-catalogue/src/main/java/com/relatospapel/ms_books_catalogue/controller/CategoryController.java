package com.relatospapel.ms_books_catalogue.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.relatospapel.ms_books_catalogue.dto.request.CategoryCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.response.CategoryResponse;
import com.relatospapel.ms_books_catalogue.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "API para la gestión de categorías de libros")
public class CategoryController {

  private final CategoryService service;

  @Operation(summary = "Crear categoría", description = "Registra una nueva categoría de libros")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
  })
  @PostMapping
  public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest req) {
    return service.create(req);
  }

  @Operation(summary = "Listar categorías", description = "Obtiene la lista completa de categorías")
  @ApiResponse(responseCode = "200", description = "Lista de categorías")
  @GetMapping
  public List<CategoryResponse> list() {
    return service.list();
  }

  @Operation(summary = "Obtener categoría por ID", description = "Recupera la información de una categoría específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
  })
  @GetMapping("/{id}")
  public CategoryResponse get(
      @Parameter(description = "ID único de la categoría") @PathVariable UUID id) {
    return service.get(id);
  }
}

