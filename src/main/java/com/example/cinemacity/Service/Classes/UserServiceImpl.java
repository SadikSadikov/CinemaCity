package com.example.cinemacity.Service.Classes;

import com.example.cinemacity.HibernateOracle.DAO.CustomerDAO;
import com.example.cinemacity.HibernateOracle.Model.CustomerEntity;
import com.example.cinemacity.HibernateOracle.Model.User;
import com.example.cinemacity.Service.Interfaces.UserService;

public class UserServiceImpl implements UserService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    public User login(String username, String password) {

        User user = customerDAO.getConnectedEmployee(username, password);

        if (user != null && user.getPassword().equals(password)){
            return user;
        }
        else {
            System.out.println("Invalid username/password");
        }
        return null;
    }

    @Override
    public boolean register(String username, String password) {

        User user = customerDAO.getConnectedEmployee(username, password);
        if (user == null || !user.getPassword().equals(password)){
            return customerDAO.addData(new CustomerEntity(username, username, password, ""));
        }

        return false;
    }


}
