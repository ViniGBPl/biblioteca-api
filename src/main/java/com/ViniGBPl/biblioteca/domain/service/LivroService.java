package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ViniGBPl.biblioteca.domain.model.LivroDocument;
import com.ViniGBPl.biblioteca.domain.repository.LivroSearchRepository;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorService autorService;
    private final LivroSearchRepository livroSearchRepository; // Adicione esta linha

    @Transactional
    public Livro salvar(Livro livro) {
        Long autorId = livro.getAutor().getId();
        Autor autor = autorService.buscarOurFalhar(autorId);
        livro.setAutor(autor);

        Livro livroSalvo = livroRepository.save(livro);

        // Sincroniza com Elasticsearch
        LivroDocument document = LivroDocument.builder()
                .id(livroSalvo.getId().toString())
                .titulo(livroSalvo.getTitulo())
                .autorNome(autor.getNome())
                .isbn(livroSalvo.getIsbn())
                .build();

        livroSearchRepository.save(document);

        return livroSalvo;
    }
}
