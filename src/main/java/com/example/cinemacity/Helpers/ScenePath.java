package com.example.cinemacity.Helpers;

public enum ScenePath {
    LOGIN("/com/example/cinemacity/login-register.fxml"),
    MAIN("/com/example/cinemacity/main.fxml"),
    MOVIE_MAIN("/com/example/cinemacity/movie-main.fxml");

    private final String path;

    private ScenePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}