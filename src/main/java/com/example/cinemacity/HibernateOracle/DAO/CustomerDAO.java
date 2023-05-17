package com.example.cinemacity.HibernateOracle.DAO;

import com.example.cinemacity.HibernateOracle.Model.CustomerEntity;
import com.example.cinemacity.HibernateOracle.Utility.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerDAO implements DAOInterface<CustomerEntity>{

    @Override
    public CustomerEntity getData(int id) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        CustomerEntity entity = entityManager.find(CustomerEntity.class, id);
        transaction.commit();
        return entity;
    }

    @Override
    public List<CustomerEntity> getDataAll() {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<CustomerEntity> entities = entityManager.createQuery("SELECT c FROM CustomerEntity c", CustomerEntity.class).getResultList();
        transaction.commit();
        return entities;
    }

    @Override
    public boolean addData(CustomerEntity data) {
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
    public void deleteData(CustomerEntity data) {
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
    public void updateData(CustomerEntity data) {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(data);
        transaction.commit();
    }

    public CustomerEntity getConnectedEmployee(String username, String password){
        try{
            EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
            TypedQuery<CustomerEntity> query = entityManager.createQuery("SELECT c FROM CustomerEntity c WHERE c.username = :username AND c.password = :password", CustomerEntity.class );
            query.setParameter("username",username);
            query.setParameter("password",password);
            return query.getSingleResult();
        }
        catch (Exception e){
            System.err.println("Customer not found");
            return null;
        }
    }
}
