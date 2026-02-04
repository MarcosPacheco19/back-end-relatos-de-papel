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

import com.relatospapel.ms_books_catalogue.constant.ApiConstants;
import com.relatospapel.ms_books_catalogue.dto.request.CategoryCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.response.CategoryResponse;
import com.relatospapel.ms_books_catalogue.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de categorías de libros.
 * Permite crear, listar y consultar categorías.
 * 
 * <p>Endpoints base: {@value ApiConstants#CATEGORIES_PATH}
 * <p>Versión de la API: {@value ApiConstants#API_VERSION}
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@RestController
@RequestMapping(ApiConstants.CATEGORIES_PATH)
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest req) {
    return service.create(req);
  }

  @GetMapping
  public List<CategoryResponse> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  public CategoryResponse get(@PathVariable UUID id) {
    return service.get(id);
  }
}

