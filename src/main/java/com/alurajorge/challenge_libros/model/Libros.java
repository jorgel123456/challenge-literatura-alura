package com.alurajorge.challenge_libros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;



import java.util.List;

@Data

@Entity
@Table(name="libro")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String idioma;

   private int numeroDescargas;

   public Libros(){}
    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor=new Autor(datosLibros.autor().get(0));
        this.idioma=datosLibros.idioma().get(0).toUpperCase();
        this.numeroDescargas = datosLibros.numeroDescargas();
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
                        return "********* Libro Encontrado********\n" +
                                "Titulo='" + titulo + "\n" +
                                "Autor=" + autor.getNombre() +"\n"+
                                "Idioma='" + idioma + "\n"+
                                "Numero de Descargas=" + numeroDescargas +"\n"+
                               "**********************************\n";
    }
}
