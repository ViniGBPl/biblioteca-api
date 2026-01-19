package com.ViniGBPl.biblioteca.domain.repository;

import com.ViniGBPl.biblioteca.domain.model.FilaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {
    // Procura a fila de um livro específico ordenada por data
    List<FilaEspera> findByLivroIdOrderByDataSolicitacaoAsc(Long livroId);
}
