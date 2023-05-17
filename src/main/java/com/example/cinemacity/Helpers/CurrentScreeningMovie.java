package com.example.cinemacity.Helpers;

import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;
import com.example.cinemacity.HibernateOracle.Model.ScreeningsEntity;

public class CurrentScreeningMovie {
    private static ScreeningsEntity screeningsEntity;

    private CurrentScreeningMovie() {
    }

    public static ScreeningsEntity getScreeningsEntity() {
        return screeningsEntity;
    }

    public static void setScreeningsEntity(ScreeningsEntity screeningsEntity) {
        CurrentScreeningMovie.screeningsEntity = screeningsEntity;
    }
}
