package com.ViniGBPl.biblioteca.domain.service;

import com.ViniGBPl.biblioteca.domain.model.Autor;
import com.ViniGBPl.biblioteca.domain.model.Livro;
import com.ViniGBPl.biblioteca.domain.model.Genero;
import com.ViniGBPl.biblioteca.domain.model.LivroDocument;
import com.ViniGBPl.biblioteca.domain.repository.LivroRepository;
import com.ViniGBPl.biblioteca.domain.repository.LivroSearchRepository;
import com.ViniGBPl.biblioteca.domain.repository.GeneroRepository; // Import necessário
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorService autorService;
    private final LivroSearchRepository livroSearchRepository;
    private final GeneroRepository generoRepository; // 1. VOCÊ PRECISA INJETAR O REPOSITÓRIO AQUI

    @Transactional
    public Livro salvar(Livro livro) {
        // 2. Validar Autor
        Long autorId = livro.getAutor().getId();
        Autor autor = autorService.buscarOurFalhar(autorId);
        livro.setAutor(autor);

        // 3. Carregar Gêneros completos (usando a instância injetada 'generoRepository')
        if (livro.getGeneros() != null) {
            List<Genero> generosCompletos = livro.getGeneros().stream()
                    .map(g -> generoRepository.findById(g.getId())
                            .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + g.getId())))
                    .toList();
            livro.setGeneros(generosCompletos);
        }

        // 4. Garantir que o livro comece como disponível se for novo
        if (livro.getId() == null) {
            livro.setDisponivel(true);
        }

        // 5. Salvar no Postgres
        Livro livroSalvo = livroRepository.save(livro);

        // 6. Sincronizar com Elasticsearch (Desnormalização)
        List<String> nomesGeneros = livroSalvo.getGeneros().stream()
                .map(Genero::getNome)
                .toList();

        LivroDocument document = LivroDocument.builder()
                .id(livroSalvo.getId().toString())
                .titulo(livroSalvo.getTitulo())
                .autorNome(autor.getNome())
                .isbn(livroSalvo.getIsbn())
                .nomesGeneros(nomesGeneros)
                .build();

        livroSearchRepository.save(document);

        return livroSalvo;
    }
}