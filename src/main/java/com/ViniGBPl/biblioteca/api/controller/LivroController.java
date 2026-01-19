package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ViniGBPl.biblioteca.domain.model.LivroDocument;
import com.ViniGBPl.biblioteca.domain.repository.LivroSearchRepository;
import java.util.List;


@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor

public class LivroController {

    private final LivroSearchRepository livroSearchRepository;
    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livro createLivro(@RequestBody Livro livro) {
        return livroService.salvar(livro);
    }

    @GetMapping("/busca")
    public List<LivroDocument> buscar(@RequestParam String titulo) {
        return livroSearchRepository.findByTituloContaining(titulo);
    }
}
