package com.example.cinemacity.HibernateOracle.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Sales")
public class SalesEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_sale")
    @GeneratedValue(generator = "incrementor")
    @GenericGenerator(name ="incrementor",strategy = "increment")
    private int id_sale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_screening_sale")
    private ScreeningsEntity id_screening_sale;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_customer_sale")
    private CustomerEntity id_customer_sale;

    @Column(name = "row_number")
    private int row_number;

    @Column(name = "seat_number")
    private int seat_number;

    @Column(name = "sale_time")
    private Timestamp sale_time;

    @Column(name = "sale_price")
    private double sale_price;

    public SalesEntity(ScreeningsEntity id_screening_sale, CustomerEntity id_customer_sale, int row_number, int seat_number, Timestamp sale_time, double sale_price) {
        this.id_screening_sale = id_screening_sale;
        this.id_customer_sale = id_customer_sale;
        this.row_number = row_number;
        this.seat_number = seat_number;
        this.sale_time = sale_time;
        this.sale_price = sale_price;
    }

    public SalesEntity() {
    }

    public int getId_sale() {
        return id_sale;
    }

    public void setId_sale(int id_sale) {
        this.id_sale = id_sale;
    }

    public CustomerEntity getId_customer_sale() {
        return id_customer_sale;
    }

    public void setId_customer_sale(CustomerEntity id_customer_sale) {
        this.id_customer_sale = id_customer_sale;
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

    public Timestamp getSale_time() {
        return sale_time;
    }

    public void setSale_time(Timestamp sale_time) {
        this.sale_time = sale_time;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public ScreeningsEntity getId_screening_sale() {
        return id_screening_sale;
    }

    public void setId_screening_sale(ScreeningsEntity id_screening_sale) {
        this.id_screening_sale = id_screening_sale;
    }
}
