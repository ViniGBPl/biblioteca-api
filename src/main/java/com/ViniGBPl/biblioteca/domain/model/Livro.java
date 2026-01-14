package com.ViniGBPl.biblioteca.domain.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(unique = true)
    private String isbn;

    private Integer anoPublicacao;

    @ManyToOne
    @JoinColumn(name ="auto_id" , nullable = false)
    private Autor autor;

    //Campo para controlar a disponibilidade
    @Builder.Default
    private Boolean disponivel = true;

}
