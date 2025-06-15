package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "diretores")
public class Diretor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 1000)
    private String biografia;

    // Relacionamento 1:N com Filme
    @OneToMany(mappedBy = "diretor")
    private Set<Filme> filmes = new HashSet<>();
} 