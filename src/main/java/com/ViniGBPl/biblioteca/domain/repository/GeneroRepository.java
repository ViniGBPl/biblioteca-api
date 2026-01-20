package com.ViniGBPl.biblioteca.domain.repository;

import com.ViniGBPl.biblioteca.domain.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    // Métodos padrão como save, findAll e findById já estão inclusos
}