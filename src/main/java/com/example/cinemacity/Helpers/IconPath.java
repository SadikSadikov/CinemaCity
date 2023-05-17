package com.example.cinemacity.Helpers;

public enum IconPath {
    MAIN("file:///C:\\Users\\USER\\IdeaProjects\\CinemaCity\\Image\\icon-monkey-circle.png");

    private final String path;

    private IconPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
