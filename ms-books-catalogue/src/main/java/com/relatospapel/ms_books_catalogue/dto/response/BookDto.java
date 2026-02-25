package com.relatospapel.ms_books_catalogue.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    @JsonProperty("Titulo")
    private String titulo;

    @JsonProperty("Autor")
    private String autor;

    @JsonProperty("Genero")
    private String genero;

    @JsonProperty("ISBN")
    private String isbn;

    @JsonProperty("fecha_publicacion")
    private String fecha_publicacion;

    @JsonProperty("estilo")
    private String estilo;

    @JsonProperty("precio")
    private Double precio;
}