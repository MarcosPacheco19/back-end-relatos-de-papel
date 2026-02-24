package com.relatospapel.ms_books_catalogue.search;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.StringTermsAggregate;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookOpenSearchService {

  private final OpenSearchClient client;

  @Value("${opensearch.index:books}")
  private String index;

  // =========================
  // CAMBIO: sincronización BD -> OpenSearch
  // =========================
  public void upsert(BookDocument doc) {
    try {
      client.index(i -> i.index(index).id(doc.getId()).document(doc));
    } catch (IOException e) {
      throw new RuntimeException("OpenSearch upsert falló (id=" + doc.getId() + ")", e);
    }
  }

  public void deleteById(String id) {
    try {
      client.delete(d -> d.index(index).id(id));
    } catch (IOException e) {
      throw new RuntimeException("OpenSearch delete falló (id=" + id + ")", e);
    }
  }

  public void bulkUpsert(List<BookDocument> docs) {
    if (docs == null || docs.isEmpty()) return;
    try {
      BulkRequest.Builder bulk = new BulkRequest.Builder();
      for (BookDocument d : docs) {
        bulk.operations(op -> op.index(ix -> ix.index(index).id(d.getId()).document(d)));
      }
      var resp = client.bulk(bulk.build());
      if (Boolean.TRUE.equals(resp.errors())) {
        throw new RuntimeException("OpenSearch bulk terminó con errores");
      }
    } catch (IOException e) {
      throw new RuntimeException("OpenSearch bulk falló", e);
    }
  }

  // =========================
  // CAMBIO: búsqueda principal (full-text + filtros)
  // =========================
  public List<BookDocument> search(
      String q,
      String title,
      String author,
      LocalDate publicationDate,
      String categoryId,
      String isbn,
      Integer rating,
      Boolean visible
  ) {
    try {
      Query query = buildQuery(q, title, author, publicationDate, categoryId, isbn, rating, visible);

      SearchResponse<BookDocument> resp = client.search(s -> s
              .index(index)
              .query(query)
              .size(50),
          BookDocument.class
      );

      return resp.hits().hits().stream()
          .map(h -> h.source())
          .filter(Objects::nonNull)
          .toList();

    } catch (IOException e) {
      throw new RuntimeException("OpenSearch search falló", e);
    }
  }

  // =========================
  // CAMBIO: facets (aggs) por categoría (keyword)
  // =========================
  public FacetResponse searchWithCategoryFacets(String q, Boolean visible) {
    try {
      Query query = buildQuery(q, null, null, null, null, null, null, visible);

      SearchResponse<BookDocument> resp = client.search(s -> s
              .index(index)
              .query(query)
              .size(50)
              .aggregations("categories", a -> a.terms(t -> t.field("categoryName")))
          , BookDocument.class);

      List<BookDocument> docs = resp.hits().hits().stream()
          .map(h -> h.source()).filter(Objects::nonNull).toList();

      var agg = resp.aggregations().get("categories");
      List<FacetItem> categories = new ArrayList<>();

      if (agg != null && agg.isSterms()) {
        StringTermsAggregate terms = agg.sterms();
        if (terms.buckets().isArray()) {
          terms.buckets().array().forEach(b ->
              categories.add(new FacetItem(b.key(), b.docCount()))
          );
        }
      }

      return new FacetResponse(docs, categories);

    } catch (IOException e) {
      throw new RuntimeException("OpenSearch facets falló", e);
    }
  }

  // =========================
  // CAMBIO: autocomplete con search_as_you_type (title)
  // =========================
  public List<String> suggestTitle(String prefix, Boolean visible) {
    try {
      BoolQuery.Builder bool = new BoolQuery.Builder();

      bool.must(Query.of(q -> q.multiMatch(mm -> mm
          .query(prefix)
          .type(TextQueryType.BoolPrefix)
          .fields("title", "title._2gram", "title._3gram")
      )));

      if (visible != null) {
        bool.filter(Query.of(f -> f.term(t -> t.field("visible").value(FieldValue.of(visible)))));
      }

      SearchResponse<BookDocument> resp = client.search(s -> s
              .index(index)
              .query(Query.of(x -> x.bool(bool.build())))
              .size(10),
          BookDocument.class
      );

      return resp.hits().hits().stream()
          .map(h -> h.source())
          .filter(Objects::nonNull)
          .map(BookDocument::getTitle)
          .filter(Objects::nonNull)
          .distinct()
          .toList();

    } catch (IOException e) {
      throw new RuntimeException("OpenSearch suggest falló", e);
    }
  }

  private Query buildQuery(String q, String title, String author, LocalDate publicationDate,
                           String categoryId, String isbn, Integer rating, Boolean visible) {

    BoolQuery.Builder bool = new BoolQuery.Builder();

    // Full-text
    if (q != null && !q.isBlank()) {
      bool.must(Query.of(m -> m.multiMatch(mm -> mm
          .query(q)
          .fields("title", "author", "categoryName", "isbn")
      )));
    }

    // Campos text
    if (title != null && !title.isBlank()) {
      bool.must(Query.of(m -> m.match(mt -> mt.field("title").query(FieldValue.of(title)))));
    }
    if (author != null && !author.isBlank()) {
      bool.must(Query.of(m -> m.match(mt -> mt.field("author").query(FieldValue.of(author)))));
    }

    // Fecha exacta (si tu mapping publicationDate es date)
    if (publicationDate != null) {
      bool.filter(Query.of(f -> f.term(t -> t.field("publicationDate")
          .value(FieldValue.of(publicationDate.toString())))));
    }

    // Exactos
    if (isbn != null && !isbn.isBlank()) {
      bool.filter(Query.of(f -> f.term(t -> t.field("isbn").value(FieldValue.of(isbn)))));
    }
    if (categoryId != null && !categoryId.isBlank()) {
      bool.filter(Query.of(f -> f.term(t -> t.field("categoryId").value(FieldValue.of(categoryId)))));
    }
    if (rating != null) {
      bool.filter(Query.of(f -> f.term(t -> t.field("rating").value(FieldValue.of(rating)))));
    }
    if (visible != null) {
      bool.filter(Query.of(f -> f.term(t -> t.field("visible").value(FieldValue.of(visible)))));
    }

    return Query.of(qb -> qb.bool(bool.build()));
  }

  public record FacetItem(String key, long count) {}
  public record FacetResponse(List<BookDocument> results, List<FacetItem> categories) {}
}