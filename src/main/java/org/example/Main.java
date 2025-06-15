package org.example;

import org.example.dao.*;
import org.example.model.*;
import org.example.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final FilmeDAO filmeDAO = new FilmeDAO();
    private static final AtorDAO atorDAO = new AtorDAO();
    private static final DiretorDAO diretorDAO = new DiretorDAO();
    private static final GeneroDAO generoDAO = new GeneroDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1 -> cadastrarFilme(scanner);
                case 2 -> cadastrarAtor();
                case 3 -> cadastrarDiretor();
                case 4 -> cadastrarGenero();
                case 5 -> cadastrarUsuario();
                case 6 -> registrarAvaliacao();
                case 7 -> listarFilmes();
                case 8 -> listarAtores();
                case 9 -> listarDiretores();
                case 10 -> listarGeneros();
                case 11 -> listarUsuarios();
                case 12 -> listarAvaliacoes();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
        HibernateUtil.shutdown();
    }

    private static void exibirMenu() {
        System.out.println("\n=== CATÁLOGO DE FILMES ===");
        System.out.println("1. Cadastrar Filme");
        System.out.println("2. Cadastrar Ator");
        System.out.println("3. Cadastrar Diretor");
        System.out.println("4. Cadastrar Gênero");
        System.out.println("5. Cadastrar Usuário");
        System.out.println("6. Registrar Avaliação");
        System.out.println("7. Listar Filmes");
        System.out.println("8. Listar Atores");
        System.out.println("9. Listar Diretores");
        System.out.println("10. Listar Gêneros");
        System.out.println("11. Listar Usuários");
        System.out.println("12. Listar Avaliações");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarFilme(Scanner scanner) {
        System.out.println("\n=== CADASTRAR FILME ===");
        
        // Coletar informações básicas
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Sinopse: ");
        String sinopse = scanner.nextLine();
        
        System.out.print("Ano de Lançamento: ");
        int anoLancamento = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Duração (minutos): ");
        int duracao = Integer.parseInt(scanner.nextLine());
        
        System.out.print("ID do Diretor: ");
        Long diretorId = Long.parseLong(scanner.nextLine());
        
        // Listar e selecionar gêneros
        System.out.println("\n📋 Gêneros disponíveis:");
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Buscar diretor
            Diretor diretor = em.find(Diretor.class, diretorId);
            if (diretor == null) {
                System.out.println("\n❌ Diretor não encontrado!");
                return;
            }
            
            // Listar gêneros
            List<Genero> generosDisponiveis = em.createQuery("SELECT g FROM Genero g ORDER BY g.nome", Genero.class)
                                               .getResultList();
            for (Genero g : generosDisponiveis) {
                System.out.printf("%d - %s%n", g.getId(), g.getNome());
            }
            
            // Selecionar gêneros
            Set<Genero> generosSelecionados = new HashSet<>();
            System.out.print("\nDigite os IDs dos gêneros separados por vírgula (ex: 1,2,3): ");
            String[] generoIds = scanner.nextLine().split(",");
            
            for (String id : generoIds) {
                try {
                    Long generoId = Long.parseLong(id.trim());
                    Genero genero = em.find(Genero.class, generoId);
                    if (genero != null) {
                        generosSelecionados.add(genero);
                    } else {
                        System.out.println("❌ Gênero ID " + generoId + " não encontrado!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ ID inválido: " + id);
                }
            }
            
            if (generosSelecionados.isEmpty()) {
                System.out.println("\n❌ É necessário selecionar pelo menos um gênero!");
                em.getTransaction().rollback();
                return;
            }
            
            // Criar e salvar filme
            Filme filme = new Filme();
            filme.setTitulo(titulo);
            filme.setSinopse(sinopse);
            filme.setAnoLancamento(anoLancamento);
            filme.setDuracaoMinutos(duracao);
            filme.setDiretor(diretor);
            filme.setGeneros(generosSelecionados);
            
            em.persist(filme);
            em.getTransaction().commit();
            System.out.println("\n✅ Filme cadastrado com sucesso!");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("\n❌ Erro ao cadastrar filme: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void cadastrarAtor() {
        System.out.println("\n=== CADASTRAR ATOR ===");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        String dataNascimentoStr = scanner.nextLine();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr);
        
        System.out.print("Biografia: ");
        String biografia = scanner.nextLine();
        
        Ator ator = new Ator();
        ator.setNome(nome);
        ator.setDataNascimento(dataNascimento);
        ator.setBiografia(biografia);
        
        atorDAO.salvar(ator);
        System.out.println("Ator cadastrado com sucesso!");
    }

    private static void cadastrarDiretor() {
        System.out.println("\n=== CADASTRAR DIRETOR ===");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Data de Nascimento (YYYY-MM-DD): ");
        String dataNascimentoStr = scanner.nextLine();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr);
        
        System.out.print("Biografia: ");
        String biografia = scanner.nextLine();
        
        Diretor diretor = new Diretor();
        diretor.setNome(nome);
        diretor.setDataNascimento(dataNascimento);
        diretor.setBiografia(biografia);
        
        diretorDAO.salvar(diretor);
        System.out.println("Diretor cadastrado com sucesso!");
    }

    private static void cadastrarGenero() {
        System.out.println("\n=== CADASTRAR GÊNERO ===");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        Genero genero = new Genero();
        genero.setNome(nome);
        genero.setDescricao(descricao);
        
        generoDAO.salvar(genero);
        System.out.println("Gênero cadastrado com sucesso!");
    }

    private static void cadastrarUsuario() {
        System.out.println("\n=== CADASTRAR USUÁRIO ===");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        
        usuarioDAO.salvar(usuario);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void registrarAvaliacao() {
        System.out.println("\n=== REGISTRAR AVALIAÇÃO ===");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("ID do Filme: ");
        Long filmeId = scanner.nextLong();
        scanner.nextLine();
        
        System.out.print("ID do Usuário: ");
        Long usuarioId = scanner.nextLong();
        scanner.nextLine();
        
        System.out.print("Nota (1-5): ");
        int nota = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Comentário: ");
        String comentario = scanner.nextLine();
        
        Filme filme = filmeDAO.buscarPorId(filmeId);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        
        if (filme != null && usuario != null) {
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setFilme(filme);
            avaliacao.setUsuario(usuario);
            avaliacao.setNota(nota);
            avaliacao.setComentario(comentario);
            avaliacao.setDataAvaliacao(LocalDateTime.now());
            
            avaliacaoDAO.salvar(avaliacao);
            System.out.println("Avaliação registrada com sucesso!");
        } else {
            System.out.println("Filme ou usuário não encontrado!");
        }
    }

    private static void listarFilmes() {
        System.out.println("\n=== FILMES CADASTRADOS ===");
        filmeDAO.listarTodosComRelacionamentos().forEach(filme -> {
            System.out.println("ID: " + filme.getId());
            System.out.println("Título: " + filme.getTitulo());
            System.out.println("Diretor: " + filme.getDiretor().getNome());
            System.out.println("Ano: " + filme.getAnoLancamento());
            System.out.println("Duração: " + filme.getDuracaoMinutos() + " minutos");
            System.out.println("Gêneros: " + filme.getGeneros().stream()
                    .map(Genero::getNome)
                    .collect(Collectors.joining(", ")));
            System.out.println("------------------------");
        });
    }

    private static void listarAtores() {
        System.out.println("\n=== ATORES CADASTRADOS ===");
        atorDAO.listarTodos().forEach(ator -> {
            System.out.println("ID: " + ator.getId());
            System.out.println("Nome: " + ator.getNome());
            System.out.println("Data de Nascimento: " + ator.getDataNascimento());
            System.out.println("------------------------");
        });
    }

    private static void listarDiretores() {
        System.out.println("\n=== DIRETORES CADASTRADOS ===");
        diretorDAO.listarTodos().forEach(diretor -> {
            System.out.println("ID: " + diretor.getId());
            System.out.println("Nome: " + diretor.getNome());
            System.out.println("Data de Nascimento: " + diretor.getDataNascimento());
            System.out.println("------------------------");
        });
    }

    private static void listarGeneros() {
        System.out.println("\n=== GÊNEROS CADASTRADOS ===");
        generoDAO.listarTodos().forEach(genero -> {
            System.out.println("ID: " + genero.getId());
            System.out.println("Nome: " + genero.getNome());
            System.out.println("Descrição: " + genero.getDescricao());
            System.out.println("------------------------");
        });
    }

    private static void listarUsuarios() {
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        usuarioDAO.listarTodos().forEach(usuario -> {
            System.out.println("ID: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("------------------------");
        });
    }

    private static void listarAvaliacoes() {
        System.out.println("\n=== AVALIAÇÕES REGISTRADAS ===");
        avaliacaoDAO.listarTodos().forEach(avaliacao -> {
            System.out.println("ID: " + avaliacao.getId());
            System.out.println("Filme: " + avaliacao.getFilme().getTitulo());
            System.out.println("Usuário: " + avaliacao.getUsuario().getNome());
            System.out.println("Nota: " + avaliacao.getNota());
            System.out.println("Comentário: " + avaliacao.getComentario());
            System.out.println("Data: " + avaliacao.getDataAvaliacao());
            System.out.println("------------------------");
        });
    }
}