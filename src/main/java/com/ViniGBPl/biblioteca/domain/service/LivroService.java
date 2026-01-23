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
    private final GeneroRepository generoRepository; // INJETAR O REPOSITÓRIO

    @Transactional
    public Livro salvar(Livro livro) {
        //  Validar Autor
        Long autorId = livro.getAutor().getId();
        Autor autor = autorService.buscarOurFalhar(autorId);
        livro.setAutor(autor);

        // Carregar Gêneros completos (usando a instância injetada 'generoRepository')
        if (livro.getGeneros() != null) {
            List<Genero> generosCompletos = livro.getGeneros().stream()
                    .map(g -> generoRepository.findById(g.getId())
                            .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + g.getId())))
                    .toList();
            livro.setGeneros(generosCompletos);
        }

        // Garantir que o livro comece como disponível se for novo
        if (livro.getId() == null) {
            livro.setDisponivel(true);
        }

        //Salvar no Postgres
        Livro livroSalvo = livroRepository.save(livro);

        //  Sincronizar com Elasticsearch (Desnormalização)
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

    // atualizar livro
    @Transactional
    public Livro atualizar(Long livroId, Livro livroAtualizado) {
        Livro livroExistente = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        // Atualiza apenas os campos permitidos
        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setIsbn(livroAtualizado.getIsbn());
        livroExistente.setAnoPublicacao(livroAtualizado.getAnoPublicacao());

        // Se o autor mudou, valida novamente
        if (!livroExistente.getAutor().getId().equals(livroAtualizado.getAutor().getId())) {
            livroExistente.setAutor(autorService.buscarOurFalhar(livroAtualizado.getAutor().getId()));
        }

        Livro livroSalvo = livroRepository.save(livroExistente);

        // Sincroniza a atualização no Elasticsearch
        syncElasticsearch(livroSalvo);

        return livroSalvo;
    }
    // Deletar livro
    @Transactional
    public void excluir(Long livroId) {
        if (!livroRepository.existsById(livroId)) {
            throw new RuntimeException("Livro não encontrado");
        }

        // Remove do Postgres
        livroRepository.deleteById(livroId);

        // Remove do Elasticsearch
        livroSearchRepository.deleteById(livroId.toString());
    }

    // Método auxiliar para evitar duplicação de código na sincronização
    private void syncElasticsearch(Livro livro) {
        List<String> nomesGeneros = livro.getGeneros().stream()
                .map(Genero::getNome)
                .toList();

        LivroDocument document = LivroDocument.builder()
                .id(livro.getId().toString())
                .titulo(livro.getTitulo())
                .autorNome(livro.getAutor().getNome())
                .isbn(livro.getIsbn())
                .nomesGeneros(nomesGeneros)
                .build();

        livroSearchRepository.save(document);
    }


}