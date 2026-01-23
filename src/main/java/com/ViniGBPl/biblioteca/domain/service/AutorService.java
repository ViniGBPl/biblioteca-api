package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AutorService {
    private final AutorRepository autorRepository;

    @Transactional
    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarOurFalhar(Long id) {
        return autorRepository.findById(id).orElseThrow(() -> new RuntimeException("Autor não encontrado"));


    }

    @Transactional
    public Autor atualizar(Long id, Autor autorAtualizado) {
        Autor autorExistente = buscarOurFalhar(id);
        autorExistente.setNome(autorAtualizado.getNome());
        autorExistente.setNacionalidade(autorAtualizado.getNacionalidade());
        return autorRepository.save(autorExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarOurFalhar(id);
        // Não deletar autor que possui livros
        if (autor.getLivros() != null && !autor.getLivros().isEmpty()) {
            throw new RuntimeException("Não é possível excluir um autor que possui livros cadastrados.");
        }
        autorRepository.delete(autor);
    }

}
