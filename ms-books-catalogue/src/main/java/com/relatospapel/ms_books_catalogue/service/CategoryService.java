package com.relatospapel.ms_books_catalogue.service;

import java.util.List;
import java.util.UUID;

import com.relatospapel.ms_books_catalogue.dto.request.CategoryCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.response.CategoryResponse;

public interface CategoryService {
  CategoryResponse create(CategoryCreateRequest req);
  List<CategoryResponse> list();
  CategoryResponse get(UUID id);
}
