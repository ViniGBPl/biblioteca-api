package com.ViniGBPl.biblioteca.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class LivroDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "portuguese")
    private String titulo;

    @Field(type = FieldType.Keyword)
    private String autorNome;

    @Field(type = FieldType.Keyword)
    private String isbn;
}