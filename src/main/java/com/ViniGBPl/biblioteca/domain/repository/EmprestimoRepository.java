package com.ViniGBPl.biblioteca.domain.repository;

import com.ViniGBPl.biblioteca.domain.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}
