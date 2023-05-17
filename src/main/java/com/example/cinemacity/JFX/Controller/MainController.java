package com.example.cinemacity.JFX.Controller;

import com.example.cinemacity.Helpers.Converter;
import com.example.cinemacity.Helpers.CurrentMovie;
import com.example.cinemacity.HibernateOracle.Model.MoviesEntity;
import com.example.cinemacity.Service.Classes.MovieService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import jfxtras.scene.control.*;

public class MainController implements Initializable {
    @FXML
    public StackPane stackPaneSlideShow;
    @FXML
    public JFXButton buttonClose;
    @FXML
    public JFXButton buttonMinimize;
    @FXML
    public AnchorPane anchorPaneScrollbar;
    @FXML
    public GridPane gridPaneMovies;
    @FXML
    public GridPane gridPaneMostViewedMovies;
    @FXML
    public StackPane stackPaneRoot;
    @FXML
    public AnchorPane anchorPaneRoot;
    @FXML
    public Label labelLogo;
    @FXML
    public HBox hBoxSearch;
    @FXML
    public JFXButton buttonSearch;
    @FXML
    public JFXButton buttonCloseTextField;
    @FXML
    public TextField textFieldSearch;
    @FXML
    public JFXButton buttonSignOut;

    private final MovieService movieService = new MovieService();


    private AutoCompletionBinding<String> autoCompletionBinding;
    private boolean isOpenSearchTextField = false;

    private List<String> nameOfMovieList = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonCloseTextField.setOpacity(0);
        textFieldSearch.setOpacity(0);

