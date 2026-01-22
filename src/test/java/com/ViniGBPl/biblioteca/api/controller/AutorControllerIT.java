package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.AbstractIntegrationTest;
import com.ViniGBPl.biblioteca.domain.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutorControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCadastrarAutorComSucesso() {
        Autor autor = Autor.builder()
                .nome("Machado de Assis")
                .nacionalidade("Brasileiro")
                .build();

        ResponseEntity<Autor> resposta = restTemplate.postForEntity("/autores", autor, Autor.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resposta.getBody().getId()).isNotNull();
        assertThat(resposta.getBody().getNome()).isEqualTo("Machado de Assis");
    }
}