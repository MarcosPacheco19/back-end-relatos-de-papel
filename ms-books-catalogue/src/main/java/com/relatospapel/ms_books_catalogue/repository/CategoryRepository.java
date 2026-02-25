package com.relatospapel.ms_books_catalogue.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.relatospapel.ms_books_catalogue.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

}
