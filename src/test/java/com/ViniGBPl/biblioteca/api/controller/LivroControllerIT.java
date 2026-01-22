package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.AbstractIntegrationTest;
import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.model.Genero;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.model.LivroDocument;
import com.ViniGBPl.biblioteca.domain.repository.AutorRepository;
import com.ViniGBPl.biblioteca.domain.repository.GeneroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LivroControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Test
    void deveSalvarLivroESincronizarComElasticsearch() throws InterruptedException {
        // Preparação
        Autor autor = autorRepository.save(Autor.builder().nome("J.K. Rowling").build());
        Genero genero = generoRepository.save(Genero.builder().nome("Magia").build());

        Livro novoLivro = Livro.builder()
                .titulo("Harry Potter")
                .isbn("987654")
                .autor(autor)
                .generos(List.of(genero))
                .build();

        // Execução: Cadastro
        ResponseEntity<Livro> respostaCadastro = restTemplate.postForEntity("/livros", novoLivro, Livro.class);
        assertThat(respostaCadastro.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Pequena pausa para garantir a indexação no Elasticsearch (opcional)
        Thread.sleep(1000);

        // Execução: Busca no Elasticsearch
        ResponseEntity<LivroDocument[]> respostaBusca = restTemplate.getForEntity("/livros/busca?titulo=Harry", LivroDocument[].class);

        // Validação
        assertThat(respostaBusca.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respostaBusca.getBody()).isNotEmpty();
        assertThat(respostaBusca.getBody()[0].getTitulo()).contains("Harry Potter");
        assertThat(respostaBusca.getBody()[0].getNomesGeneros()).contains("Magia");
    }
}