        Platform.runLater(()->{
            stackPaneRoot.applyCss();
            stackPaneRoot.layout();

            buttonClose();
            buttonMinimize();
            slideshow();
            fillGridPaneMovies();
            fillGridPaneMostSoldMovies();

            // Logo
            Font font = Font.loadFont("file:///C:\\Users\\USER\\IdeaProjects\\CinemaCity\\Font\\KnifeFightBallet-Italic.ttf", 24);
            labelLogo.setFont(font);

            // Search Movie
            searchMovieTextField();

        });

    }

    @FXML
    public void buttonSignOutAction(ActionEvent actionEvent) throws IOException {
        SceneController.getLoginScene(actionEvent);
    }

    private void slideshow(){

        double imageWidth = 300;
        double imageHeight = 200;
        double imageSpacing = 10;

        String images_directory = "C:\\Users\\USER\\IdeaProjects\\CinemaCity\\Image\\SlideShow\\"; // Път към директорията със снимките
        Group imageGroup = new Group();

        File imagesDirectory = new File(images_directory); // Създаване на обект за директорията със снимките

        if (imagesDirectory.exists() && imagesDirectory.isDirectory()) { // Проверка дали директорията съществува и е директория
            File[] imageFiles = imagesDirectory.listFiles(); // Вземане на всички файлове в директорията
            int i = 0;

            // Обхождане на всички файлове и добавяне на снимките в клетките на GridPane
            assert imageFiles != null;
            for (File imageFile : imageFiles) {
                if (imageFile.isFile() && imageFile.getName().toLowerCase().endsWith(".jpg")) { // Проверка дали текущият файл е снимка (с разширение .jpg)
                    // Създаване на ImageView със снимка
                    ImageView imageView = new ImageView(new Image(imageFile.toURI().toString()));
                    imageView.setFitWidth(imageWidth);
                    imageView.setFitHeight(imageHeight);
                    imageView.setTranslateX(i * (imageWidth + imageSpacing));

                    imageGroup.getChildren().add(imageView);
                    i++;

                }
            }
        }

        animateCarousel(imageGroup);

    }

    private void buttonClose(){
        buttonClose.setOnAction(SceneController::close);
    }

    private void buttonMinimize(){
        buttonMinimize.setOnAction(SceneController::minimize);
    }

    private void animateCarousel(Group imageGroup) {

        double stackPaneSlideshowWidth= 1000;

        double startEndSlideShow = ((imageGroup.getBoundsInParent().getWidth() / 2.0) - (stackPaneSlideshowWidth / 2));

        double totalDuration = 35.0;

        double startXRight = startEndSlideShow;

        double endXRight = -startEndSlideShow;

        stackPaneSlideShow.getChildren().add(imageGroup);
        stackPaneSlideShow.setAlignment(Pos.CENTER);

        stackPaneSlideShow.setMinWidth(0);
        stackPaneSlideShow.setMaxWidth(imageGroup.getBoundsInParent().getWidth());
        stackPaneSlideShow.setMinHeight(0);
        stackPaneSlideShow.setMaxHeight(imageGroup.getBoundsInParent().getHeight());

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(stackPaneSlideShow.widthProperty());
        clip.heightProperty().bind(stackPaneSlideShow.heightProperty());
        stackPaneSlideShow.setClip(clip);

        TranslateTransition translateTransitionRight = new TranslateTransition(Duration.seconds(totalDuration), imageGroup);
        translateTransitionRight.setFromX(startXRight);
        translateTransitionRight.setToX(endXRight);
        translateTransitionRight.setAutoReverse(true);
        translateTransitionRight.setCycleCount(TranslateTransition.INDEFINITE);

        translateTransitionRight.play();
    }

    private void fillGridPaneMostSoldMovies(){
        double image_size_width = 220; // Размер на снимките
        double image_size_height = 275;
        List<MoviesEntity> movies = movieService.getMostViewedMovies();

        if (!movies.isEmpty()){
            int row = 0; // Променлива за текущия ред
            int col = 0; // Променлива за текущата колона

            for (MoviesEntity movie : movies) {

                ImageView imageView = new ImageView(Converter.convertArrayOfByteToImage(movie.getImage()));
                imageView.setUserData(movie);
                imageView.setFitWidth(image_size_width); // Задаване на ширина на ImageView
                imageView.setFitHeight(image_size_height); // Задаване на височина на ImageView

                // Добавяне на ImageView в клетката на GridPane
                gridPaneMostViewedMovies.add(imageView, col, row);

                imageView.setOnMouseEntered(event -> {
                    // Код за уголемяване на снимката при клик
                    imageView.setScaleX(imageView.getScaleX() + 0.1);
                    imageView.setScaleY(imageView.getScaleY() + 0.1);
                });

                imageView.setOnMouseExited(event -> {
                    // Код за уголемяване на снимката при клик
                    imageView.setScaleX(imageView.getScaleX() - 0.1);
                    imageView.setScaleY(imageView.getScaleY() - 0.1);
                });


                imageView.setOnMouseClicked(event -> {
                    try {
                        CurrentMovie.setMoviesEntity((MoviesEntity) imageView.getUserData());
                        // Зареждане на втората сцена
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemacity/movie-main.fxml"));
                        Parent root = loader.load();

                        // Създаване на нова сцена
                        Scene newScene = new Scene(root);

                        // Вземане на текущата сцена
                        Scene currentScene = imageView.getScene();

                        // Добавяне на новата сцена към родителския контейнер
                        StackPane parentContainer = (StackPane) currentScene.getRoot();
                        parentContainer.getChildren().add(root);

                        // Задаване на начална стойност за преместването на новата сцена
                        root.translateYProperty().set(currentScene.getHeight());

                        // Създаване на анимация за преместване на новата сцена
                        Timeline timeline = new Timeline();
                        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                        timeline.getKeyFrames().add(kf);
                        timeline.setOnFinished(t -> {
                            // Премахване на старата сцена
                            parentContainer.getChildren().remove(currentScene.getRoot());
                        });
                        timeline.play();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });



                col++; // Увеличаване на текущата колона

                // Проверка дали сме стигнали до последната колона и ако да, да минем на следващия ред и да нулираме текущата колона
                if (col >= gridPaneMostViewedMovies.getColumnCount()) {
                    col = 0;
                    if (row >= gridPaneMostViewedMovies.getRowCount()){
                        return;
                    }
                    row++;

                }
            }
        }
    }

    private void fillGridPaneMovies(){
        double image_size_width = 220; // Размер на снимките
        double image_size_height = 275;
        List<MoviesEntity> movies = movieService.filmsEntityList();

        if (!movies.isEmpty()){
            int row = 0; // Променлива за текущия ред
            int col = 0; // Променлива за текущата колона

            for (MoviesEntity movie : movies) {
                    //Fill list For name of Movie
                    nameOfMovieList.add(movie.getTitle());

                    ImageView imageView = new ImageView(Converter.convertArrayOfByteToImage(movie.getImage()));
                    imageView.setUserData(movie);
                    imageView.setFitWidth(image_size_width); // Задаване на ширина на ImageView
                    imageView.setFitHeight(image_size_height); // Задаване на височина на ImageView

                    // Добавяне на ImageView в клетката на GridPane
                    gridPaneMovies.add(imageView, col, row);

                    imageView.setOnMouseEntered(event -> {
                        // Код за уголемяване на снимката при клик
                        imageView.setScaleX(imageView.getScaleX() + 0.1);
                        imageView.setScaleY(imageView.getScaleY() + 0.1);
                    });

                    imageView.setOnMouseExited(event -> {
                        // Код за уголемяване на снимката при клик
                        imageView.setScaleX(imageView.getScaleX() - 0.1);
                        imageView.setScaleY(imageView.getScaleY() - 0.1);
                    });


                imageView.setOnMouseClicked(event -> {
                    try {
                        CurrentMovie.setMoviesEntity((MoviesEntity) imageView.getUserData());
                        // Зареждане на втората сцена
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemacity/movie-main.fxml"));
                        Parent root = loader.load();

                        // Създаване на нова сцена
                        Scene newScene = new Scene(root);

                        // Вземане на текущата сцена
                        Scene currentScene = imageView.getScene();

                        // Добавяне на новата сцена към родителския контейнер
                        StackPane parentContainer = (StackPane) currentScene.getRoot();
                        parentContainer.getChildren().add(root);

                        // Задаване на начална стойност за преместването на новата сцена
                        root.translateYProperty().set(currentScene.getHeight());

                        // Създаване на анимация за преместване на новата сцена
                        Timeline timeline = new Timeline();
                        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
                        timeline.getKeyFrames().add(kf);
                        timeline.setOnFinished(t -> {
                            // Премахване на старата сцена
                            parentContainer.getChildren().remove(currentScene.getRoot());
                        });
                        timeline.play();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });



                    col++; // Увеличаване на текущата колона

                    // Проверка дали сме стигнали до последната колона и ако да, да минем на следващия ред и да нулираме текущата колона
                    if (col >= gridPaneMovies.getColumnCount()) {
                        col = 0;
                        if (row >= gridPaneMovies.getRowCount()){
                            return;
                        }
                        row++;

                    }
            }
            //add text in AutoCompleteText
            autoCompletionBinding = TextFields.bindAutoCompletion(textFieldSearch, nameOfMovieList);
        }
    }

    private void searchMovieTextField(){

        buttonSearch.setOnMouseClicked(e ->{

            if (!isOpenSearchTextField){
                showSearchTextField(textFieldSearch, buttonSearch, buttonCloseTextField);
                textFieldSearch.setDisable(false);
                buttonCloseTextField.setDisable(false);
            }
            else{
                List<MoviesEntity> moviesEntityList = movieService.filmsEntityList();
                for (MoviesEntity movie: moviesEntityList){
                    if (movie.getTitle().equals(textFieldSearch.getText())){
                        CurrentMovie.setMoviesEntity(movie);
                        try {
                            SceneController.getMovieMainScene(e);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

        });

        buttonCloseTextField.setOnMouseClicked(e->{
            hideSearchTextField(textFieldSearch, buttonSearch, buttonCloseTextField);
            textFieldSearch.setDisable(true);
            buttonCloseTextField.setDisable(true);
        });

    }

    private void showSearchTextField(TextField searchTextField, Button searchButton, Button closeButton) {
        isOpenSearchTextField = true;

        FadeTransition ft = new FadeTransition(Duration.millis(200), searchTextField);
        ft.setFromValue(0);
        ft.setToValue(1);


        FadeTransition closeBtnFadeIn = new FadeTransition(Duration.millis(200), closeButton);
        closeBtnFadeIn.setFromValue(0);
        closeBtnFadeIn.setToValue(1);

        TranslateTransition tt = new TranslateTransition(Duration.millis(200), searchTextField);
        tt.setToX(0);

        TranslateTransition ttt = new TranslateTransition(Duration.millis(200), closeButton);
        ttt.setToX(0);

        ParallelTransition pt = new ParallelTransition(textFieldSearch, ft, tt);
        ParallelTransition ptt = new ParallelTransition(closeButton, closeBtnFadeIn, ttt);
        ptt.play();
        pt.play();
    }


    private void hideSearchTextField(TextField searchTextField, Button searchButton, Button closeButton) {
        isOpenSearchTextField = false;

        FadeTransition ft = new FadeTransition(Duration.millis(200), searchTextField);
        ft.setToValue(0);

        FadeTransition closeBtnFadeOut = new FadeTransition(Duration.millis(200), closeButton);
        closeBtnFadeOut.setToValue(0);

        double buttonWidth = searchButton.getWidth();

        double textFieldWidth = searchTextField.getWidth();

        TranslateTransition tt = new TranslateTransition(Duration.millis(200), searchTextField);
        tt.setToX(buttonWidth-10);

        TranslateTransition ttt = new TranslateTransition(Duration.millis(200), closeButton);
        ttt.setToX(buttonWidth-10);

        // Reset position of search text field when animation is finished
        ParallelTransition pt = new ParallelTransition(searchButton, ft, tt, closeBtnFadeOut);
        ParallelTransition ptt = new ParallelTransition(closeButton,closeBtnFadeOut, ttt);

        pt.setOnFinished(e -> {
            searchTextField.setLayoutX(-textFieldWidth);
            //textFieldWidth
            searchTextField.clear();
            isOpenSearchTextField = false;

        });

        ptt.play();
        pt.play();
    }



}
