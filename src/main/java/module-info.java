module com.example.cinemacity {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires com.jfoenix;
    requires java.prefs;
    requires javafx.swing;
    requires jfxtras.controls;

    opens com.example.cinemacity.Application;
    opens com.example.cinemacity.HibernateOracle.Model;
    opens com.example.cinemacity.HibernateOracle.DAO;
    opens com.example.cinemacity.HibernateOracle.Utility;
    opens com.example.cinemacity.JFX.Controller;
    opens com.example.cinemacity.Service.Classes;
    opens com.example.cinemacity.Service.Interfaces;
    opens com.example.cinemacity.Helpers;
    opens com.example.cinemacity to javafx.fxml;
    exports com.example.cinemacity.Application;
}