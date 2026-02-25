package com.relatospapel.ms_books_catalogue.service.impl;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_catalogue.dto.request.CategoryCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.response.CategoryResponse;
import com.relatospapel.ms_books_catalogue.entity.CategoryEntity;
import com.relatospapel.ms_books_catalogue.exception.NotFoundException;
import com.relatospapel.ms_books_catalogue.repository.CategoryRepository;
import com.relatospapel.ms_books_catalogue.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;

  @Override
  public CategoryResponse create(CategoryCreateRequest req) {
    CategoryEntity c = CategoryEntity.builder()
        .name(req.getName())
        .description(req.getDescription())
        .build();

    c = repo.save(c);
    return CategoryResponse.builder()
        .id(c.getId())
        .name(c.getName())
        .description(c.getDescription())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> list() {
    return repo.findAll().stream().map(c ->
        CategoryResponse.builder()
            .id(c.getId())
            .name(c.getName())
            .description(c.getDescription())
            .build()
    ).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponse get(UUID id) {
    var c = repo.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
    return CategoryResponse.builder()
        .id(c.getId())
        .name(c.getName())
        .description(c.getDescription())
        .build();
  }
}
