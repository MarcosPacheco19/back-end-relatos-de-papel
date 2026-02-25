package com.relatospapel.ms_books_catalogue.controller;

import com.relatospapel.ms_books_catalogue.dto.response.BookDto;
import com.relatospapel.ms_books_catalogue.dto.response.BookResponse;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private final OpenSearchClient client;

    public TestController(OpenSearchClient client) {
        this.client = client;
    }

    @GetMapping("/test-es")
    public String testConnection() throws Exception {
        return "Conectado a cluster: " + client.info().clusterName();
    }

    @GetMapping("/books")
    public List<BookResponse> getAllBooks() throws Exception {
        try {
            // Realizar búsqueda en OpenSearch
            SearchResponse<BookDto> response = client.search(
                    s -> s.index("books_catalogue").size(10),
                    BookDto.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Mapear BookDto a BookResponse
            return response.hits().hits().stream()
                    .map(hit -> {
                        BookDto b = hit.source();
                        return BookResponse.builder()
                                .id(null) // No hay UUID en el índice, puedes usar hit.id() si quieres
                                .title(b.getTitulo())
                                .author(b.getAutor())
                                .publicationDate(LocalDate.parse(b.getFecha_publicacion(), formatter))
                                .categoryId(null) // No hay categoría en el índice
                                .categoryName(null)
                                .isbn(b.getIsbn())
                                .rating(null)
                                .visible(true)
                                .stock(null)
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error real en consola
            throw new RuntimeException("Error al obtener libros: " + e.getMessage());
        }
    }
}