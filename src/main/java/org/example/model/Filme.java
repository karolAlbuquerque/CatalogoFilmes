package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "filmes")
public class Filme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String sinopse;

    @Column(name = "ano_lancamento")
    private Integer anoLancamento;

    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    // Relacionamento 1:N com Avaliacao (Composição)
    @OneToMany(mappedBy = "filme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Avaliacao> avaliacoes = new HashSet<>();

    // Relacionamento N:M com Ator (Agregação)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
        name = "filme_ator",
        joinColumns = @JoinColumn(name = "filme_id"),
        inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    private Set<Ator> atores = new HashSet<>();

    // Relacionamento N:M com Genero
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
        name = "filme_genero",
        joinColumns = @JoinColumn(name = "filme_id"),
        inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    // Relacionamento N:1 com Diretor
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "diretor_id")
    private Diretor diretor;
} 