package org.example.dao;

import org.example.model.Filme;
import org.example.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FilmeDAO extends GenericDAO<Filme> {
    
    public FilmeDAO() {
        super(Filme.class);
    }

    public List<Filme> buscarPorTitulo(String titulo) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Filme> query = em.createQuery(
                "from Filme f where lower(f.titulo) like lower(:titulo)",
                Filme.class
            );
            query.setParameter("titulo", "%" + titulo + "%");
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Filme> buscarPorGenero(String genero) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Filme> query = em.createQuery(
                "select f from Filme f join f.generos g where lower(g.nome) like lower(:genero)",
                Filme.class
            );
            query.setParameter("genero", "%" + genero + "%");
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Filme> buscarPorAtor(String nomeAtor) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Filme> query = em.createQuery(
                "select f from Filme f join f.atores a where lower(a.nome) like lower(:nomeAtor)",
                Filme.class
            );
            query.setParameter("nomeAtor", "%" + nomeAtor + "%");
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Filme> buscarPorDiretor(String nomeDiretor) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Filme> query = em.createQuery(
                "select f from Filme f join f.diretor d where lower(d.nome) like lower(:nomeDiretor)",
                Filme.class
            );
            query.setParameter("nomeDiretor", "%" + nomeDiretor + "%");
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Filme> listarTodosComRelacionamentos() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Filme> query = em.createQuery(
                "select distinct f from Filme f " +
                "left join fetch f.diretor " +
                "left join fetch f.generos " +
                "left join fetch f.atores " +
                "left join fetch f.avaliacoes",
                Filme.class
            );
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 