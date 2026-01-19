package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Emprestimo;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.repository.EmprestimoRepository;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import com.ViniGBPl.biblioteca.domain.model.FilaEspera;
import com.ViniGBPl.biblioteca.domain.repository.FilaEsperaRepository;


@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final FilaEsperaRepository filaEsperaRepository; // Nova dependência

    @Transactional
    public Object realizarEmprestimo(Long livroId, String nomeUsuario) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (!livro.getDisponivel()) {
            // Lógica da Fila de Espera
            FilaEspera fila = FilaEspera.builder()
                    .livro(livro)
                    .nomeUsuario(nomeUsuario)
                    .dataSolicitacao(LocalDateTime.now())
                    .build();

            filaEsperaRepository.save(fila);
            return "Livro indisponível. " + nomeUsuario + " foi adicionado à fila de espera.";
        }

        livro.setDisponivel(false);
        livroRepository.save(livro);

        Emprestimo emprestimo = Emprestimo.builder()
                .livro(livro)
                .nomeUsuario(nomeUsuario)
                .dataEmprestimo(LocalDateTime.now())
                .build();

        return emprestimoRepository.save(emprestimo);
    }
}