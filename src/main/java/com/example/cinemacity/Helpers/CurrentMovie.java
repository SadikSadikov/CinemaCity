package com.example.cinemacity.Helpers;

import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;

public class CurrentMovie {
    private static MoviesEntity moviesEntity;

    private CurrentMovie() {
    }

    public static MoviesEntity getMoviesEntity() {
        return moviesEntity;
    }

    public static void setMoviesEntity(MoviesEntity moviesEntity) {
        CurrentMovie.moviesEntity = moviesEntity;
    }
}
