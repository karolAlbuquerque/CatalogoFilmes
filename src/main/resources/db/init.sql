-- Criação do banco de dados
DROP DATABASE IF EXISTS catalogo_filmes;
CREATE DATABASE catalogo_filmes;

-- Criação das tabelas
CREATE TABLE diretores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    biografia TEXT
);

CREATE TABLE generos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(500)
);

CREATE TABLE atores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    biografia TEXT
);

CREATE TABLE filmes (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    sinopse TEXT,
    ano_lancamento INTEGER,
    data_lancamento DATE,
    duracao_minutos INTEGER,
    diretor_id BIGINT REFERENCES diretores(id)
);

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    nota INTEGER NOT NULL,
    comentario TEXT,
    data_avaliacao TIMESTAMP NOT NULL,
    filme_id BIGINT NOT NULL REFERENCES filmes(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id)
);

-- Tabelas de relacionamento N:M
CREATE TABLE filme_ator (
    filme_id BIGINT REFERENCES filmes(id),
    ator_id BIGINT REFERENCES atores(id),
    PRIMARY KEY (filme_id, ator_id)
);

CREATE TABLE filme_genero (
    filme_id BIGINT REFERENCES filmes(id),
    genero_id BIGINT REFERENCES generos(id),
    PRIMARY KEY (filme_id, genero_id)
); 