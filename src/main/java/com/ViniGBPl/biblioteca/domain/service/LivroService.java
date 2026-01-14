package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorService autorService;

    @Transactional
    public Livro salvar(Livro livro) {
        // Para cadastrar um livro, o autor deve existir
        Long autorId = livro.getAutor().getId();
        Autor autor = autorService.buscarOurFalhar(autorId);

        livro.setAutor(autor);
        return livroRepository.save(livro);
    }
}
