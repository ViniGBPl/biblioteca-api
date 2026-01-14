package com.ViniGBPl.biblioteca.domain.repository;

import com.ViniGBPl.biblioteca.domain.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
// Podemos adicionar um método customizado para buscar livros por título
List<Livro> findByTituloContainingIgnoreCase(String titulo);

// Método para buscar apenas livros disponíveis para o sistema de empréstimo
List<Livro> findByDisponivelTrue();

}
