package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Genero;
import com.ViniGBPl.biblioteca.domain.service.GeneroService; // 1. Importe o novo serviço
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
@RequiredArgsConstructor
public class GeneroController {

    // Injete o Service em vez do Repository para centralizar as regras de negócio
    private final GeneroService generoService;

    @GetMapping
    public List<Genero> listar() {
        //cria um método listar no Service ou usar o repository se o service o expuser
        return generoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Genero adicionar(@RequestBody Genero genero) {
        return generoService.salvar(genero);
    }

    @PutMapping("/{id}")
    public Genero atualizar(@PathVariable Long id, @RequestBody Genero genero) {
        // Chama a lógica de atualização que valida a existência do registro
        return generoService.atualizar(id, genero);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        // Chama a lógica de exclusão que valida integridade
        generoService.excluir(id);
    }
}