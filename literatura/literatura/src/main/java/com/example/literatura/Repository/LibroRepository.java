package com.example.literatura.Repository;

import com.example.literatura.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitleContains(String title);
    List<Libro> findByLanguageContains(String language);
}
