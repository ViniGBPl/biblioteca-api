package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
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
    private final LivroRepository livroRepository; //testar

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livro createLivro(@RequestBody Livro livro) {
        return livroService.salvar(livro);
    }

    @GetMapping("/busca")
    public List<LivroDocument> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero) {

        if (titulo != null && genero != null) {
            return livroSearchRepository.findByTituloContainingAndNomesGeneros(titulo, genero);
        } else if (titulo != null) {
            return livroSearchRepository.findByTituloContaining(titulo);
        } else if (genero != null) {
            return livroSearchRepository.findByNomesGeneros(genero);
        }

        return List.of();
    }


    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return livroService.atualizar(id, livro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        livroService.excluir(id);
    }
    //testar
    @PatchMapping("/{id}/disponibilidade")
    public Livro atualizarDisponibilidade(@PathVariable Long id, @RequestBody Boolean disponivel) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        livro.setDisponivel(disponivel);
        return livroRepository.save(livro);
    }
}
