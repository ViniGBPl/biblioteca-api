package com.ViniGBPl.biblioteca.api.controller;

import com.ViniGBPl.biblioteca.AbstractIntegrationTest;
import com.ViniGBPl.biblioteca.domain.model.Genero;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeneroControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCadastrarGeneroComSucesso() {
        Genero genero = Genero.builder().nome("Ficção Científica").build();

        ResponseEntity<Genero> resposta = restTemplate.postForEntity("/generos", genero, Genero.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resposta.getBody().getNome()).isEqualTo("Ficção Científica");
    }
}