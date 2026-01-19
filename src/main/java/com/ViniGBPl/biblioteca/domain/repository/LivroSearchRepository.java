package com.ViniGBPl.biblioteca.domain.repository;

import com.ViniGBPl.biblioteca.domain.model.LivroDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface LivroSearchRepository extends ElasticsearchRepository<LivroDocument, String> {
    List<LivroDocument> findByTituloContaining(String titulo);
}