package com.example.cinemacity.HibernateOracle.DAO;

import java.util.List;

public interface DAOInterface<T> {

    T getData(int id);
    List<T> getDataAll();
    boolean addData(T data);
    void deleteData(T data);
    void updateData(T data);
}