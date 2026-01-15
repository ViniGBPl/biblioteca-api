package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor

public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livro createLivro(@RequestBody Livro livro) {
        return livroService.salvar(livro);
    }
}
