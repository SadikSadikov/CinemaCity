package com.example.cinemacity.Helpers;

import com.example.cinemacity.HibernateOracle.Model.User;

public class CurrentUser {

    private static User user;

    private CurrentUser(){}

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CurrentUser.user = user;
    }
}