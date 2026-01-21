package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.AbstractIntegrationTest;
import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.repository.AutorRepository;
import com.ViniGBPl.biblioteca.domain.repository.FilaEsperaRepository;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // Garante que cada teste rode em uma transação que sofre rollback ao final
public class EmprestimoServiceIT extends AbstractIntegrationTest {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private FilaEsperaRepository filaEsperaRepository;

    private Livro livro;

    @BeforeEach
    void setUp() {

        filaEsperaRepository.deleteAll();
        livroRepository.deleteAll();
        autorRepository.deleteAll();

        Autor autor = Autor.builder()
                .nome("Autor Teste")
                .nacionalidade("Brasileira")
                .build();
        autor = autorRepository.save(autor);

        livro = Livro.builder()
                .titulo("Livro de Teste")
                .isbn("123456")
                .autor(autor)
                .disponivel(true)
                .build();
        livro = livroRepository.save(livro);
    }

    @Test
    void deveAdicionarUsuarioAFilaQuandoLivroIndisponivel() {
        // Cenário: Tornar livro indisponível manualmente
        livro.setDisponivel(false);
        livroRepository.save(livro);

        // Ação
        Object resultado = emprestimoService.realizarEmprestimo(livro.getId(), "Usuario Fila");

        // Validação
        assertThat(resultado).isInstanceOf(String.class);
        assertThat(resultado.toString()).contains("adicionado à fila de espera");
        assertThat(filaEsperaRepository.findAll()).hasSize(1);
    }

    @Test
    void deveEmprestarAutomaticamenteParaProximoDaFilaAoDevolver() {
        // 1. Cenário: Livro já está emprestado (indisponível)
        livro.setDisponivel(false);
        livroRepository.save(livro);

        // 2. Ação: Usuario 2 entra na fila
        emprestimoService.realizarEmprestimo(livro.getId(), "Usuario 2");

        // 3. Ação: Alguém devolve o livro, o que deve disparar o empréstimo para quem está na fila
        String mensagem = emprestimoService.devolverLivro(livro.getId());

        // 4. Validações
        assertThat(mensagem).contains("automaticamente emprestado para Usuario 2");

        Livro livroPosDevolucao = livroRepository.findById(livro.getId()).orElseThrow();
        // O livro deve continuar indisponível porque agora está com o Usuario 2
        assertThat(livroPosDevolucao.getDisponivel()).isFalse();
        // A fila deve estar vazia pois o usuário saiu da fila e virou um empréstimo
        assertThat(filaEsperaRepository.findAll()).isEmpty();
    }
}