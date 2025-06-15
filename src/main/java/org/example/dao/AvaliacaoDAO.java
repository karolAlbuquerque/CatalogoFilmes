package org.example.dao;

import org.example.model.Avaliacao;
import org.example.model.Filme;
import org.example.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AvaliacaoDAO extends GenericDAO<Avaliacao> {
    
    public AvaliacaoDAO() {
        super(Avaliacao.class);
    }

    public List<Avaliacao> buscarPorFilme(Filme filme) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Avaliacao> query = em.createQuery(
                "from Avaliacao a where a.filme = :filme order by a.dataAvaliacao desc",
                Avaliacao.class
            );
            query.setParameter("filme", filme);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public double calcularMediaAvaliacoes(Filme filme) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            TypedQuery<Double> query = em.createQuery(
                "select avg(a.nota) from Avaliacao a where a.filme = :filme",
                Double.class
            );
            query.setParameter("filme", filme);
            Double media = query.getSingleResult();
            return media != null ? media : 0.0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 