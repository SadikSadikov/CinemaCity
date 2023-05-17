package com.example.cinemacity.Helpers;

import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;

import java.util.HashMap;
import java.util.Map;

public class MovieSession {
    private static Map<String, MoviesEntity> sessions = new HashMap<>();

    public static void setMovieSession(String nameMovie, MoviesEntity movie) {
        sessions.put(nameMovie, movie);
    }

    public static MoviesEntity getMovieSession(String nameMovie) {
        return sessions.get(nameMovie);
    }

}
