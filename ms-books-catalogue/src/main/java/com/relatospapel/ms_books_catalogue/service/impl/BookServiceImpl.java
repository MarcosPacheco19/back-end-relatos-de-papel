package com.relatospapel.ms_books_catalogue.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.relatospapel.ms_books_catalogue.dto.request.BookCreateRequest;
import com.relatospapel.ms_books_catalogue.dto.request.BookPatchRequest;
import com.relatospapel.ms_books_catalogue.dto.response.BookAvailabilityResponse;
import com.relatospapel.ms_books_catalogue.dto.response.BookResponse;
import com.relatospapel.ms_books_catalogue.entity.BookEntity;
import com.relatospapel.ms_books_catalogue.exception.NotFoundException;
import com.relatospapel.ms_books_catalogue.repository.BookRepository;
import com.relatospapel.ms_books_catalogue.repository.CategoryRepository;
import com.relatospapel.ms_books_catalogue.search.BookOpenSearchService;  // CAMBIO
import com.relatospapel.ms_books_catalogue.search.BookSearchMapper;       // CAMBIO
import com.relatospapel.ms_books_catalogue.service.BookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepo;
  private final CategoryRepository categoryRepo;

  // CAMBIO: OpenSearch se usa para sincronizar y para realizar la búsqueda (único motor)
  private final BookOpenSearchService os;

  @Override
  public BookResponse create(BookCreateRequest req) {
    BookEntity book = BookEntity.builder()
        .title(req.getTitle())
        .author(req.getAuthor())
        .publicationDate(req.getPublicationDate())
        .isbn(req.getIsbn())
        .rating(req.getRating())
        .visible(req.getVisible())
        .stock(req.getStock())
        .build();

    if (req.getCategoryId() != null) {
      var cat = categoryRepo.findById(req.getCategoryId())
          .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
      book.setCategory(cat);
    }

    BookResponse resp = toResponse(bookRepo.save(book));

    // CAMBIO: indexar en OpenSearch al crear
    os.upsert(BookSearchMapper.toDocument(resp));

    return resp;
  }

  @Override
  @Transactional(readOnly = true)
  public BookResponse getById(UUID id) {
    return toResponse(bookRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Libro no encontrado")));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookResponse> search(String q, String title, String author, LocalDate publicationDate,
                                   UUID categoryId, String isbn, Integer rating, Boolean visible) {
    // CAMBIO: búsqueda SOLO por OpenSearch (se eliminó BookSpecifications)
    var docs = os.search(
        q,
        title,
        author,
        publicationDate,
        categoryId != null ? categoryId.toString() : null,
        isbn,
        rating,
        visible
    );

    return docs.stream().map(BookSearchMapper::toResponse).toList();
  }

  @Override
  public BookResponse patch(UUID id, BookPatchRequest req) {
    var b = bookRepo.findById(id).orElseThrow(() -> new NotFoundException("Libro no encontrado"));

    if (req.getTitle() != null) b.setTitle(req.getTitle());
    if (req.getAuthor() != null) b.setAuthor(req.getAuthor());
    if (req.getPublicationDate() != null) b.setPublicationDate(req.getPublicationDate());
    if (req.getIsbn() != null) b.setIsbn(req.getIsbn());
    if (req.getRating() != null) b.setRating(req.getRating());
    if (req.getVisible() != null) b.setVisible(req.getVisible());
    if (req.getStock() != null) b.setStock(req.getStock());

    if (req.getCategoryId() != null) {
      var cat = categoryRepo.findById(req.getCategoryId())
          .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
      b.setCategory(cat);
    }

    BookResponse resp = toResponse(bookRepo.save(b));

    // CAMBIO: re-indexar en OpenSearch al modificar
    os.upsert(BookSearchMapper.toDocument(resp));

    return resp;
  }

  @Override
  public void delete(UUID id) {
    if (!bookRepo.existsById(id)) throw new NotFoundException("Libro no encontrado");
    bookRepo.deleteById(id);

    // CAMBIO: borrar del índice OpenSearch al eliminar
    os.deleteById(id.toString());
  }

  @Override
  @Transactional(readOnly = true)
  public BookAvailabilityResponse availability(UUID id, int quantity) {
    var opt = bookRepo.findById(id);

    if (opt.isEmpty()) {
      return BookAvailabilityResponse.builder()
          .bookId(id).exists(false).visible(false).currentStock(0).available(false).reason("NO_ENCONTRADO")
          .build();
    }

    var b = opt.get();
    boolean visible = Boolean.TRUE.equals(b.getVisible());
    Integer stockValue = b.getStock();
    int stock = stockValue == null ? 0 : stockValue;

    if (!visible) {
      return BookAvailabilityResponse.builder()
          .bookId(id).exists(true).visible(false).currentStock(stock).available(false).reason("OCULTO")
          .build();
    }
    if (quantity <= 0) {
      return BookAvailabilityResponse.builder()
          .bookId(id).exists(true).visible(true).currentStock(stock).available(false).reason("CANTIDAD_INVALIDA")
          .build();
    }
    if (stock < quantity) {
      return BookAvailabilityResponse.builder()
          .bookId(id).exists(true).visible(true).currentStock(stock).available(false).reason("SIN_STOCK")
          .build();
    }

    return BookAvailabilityResponse.builder()
        .bookId(id).exists(true).visible(true).currentStock(stock).available(true).reason("OK")
        .build();
  }

  private BookResponse toResponse(BookEntity b) {
    return BookResponse.builder()
        .id(b.getId())
        .title(b.getTitle())
        .author(b.getAuthor())
        .publicationDate(b.getPublicationDate())
        .isbn(b.getIsbn())
        .rating(b.getRating())
        .visible(b.getVisible())
        .stock(b.getStock())
        .categoryId(b.getCategory() != null ? b.getCategory().getId() : null)
        .categoryName(b.getCategory() != null ? b.getCategory().getName() : null)
        .build();
  }
}