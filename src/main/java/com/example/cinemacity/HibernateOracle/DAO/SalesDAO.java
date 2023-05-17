package com.example.cinemacity.HibernateOracle.DAO;

import com.example.cinemacity.HibernateOracle.Model.SalesEntity;
import com.example.cinemacity.HibernateOracle.Model.ScreeningsEntity;
import com.example.cinemacity.HibernateOracle.Utility.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO implements DAOInterface<SalesEntity>{
    @Override
    public SalesEntity getData(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        SalesEntity entity = entityManager.find(SalesEntity.class, id);
        transaction.commit();
        return entity;
    }

    @Override
    public List<SalesEntity> getDataAll() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<SalesEntity> entities = entityManager.createQuery("SELECT s FROM SalesEntity s", SalesEntity.class).getResultList();
        transaction.commit();
        return entities;
    }

    @Override
    public boolean addData(SalesEntity data) {

        EntityTransaction transaction = null;
        try{
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(data);
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
    public void deleteData(SalesEntity data) {
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
    public void updateData(SalesEntity data) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(data);
        transaction.commit();
    }

    public List<SalesEntity> getAllSoldSeatsForScreening(int idScreening, int idCustomer) {
        try {
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            TypedQuery<SalesEntity> query = entityManager.createQuery("SELECT s FROM SalesEntity s JOIN ScreeningsEntity ss ON s.id_screening_sale = ss.id_screening JOIN CustomerEntity c ON s.id_customer_sale = c.id_customer WHERE ss.id_screening = :idScreening AND c.id_customer = :idCustomer", SalesEntity.class);
            query.setParameter("idScreening", idScreening);
            query.setParameter("idCustomer", idCustomer);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Integer> mostViewedMovies() {
        try {
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            TypedQuery<Integer> query = entityManager.createQuery("SELECT m.id_movie FROM SalesEntity s JOIN ScreeningsEntity ss ON s.id_screening_sale = ss.id_screening JOIN MoviesEntity m ON ss.id_film_screening = m.id_movie GROUP BY m.id_movie ORDER BY COUNT(*) DESC", Integer.class).setMaxResults(8);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
