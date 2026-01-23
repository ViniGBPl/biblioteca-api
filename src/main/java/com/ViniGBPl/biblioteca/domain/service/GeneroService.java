package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Genero;
import com.ViniGBPl.biblioteca.domain.repository.GeneroRepository;
// Altere para o import do Spring para manter padrão com LivroService
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroService {

    private final GeneroRepository generoRepository;

    // Método que faltava para o GET /generos
    public List<Genero> listar() {
        return generoRepository.findAll();
    }

    // Método que faltava para o POST /generos
    @Transactional
    public Genero salvar(Genero genero) {
        return generoRepository.save(genero);
    }

    public Genero buscarOuFalhar(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado"));
    }

    @Transactional
    public Genero atualizar(Long id, Genero generoAtualizado) {
        Genero generoExistente = buscarOuFalhar(id);
        generoExistente.setNome(generoAtualizado.getNome());
        return generoRepository.save(generoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Genero genero = buscarOuFalhar(id);

        //  Valida se existem livros vinculados
        if (genero.getLivros() != null && !genero.getLivros().isEmpty()) {
            throw new RuntimeException("Gênero em uso por livros. Não pode ser excluído.");
        }

        generoRepository.delete(genero);
    }
}