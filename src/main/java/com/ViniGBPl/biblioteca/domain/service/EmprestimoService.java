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


    @Transactional
    public String devolverLivro(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        // 1. Verificar se há alguém na fila para este livro
        var fila = filaEsperaRepository.findByLivroIdOrderByDataSolicitacaoAsc(livroId);

        if (!fila.isEmpty()) {
            // Pega a primeira pessoa da fila
            FilaEspera proximoDaFila = fila.get(0);

            // Cria um novo empréstimo para ela
            Emprestimo novoEmprestimo = Emprestimo.builder()
                    .livro(livro)
                    .nomeUsuario(proximoDaFila.getNomeUsuario())
                    .dataEmprestimo(LocalDateTime.now())
                    .build();

            emprestimoRepository.save(novoEmprestimo);

            // Remove a pessoa da fila
            filaEsperaRepository.delete(proximoDaFila);

            return "Livro devolvido e automaticamente emprestado para " + proximoDaFila.getNomeUsuario() + " (fila de espera).";
        }

        // 2. Se não houver fila, o livro volta a ficar disponível
        livro.setDisponivel(true);
        livroRepository.save(livro);

        return "Livro devolvido com sucesso. O status agora é: DISPONÍVEL.";
    }



}