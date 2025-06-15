package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "atores")
public class Ator {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 1000)
    private String biografia;

    // Relacionamento N:M com Filme (Agregação)
    @ManyToMany(mappedBy = "atores")
    private Set<Filme> filmes = new HashSet<>();
} 