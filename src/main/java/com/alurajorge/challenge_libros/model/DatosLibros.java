package com.alurajorge.challenge_libros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("title") String titulo,

        @JsonAlias("authors") List<DatosAutor> autor,

        @JsonAlias("languages") List<String> idioma,

        @JsonAlias("download_count") int numeroDescargas
) {
    @Override
    public String toString() {
        return  "************DatosLibros**************\n" +
                "titulo='" + titulo + "\n" +
                "autor=" + autor +"\n"+
                "idioma=" + idioma +"\n"+
                "numeroDescargas=" + numeroDescargas +"\n"+
                "*************************************\n";
    }
}
