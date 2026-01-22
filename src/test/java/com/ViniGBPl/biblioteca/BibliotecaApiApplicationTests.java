package com.ViniGBPl.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BibliotecaApiApplicationTests extends AbstractIntegrationTest { // Adicione a herança aqui

	@Test
	void contextLoads() {
		// Este teste agora passará pois o AbstractIntegrationTest subirá o Postgres e ES
	}

}