package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.util.HibernateUtil;

import java.util.List;

public abstract class GenericDAO<T> {
    private final Class<T> entityClass;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void salvar(T entity) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = HibernateUtil.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void atualizar(T entity) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = HibernateUtil.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void deletar(T entity) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = HibernateUtil.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public T buscarPorId(Long id) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.find(entityClass, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> listarTodos() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.createQuery("from " + entityClass.getName(), entityClass).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 