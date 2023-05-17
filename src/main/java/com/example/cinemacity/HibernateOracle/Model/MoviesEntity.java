package com.example.cinemacity.HibernateOracle.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Movies")
public class MoviesEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_movie")
    @GeneratedValue(generator = "incrementor")
    @GenericGenerator(name ="incrementor",strategy = "increment")
    private int id_movie;

    @Column(name = "title")
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "date_release")
    private Timestamp date_release;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_background")
    private byte[] image_background;

    @Column(name = "running_time")
    private double running_time;

    public MoviesEntity(String title, String director, Timestamp date_release, byte[] image, byte[] image_background, double running_time) {
        this.title = title;
        this.director = director;
        this.date_release = date_release;
        this.image = image;
        this.image_background = image_background;
        this.running_time = running_time;
    }

    public MoviesEntity() {
    }

    public int getId_movie() {
        return id_movie;
    }

    public void setId_movie(int id_movie) {
        this.id_movie = id_movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Timestamp getDate_release() {
        return date_release;
    }

    public void setDate_release(Timestamp date_release) {
        this.date_release = date_release;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getRunning_time() {
        return running_time;
    }

    public void setRunning_time(double running_time) {
        this.running_time = running_time;
    }

    public byte[] getImage_background() {
        return image_background;
    }

    public void setImage_background(byte[] image_background) {
        this.image_background = image_background;
    }
}
