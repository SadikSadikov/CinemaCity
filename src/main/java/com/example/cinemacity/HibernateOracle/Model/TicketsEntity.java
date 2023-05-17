package com.example.cinemacity.HibernateOracle.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tickets")
public class TicketsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_ticket")
    @GeneratedValue(generator = "incrementor")
    @GenericGenerator(name ="incrementor",strategy = "increment")
    private int id_ticket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_screening_ticket")
    private ScreeningsEntity id_screening_ticket;

    @Column(name = "row_number")
    private int row_number;

    @Column(name = "seat_number")
    private int seat_number;

    @Column(name = "price_ticket")
    private double price_ticket;

    @Column(name = "count_ticket")
    private int count_ticket;


    public TicketsEntity(ScreeningsEntity id_screening_ticket, int row_number, int seat_number, double price_ticket, int count_ticket) {
        this.id_screening_ticket = id_screening_ticket;
        this.row_number = row_number;
        this.seat_number = seat_number;
        this.price_ticket = price_ticket;
        this.count_ticket = count_ticket;
    }

    public TicketsEntity() {
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public ScreeningsEntity getId_screening_ticket() {
        return id_screening_ticket;
    }

    public void setId_screening_ticket(ScreeningsEntity id_screening_ticket) {
        this.id_screening_ticket = id_screening_ticket;
    }

    public int getRow_number() {
        return row_number;
    }

    public void setRow_number(int row_number) {
        this.row_number = row_number;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public double getPrice_ticket() {
        return price_ticket;
    }

    public void setPrice_ticket(double price_ticket) {
        this.price_ticket = price_ticket;
    }

    public int getCount_ticket() {
        return count_ticket;
    }

    public void setCount_ticket(int count_ticket) {
        this.count_ticket = count_ticket;
    }
}
