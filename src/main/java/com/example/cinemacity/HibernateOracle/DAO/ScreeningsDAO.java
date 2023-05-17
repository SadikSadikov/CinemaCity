package com.example.cinemacity.HibernateOracle.DAO;

import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;
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

public class ScreeningsDAO implements DAOInterface<ScreeningsEntity> {

    @Override
    public ScreeningsEntity getData(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ScreeningsEntity entity = entityManager.find(ScreeningsEntity.class, id);
        transaction.commit();
        return entity;
    }

    @Override
    public List<ScreeningsEntity> getDataAll() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<ScreeningsEntity> entities = entityManager.createQuery("SELECT s FROM ScreeningsEntity s", ScreeningsEntity.class).getResultList();
        transaction.commit();
        return entities;
    }

    @Override
    public boolean addData(ScreeningsEntity data) {
        EntityTransaction transaction = null;
        try {
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(data);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e);
        }
        return false;
    }

    @Override
    public void deleteData(ScreeningsEntity data) {
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
    public void updateData(ScreeningsEntity data) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(data);
        transaction.commit();
    }


    public List<ScreeningsEntity> getAllScreeningsForMovie(int idMovie, Timestamp date) {
        try {
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

            // Конвертиране на Timestamp в LocalDateTime
            LocalDateTime localDateTime = date.toLocalDateTime();

            // Извличане на деня, месеца и годината от LocalDateTime
            LocalDate localDate = localDateTime.toLocalDate();

            TypedQuery<ScreeningsEntity> query = entityManager.createQuery("SELECT s FROM ScreeningsEntity s JOIN MoviesEntity m ON m.id_movie = s.id_film_screening WHERE m.id_movie = :idMovie AND TRUNC(s.time_screening) = TRUNC(:date)", ScreeningsEntity.class);
            query.setParameter("idMovie", idMovie);
            query.setParameter("date", localDate);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
