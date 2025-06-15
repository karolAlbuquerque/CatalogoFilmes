package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    // Relacionamento 1:N com Avaliacao
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Avaliacao> avaliacoes = new HashSet<>();
} 