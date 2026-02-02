package com.relatospapel.ms_books_catalogue.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "books",
    uniqueConstraints = @UniqueConstraint(name = "uk_book_isbn", columnNames = "isbn"))
public class BookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @EqualsAndHashCode.Include
  private UUID id;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false, length = 150)
  private String author;

  private LocalDate publicationDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_book_category"))
  @ToString.Exclude
  private CategoryEntity category;

  @Column(nullable = false, length = 20)
  private String isbn;

  @Column(nullable = false)
  private Integer rating;

  @Builder.Default
  @Column(nullable = false)
  private Boolean visible = true;

  @Builder.Default
  @Column(nullable = false)
  private Integer stock = 0;
}