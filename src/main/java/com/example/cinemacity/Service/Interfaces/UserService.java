package com.example.cinemacity.Service.Interfaces;

import com.example.cinemacity.HibernateOracle.Model.User;

public interface UserService {

    User login (String username, String password);
    boolean register (String username, String password);
}
