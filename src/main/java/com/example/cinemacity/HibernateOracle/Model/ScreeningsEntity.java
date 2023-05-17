package com.example.cinemacity.HibernateOracle.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Screenings")
public class ScreeningsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_screening")
    @GeneratedValue(generator = "incrementor")
    @GenericGenerator(name ="incrementor",strategy = "increment")
    private int id_screening;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_film_screening")
    private MoviesEntity id_film_screening;

    @Column(name = "time_screening")
    private Timestamp time_screening;

    @Column(name = "number_screening")
    private String number_screening;

    @Column(name = "max_row")
    private int max_row;

    @Column(name = "max_column")
    private int max_column;


    public ScreeningsEntity(MoviesEntity id_film_screening, Timestamp time_screening, String number_screening, int max_row, int max_column) {
        this.id_film_screening = id_film_screening;
        this.time_screening = time_screening;
        this.number_screening = number_screening;
        this.max_row = max_row;
        this.max_column = max_column;
    }

    public ScreeningsEntity() {
    }

    public int getId_screening() {
        return id_screening;
    }

    public void setId_screening(int id_screening) {
        this.id_screening = id_screening;
    }

    public MoviesEntity getId_film_screening() {
        return id_film_screening;
    }

    public void setId_film_screening(MoviesEntity id_film_screening) {
        this.id_film_screening = id_film_screening;
    }

    public Timestamp getTime_screening() {
        return time_screening;
    }

    public void setTime_screening(Timestamp time_screening) {
        this.time_screening = time_screening;
    }

    public String getNumber_screening() {
        return number_screening;
    }

    public void setNumber_screening(String number_screening) {
        this.number_screening = number_screening;
    }

    public int getMax_row() {
        return max_row;
    }

    public void setMax_row(int max_row) {
        this.max_row = max_row;
    }

    public int getMax_column() {
        return max_column;
    }

    public void setMax_column(int max_column) {
        this.max_column = max_column;
    }
}
