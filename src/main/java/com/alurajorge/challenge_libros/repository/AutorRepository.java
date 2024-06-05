package com.alurajorge.challenge_libros.repository;

import com.alurajorge.challenge_libros.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findAutorByNombre(String nombre);
    @Query(value = "SELECT * FROM autores WHERE :agno >= fecha_nacimiento AND :agno <= fecha_fallecimiento", nativeQuery = true)
    List<Autor> autoresVivosPorFecha(int agno);

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Autor mostrarAutorPorNombre( String nombre);
    @Query("SELECT a FROM Autor a WHERE :fecha <= a.fechaNacimiento")
    List<Autor> mostrarAutorPorFechaNacimiento(Integer fecha);
}
