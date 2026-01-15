package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor

public class AutorController {

    private final AutorService autorService;

    @GetMapping
    public List<Autor> listar(){
        return autorService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Autor adicionar(@RequestBody Autor autor){
        return autorService.save(autor);
    }

}
