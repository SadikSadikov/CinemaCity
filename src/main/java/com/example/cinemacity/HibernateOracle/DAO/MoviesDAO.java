package com.example.cinemacity.HibernateOracle.DAO;

import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;
import com.example.cinemacity.HibernateOracle.Utility.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class MoviesDAO implements DAOInterface<MoviesEntity>{

    @Override
    public MoviesEntity getData(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        MoviesEntity entity = entityManager.find(MoviesEntity.class, id);
        transaction.commit();
        return entity;
    }

    @Override
    public List<MoviesEntity> getDataAll() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<MoviesEntity> entities = entityManager.createQuery("SELECT m FROM MoviesEntity m", MoviesEntity.class).getResultList();
        transaction.commit();
        return entities;
    }

    @Override
    public boolean addData(MoviesEntity data) {
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
    public void deleteData(MoviesEntity data) {

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
    public void updateData(MoviesEntity data) {

        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(data);
        transaction.commit();

    }
}
