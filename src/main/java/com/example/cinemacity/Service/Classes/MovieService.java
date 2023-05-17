package com.example.cinemacity.Service.Classes;

import com.example.cinemacity.HibernateOracle.DAO.MoviesDAO;
import com.example.cinemacity.HibernateOracle.DAO.SalesDAO;
import com.example.cinemacity.HibernateOracle.DAO.ScreeningsDAO;
import com.example.cinemacity.HibernateOracle.DAO.TicketsDAO;
import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;
import com.example.cinemacity.HibernateOracle.Model.SalesEntity;
import com.example.cinemacity.HibernateOracle.Model.ScreeningsEntity;
import com.example.cinemacity.HibernateOracle.Model.TicketsEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private final MoviesDAO moviesDAO = new MoviesDAO();
    private final ScreeningsDAO screeningsDAO = new ScreeningsDAO();
    private final SalesDAO salesDAO = new SalesDAO();

    public List<MoviesEntity> filmsEntityList(){
        return moviesDAO.getDataAll();
    }

    public List<ScreeningsEntity> getScreeningsForMovie(int idMovie, Timestamp date){
        return screeningsDAO.getAllScreeningsForMovie(idMovie, date);
    }

    public boolean addSales(SalesEntity sales){
        return salesDAO.addData(sales);
    }

    public List<SalesEntity> getAllSalesForScreening(int idScreening, int idCustomer){
        return salesDAO.getAllSoldSeatsForScreening(idScreening, idCustomer);
    }

    public List<MoviesEntity> getMostViewedMovies(){

        List<Integer> list = salesDAO.mostViewedMovies();

        List<MoviesEntity> mostSoldMovies = new ArrayList<>();

        for (Integer value : list){

            mostSoldMovies.add(moviesDAO.getData(value));

        }

        return mostSoldMovies;

    }



}
