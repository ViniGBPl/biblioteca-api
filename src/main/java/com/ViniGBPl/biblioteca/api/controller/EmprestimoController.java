package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.domain.model.Emprestimo;
import com.ViniGBPl.biblioteca.domain.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Import necessário
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<Object> realizar(@RequestParam Long livroId, @RequestParam String nomeUsuario) {
        Object resultado = emprestimoService.realizarEmprestimo(livroId, nomeUsuario);

        // Verifica se o retorno é um objeto Emprestimo (Sucesso)
        if (resultado instanceof Emprestimo) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/devolucao")
    public ResponseEntity<String> devolver(@RequestParam Long livroId) {
        String mensagem = emprestimoService.devolverLivro(livroId);
        return ResponseEntity.ok(mensagem);
    }

}