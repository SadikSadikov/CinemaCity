package com.example.cinemacity.Application;

import com.example.cinemacity.HibernateOracle.Utility.HibernateUtil;
import com.example.cinemacity.JFX.Controller.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.persistence.EntityManager;

public class App extends Application {

    public static void main(String[] args) {
        App.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneController.getInitialScene(stage);
    }

    @Override
    public void init() throws Exception {
        EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
        entityManager.close();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateUtil.shutdown();

    }
}
