package com.example.cinemacity.HibernateOracle.DAO;

import com.example.cinemacity.HibernateOracle.Model.TicketsEntity;
import com.example.cinemacity.HibernateOracle.Utility.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class TicketsDAO implements DAOInterface<TicketsEntity>{
    @Override
    public TicketsEntity getData(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        TicketsEntity entity = entityManager.find(TicketsEntity.class, id);
        transaction.commit();
        return entity;
    }

    @Override
    public List<TicketsEntity> getDataAll() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<TicketsEntity> entities = entityManager.createQuery("SELECT t FROM TicketsEntity t", TicketsEntity.class).getResultList();
        transaction.commit();
        return entities;
    }

    @Override
    public boolean addData(TicketsEntity data) {
        EntityTransaction transaction = null;
        try{
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(data);
            transaction.commit();
            return true;
        }
        catch (Exception e) {
            if(transaction.isActive()){
                transaction.rollback();
            }
            System.out.println(e);
        }
        return false;
    }

    @Override
    public void deleteData(TicketsEntity data) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.merge(data));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(TicketsEntity data) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(data);
        transaction.commit();
    }
}
