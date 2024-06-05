package com.alurajorge.challenge_libros.repository;

import com.alurajorge.challenge_libros.model.Autor;
import com.alurajorge.challenge_libros.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libros, Long> {
    List<Libros> findLibrosByIdioma(String idioma);
    List<Libros> findTop10ByOrderByNumeroDescargasDesc();

}
