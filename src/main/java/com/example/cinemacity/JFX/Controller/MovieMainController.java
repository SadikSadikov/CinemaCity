package com.example.cinemacity.JFX.Controller;

import com.example.cinemacity.Helpers.*;
import com.example.cinemacity.HibernateOracle.Model.CustomerEntity;
import com.example.cinemacity.HibernateOracle.Model.SalesEntity;
import com.example.cinemacity.HibernateOracle.Model.ScreeningsEntity;
import com.example.cinemacity.HibernateOracle.Model.TicketsEntity;
import com.example.cinemacity.Service.Classes.MovieService;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.prefs.Preferences;

public class MovieMainController implements Initializable {

    @FXML
    public StackPane stackPaneRoot;
    @FXML
    public AnchorPane anchorPaneRoot;
    @FXML
    public AnchorPane anchorPaneMenu;
    @FXML
    public GridPane gridPaneToday;
    @FXML
    public GridPane gridPaneTomorrow;
    @FXML
    public Label labelTomorrowDate;
    @FXML
    public Label labelTodayDate;
    @FXML
    public Label labelNameMovie;
    @FXML
    public JFXButton buttonBack;
    @FXML
    public AnchorPane anchorPaneMenuBuyTicket;
    @FXML
    public GridPane gridPaneSeats;
    @FXML
    public Label labelSeatAvailable;
    @FXML
    public Label labelSeatSelected;
    @FXML
    public Label labelSeatSold;
    @FXML
    public JFXButton buttonAddCardFirst;
    @FXML
    public JFXButton buttonAddCardSecond;
    @FXML
    public AnchorPane anchorPaneCardFirst;
    @FXML
    public JFXToggleButton toggleButtonFirst;
    @FXML
    public Label labelCardNumberFirst;
    @FXML
    public Label labelCardHolderNameFirst;
    @FXML
    public Label labelExpiryDateFirst;
    @FXML
    public JFXButton buttonPay;
    @FXML
    public AnchorPane anchorPaneCardSecond;
    @FXML
    public JFXToggleButton toggleButtonSecond;
    @FXML
    public Label labelCardNumberSecond;
    @FXML
    public Label labelCardHolderNameSecond;
    @FXML
    public Label labelExpiryDateSecond;
    @FXML
    public Label labelSelectedRow;
    @FXML
    public Label labelSelectedPlace;
    @FXML
    public Label labelTotalPrice;
    @FXML
    public JFXButton buttonCloseCardFirst;
    @FXML
    public JFXButton buttonCloseCardSecond;

    private final MovieService movieService = new MovieService();

    private boolean isOpenSceneForBuyTickets = false;

    private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    private static final String cNumberFirst_PREF_KEY = "cNumberFirst";
    private static final String eDateFirst_PREF_KEY = "eDateFirst";
    private static final String cHolderNameFirst_PREF_KEY = "cHolderNameFirst";
    private static final String saveCardFirst_PREF_KEY = "saveCardFirst";

    private static final String cNumberSecond_PREF_KEY = "cNumberSecond";
    private static final String eDateSecond_PREF_KEY = "eDateSecond";
    private static final String cHolderNameSecond_PREF_KEY = "cHolderNameSecond";
    private static final String saveCardSecond_PREF_KEY = "saveCardSecond";

    private List<String> soldTicketsRowColumn = new ArrayList<>();

    private double totalPrice = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(()->{
            stackPaneRoot.applyCss();
            stackPaneRoot.layout();
            if (preferences != null) {

                Platform.runLater(() -> {
                    String savedNumberFirst = preferences.get(cNumberFirst_PREF_KEY, "");
                    String savedDateFirst = preferences.get(eDateFirst_PREF_KEY, "");
                    String savedNameFirst = preferences.get(cHolderNameFirst_PREF_KEY, "");
                    boolean isRememberMeCheckedFirst = preferences.getBoolean(saveCardFirst_PREF_KEY, false);

                    String savedNumberSecond = preferences.get(cNumberSecond_PREF_KEY, "");
                    String savedDateSecond = preferences.get(eDateSecond_PREF_KEY, "");
                    String savedNameSecond = preferences.get(cHolderNameSecond_PREF_KEY, "");
                    boolean isRememberMeCheckedSecond = preferences.getBoolean(saveCardSecond_PREF_KEY, false);

                    if (isRememberMeCheckedFirst) {
                        buttonAddCardFirst.setVisible(false);
                        anchorPaneCardFirst.setVisible(true);
                    }
                    toggleButtonFirst.setSelected(false);
                    labelCardNumberFirst.setText(savedNumberFirst);
                    labelExpiryDateFirst.setText(savedDateFirst);
                    labelCardHolderNameFirst.setText(savedNameFirst);

                    if (isRememberMeCheckedSecond) {
                        buttonAddCardSecond.setVisible(false);
                        anchorPaneCardSecond.setVisible(true);
                    }

                    toggleButtonSecond.setSelected(false);
                    labelCardNumberSecond.setText(savedNumberSecond);
                    labelExpiryDateSecond.setText(savedDateSecond);
                    labelCardHolderNameSecond.setText(savedNameSecond);


                });

            }

            createButtonHourForToday();
            createButtonHourForTomorrow();
            setValues();

            labelSelectedRow.setText("");
            labelSelectedPlace.setText("");
            labelTotalPrice.setText("");

        });

    }

    @FXML
    public void buttonBackAction(ActionEvent actionEvent) {

        if (isOpenSceneForBuyTickets){
            isOpenSceneForBuyTickets = false;
            anchorPaneMenuBuyTicket.setVisible(false);
            anchorPaneMenu.setVisible(true);
        }
        else {
            CurrentMovie.setMoviesEntity(null);
            // Зареждане на втората сцена
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemacity/main.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Създаване на нова сцена
            Scene newScene = new Scene(root);

            // Вземане на текущата сцена
            Scene currentScene = buttonBack.getScene();

            // Добавяне на новата сцена към родителския контейнер
            StackPane parentContainer = (StackPane) currentScene.getRoot();
            parentContainer.getChildren().add(root);

            // Задаване на начална стойност за преместването на новата сцена
            root.translateYProperty().set(-currentScene.getHeight());

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
        }


    }

    @FXML
    public void buttonAddCardFirstAction(ActionEvent actionEvent) {

        BoxBlur blur = new BoxBlur(4, 4, 4);

        VBox vBoxHeader = new VBox();
        vBoxHeader.setAlignment(Pos.CENTER);
        vBoxHeader.setSpacing(20);


        VBox vboxBody = new VBox();
        vboxBody.setAlignment(Pos.CENTER);
        vboxBody.setSpacing(30);

        VBox vboxCardNumber = new VBox();
        vboxCardNumber.setAlignment(Pos.CENTER_LEFT);
        vboxCardNumber.setSpacing(5);

        VBox vboxExpiryDate = new VBox();
        vboxExpiryDate.setAlignment(Pos.CENTER_LEFT);
        vboxExpiryDate.setSpacing(5);

        VBox vboxCardholderName = new VBox();
        vboxCardholderName.setAlignment(Pos.CENTER_LEFT);
        vboxCardholderName.setSpacing(5);


        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXDialog dialog = new JFXDialog();

        JFXButton button = new JFXButton("Add Card");
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-background-color: transparent;" +
                "-fx-border-radius: 50px;" +
                "-fx-background-radius: 50px;" +
                "-fx-border-color: linear-gradient(to bottom left, #FF9900, #FFCC00, #CCFF00, #00CCFF, #FF00FF);"+
                "-fx-pref-width: 190px;"+
                "-fx-pref-height: 40px;" +
                "-fx-text-fill: white;");

        JFXTextField textFieldCardNumber = new JFXTextField();
        textFieldCardNumber.setFocusColor(Color.DIMGRAY);
        textFieldCardNumber.setUnFocusColor(Color.WHITE);
        textFieldCardNumber.setStyle("-fx-text-fill: white;");
        textFieldCardNumber.setPromptText("0000 0000 0000 0000");

        JFXTextField textFieldExpiryDate = new JFXTextField();
        textFieldExpiryDate.setFocusColor(Color.DIMGRAY);
        textFieldExpiryDate.setUnFocusColor(Color.WHITE);
        textFieldExpiryDate.setStyle("-fx-text-fill: white;");
        textFieldExpiryDate.setPromptText("MM/YY");

        JFXTextField textFieldCardholderName = new JFXTextField();
        textFieldCardholderName.setFocusColor(Color.DIMGRAY);
        textFieldCardholderName.setUnFocusColor(Color.WHITE);
        textFieldCardholderName.setStyle("-fx-text-fill: white;");
        textFieldCardholderName.setPromptText("Enter cardholder`s full name");

        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.setText("Save card");
        checkBox.setTextFill(Color.WHITE);

        Label labelHeader = new Label("Add New Card");
        labelHeader.setTextFill(Color.WHITE);
        labelHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label labelCardNumber = new Label("Card number");
        labelCardNumber.setTextFill(Color.WHITE);
        labelCardNumber.setStyle("-fx-font-size: 10px;");

        Label labelExpiryDate = new Label("Expiry date");
        labelExpiryDate.setTextFill(Color.WHITE);
        labelExpiryDate.setStyle("-fx-font-size: 10px;");

        Label labelCardholderName = new Label("Cardholder name");
        labelCardholderName.setTextFill(Color.WHITE);
        labelCardholderName.setStyle("-fx-font-size: 10px;");

        vboxCardNumber.getChildren().addAll(labelCardNumber, textFieldCardNumber);

        vboxExpiryDate.getChildren().addAll(labelExpiryDate, textFieldExpiryDate);

        vboxCardholderName.getChildren().addAll(labelCardholderName, textFieldCardholderName);

        vboxBody.getChildren().addAll(vboxCardNumber,vboxExpiryDate,vboxCardholderName,checkBox , button);

        button.setOnMouseClicked(mouseEvent -> {

            if (checkBox.isSelected()){
                savePreferencesFirstCard(textFieldCardNumber.getText(), textFieldExpiryDate.getText(), textFieldCardholderName.getText(), true);
            }
            else {
                savePreferencesFirstCard("", "", "", false);
            }

            buttonAddCardFirst.setVisible(false);
            anchorPaneCardFirst.setVisible(true);

            toggleButtonFirst.setSelected(false);
            labelCardNumberFirst.setText(textFieldCardNumber.getText());
            labelExpiryDateFirst.setText(textFieldExpiryDate.getText());
            labelCardHolderNameFirst.setText(textFieldCardholderName.getText());

            dialog.close();
        });

        button.setTextFill(Color.BLACK);

        vBoxHeader.getChildren().addAll(labelHeader);


        dialogLayout.setHeading(vBoxHeader);

        dialogLayout.setBody(vboxBody);

        dialogLayout.setStyle("-fx-background-color: #1c1c1c;" + "-fx-border-color: linear-gradient(to bottom left, #FF9900, #FFCC00, #CCFF00, #00CCFF, #FF00FF);");
        dialogLayout.setPrefWidth(300);

        dialog.setContent(dialogLayout);

        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);

        dialog.setDialogContainer(stackPaneRoot);

        dialog.show();

        dialog.setOnDialogClosed((JFXDialogEvent event1) -> anchorPaneRoot.setEffect(null));

        anchorPaneRoot.setEffect(blur);


    }

    @FXML
    public void buttonAddCardSecondAction(ActionEvent actionEvent) {

        BoxBlur blur = new BoxBlur(4, 4, 4);

        VBox vBoxHeader = new VBox();
        vBoxHeader.setAlignment(Pos.CENTER);
        vBoxHeader.setSpacing(20);


        VBox vboxBody = new VBox();
        vboxBody.setAlignment(Pos.CENTER);
        vboxBody.setSpacing(30);

        VBox vboxCardNumber = new VBox();
        vboxCardNumber.setAlignment(Pos.CENTER_LEFT);
        vboxCardNumber.setSpacing(5);

        VBox vboxExpiryDate = new VBox();
        vboxExpiryDate.setAlignment(Pos.CENTER_LEFT);
        vboxExpiryDate.setSpacing(5);

        VBox vboxCardholderName = new VBox();
        vboxCardholderName.setAlignment(Pos.CENTER_LEFT);
        vboxCardholderName.setSpacing(5);


        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXDialog dialog = new JFXDialog();

        JFXButton button = new JFXButton("Add Card");
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-background-color: transparent;" +
                "-fx-border-radius: 50px;" +
                "-fx-background-radius: 50px;" +
                "-fx-border-color: linear-gradient(to bottom left, #FF9900, #FFCC00, #CCFF00, #00CCFF, #FF00FF);"+
                "-fx-pref-width: 190px;"+
                "-fx-pref-height: 40px;" +
                "-fx-text-fill: white;");

        JFXTextField textFieldCardNumber = new JFXTextField();
        textFieldCardNumber.setFocusColor(Color.DIMGRAY);
        textFieldCardNumber.setUnFocusColor(Color.WHITE);
        textFieldCardNumber.setStyle("-fx-text-fill: white;");
        textFieldCardNumber.setPromptText("0000 0000 0000 0000");

        JFXTextField textFieldExpiryDate = new JFXTextField();
        textFieldExpiryDate.setFocusColor(Color.DIMGRAY);
        textFieldExpiryDate.setUnFocusColor(Color.WHITE);
        textFieldExpiryDate.setStyle("-fx-text-fill: white;");
        textFieldExpiryDate.setPromptText("MM/YY");

        JFXTextField textFieldCardholderName = new JFXTextField();
        textFieldCardholderName.setFocusColor(Color.DIMGRAY);
        textFieldCardholderName.setUnFocusColor(Color.WHITE);
        textFieldCardholderName.setStyle("-fx-text-fill: white;");
        textFieldCardholderName.setPromptText("Enter cardholder`s full name");

        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.setText("Save card");
        checkBox.setTextFill(Color.WHITE);

        Label labelHeader = new Label("Add New Card");
        labelHeader.setTextFill(Color.WHITE);
        labelHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label labelCardNumber = new Label("Card number");
        labelCardNumber.setTextFill(Color.WHITE);
        labelCardNumber.setStyle("-fx-font-size: 10px;");

        Label labelExpiryDate = new Label("Expiry date");
        labelExpiryDate.setTextFill(Color.WHITE);
        labelExpiryDate.setStyle("-fx-font-size: 10px;");

        Label labelCardholderName = new Label("Cardholder name");
        labelCardholderName.setTextFill(Color.WHITE);
        labelCardholderName.setStyle("-fx-font-size: 10px;");

        vboxCardNumber.getChildren().addAll(labelCardNumber, textFieldCardNumber);

        vboxExpiryDate.getChildren().addAll(labelExpiryDate, textFieldExpiryDate);

        vboxCardholderName.getChildren().addAll(labelCardholderName, textFieldCardholderName);

        vboxBody.getChildren().addAll(vboxCardNumber,vboxExpiryDate,vboxCardholderName,checkBox ,button);

        button.setOnMouseClicked(mouseEvent -> {

            if (checkBox.isSelected()){
                savePreferencesSecondCard(textFieldCardNumber.getText(), textFieldExpiryDate.getText(), textFieldCardholderName.getText(), true);
            }
            else {
                savePreferencesSecondCard("", "", "", false);
            }

            buttonAddCardSecond.setVisible(false);
            anchorPaneCardSecond.setVisible(true);

            toggleButtonSecond.setSelected(false);
            labelCardNumberSecond.setText(textFieldCardNumber.getText());
            labelExpiryDateSecond.setText(textFieldExpiryDate.getText());
            labelCardHolderNameSecond.setText(textFieldCardholderName.getText());

            dialog.close();
        });

        button.setTextFill(Color.BLACK);

        vBoxHeader.getChildren().addAll(labelHeader);


        dialogLayout.setHeading(vBoxHeader);

        dialogLayout.setBody(vboxBody);

        dialogLayout.setStyle("-fx-background-color: #1c1c1c;" + "-fx-border-color: linear-gradient(to bottom left, #FF9900, #FFCC00, #CCFF00, #00CCFF, #FF00FF);");
        dialogLayout.setPrefWidth(300);

        dialog.setContent(dialogLayout);

        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);

        dialog.setDialogContainer(stackPaneRoot);

        dialog.show();

        dialog.setOnDialogClosed((JFXDialogEvent event1) -> anchorPaneRoot.setEffect(null));

        anchorPaneRoot.setEffect(blur);
    }

    @FXML
    public void buttonPayAction(ActionEvent actionEvent) {
        if (!toggleButtonFirst.isSelected() && !toggleButtonSecond.isSelected()){
            messagesForCustomer("Please select at least one payment card!");
        }
        else if (toggleButtonFirst.isSelected() && toggleButtonSecond.isSelected()){
            messagesForCustomer("Please select one payment card!");
        }
        else {
            if (!soldTicketsRowColumn.isEmpty()){
                playAnimationForSuccessfullyPaid();

                for (String value : soldTicketsRowColumn){
                    String[] parts = value.split("-");
                    String firstValue = parts[0];
                    String secondValue = parts[1];

                    movieService.addSales(new SalesEntity(CurrentScreeningMovie.getScreeningsEntity(),(CustomerEntity) CurrentUser.getUser(),Integer.parseInt(firstValue), Integer.parseInt(secondValue), Timestamp.valueOf(LocalDateTime.now()), 15));

                }
                createSeatsAndUpdate();
                soldTicketsRowColumn.clear();
                labelSelectedRow.setText("");
                labelSelectedPlace.setText("");

                totalPrice = 0.0;
                labelTotalPrice.setText("");


            }
            else{
                messagesForCustomer("Please select seats");
            }

        }
    }

    @FXML
    public void toggleButtonFirstAction(ActionEvent actionEvent) {
        if (toggleButtonFirst.isSelected()){
            anchorPaneCardFirst.setOpacity(1);
        }
        else {
            anchorPaneCardFirst.setOpacity(0.5);
        }
    }

    @FXML
    public void toggleButtonSecondAction(ActionEvent actionEvent) {
        if (toggleButtonSecond.isSelected()){
            anchorPaneCardSecond.setOpacity(1);
        }
        else {
            anchorPaneCardSecond.setOpacity(0.5);
        }
    }

    @FXML
    public void buttonCloseCardFirstAction(ActionEvent actionEvent) {

        savePreferencesFirstCard("", "", "", false);

        anchorPaneCardFirst.setVisible(false);
        buttonAddCardFirst.setVisible(true);
    }

    @FXML
    public void buttonCloseCardSecondAction(ActionEvent actionEvent) {

        savePreferencesSecondCard("", "", "", false);

        anchorPaneCardSecond.setVisible(false);
        buttonAddCardSecond.setVisible(true);
    }


    private void setValues() {

        //Background
        byte[] imageData = CurrentMovie.getMoviesEntity().getImage_background();
        Image image = Converter.convertArrayOfByteToImage(imageData);
        BackgroundSize backgroundSize = new BackgroundSize(anchorPaneRoot.getWidth(), anchorPaneRoot.getHeight(), true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        anchorPaneRoot.setBackground(background);

        //Name of Movie
        labelNameMovie.setText(CurrentMovie.getMoviesEntity().getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");

        //Today date

        labelTodayDate.setText(CurrentTime.getTime().format(formatter));

        //Tomorrow date
        labelTomorrowDate.setText(CurrentTime.getTime().plusDays(1).format(formatter));
    }

    private void createButtonHourForToday() {

        int maxCountOfRows = gridPaneToday.getRowCount();
        int maxCountOfColumn = gridPaneToday.getColumnCount();

        double buttonWidth = ((gridPaneToday.getWidth() / maxCountOfColumn) - 10);
        double buttonHeight = ((gridPaneToday.getHeight() / maxCountOfRows) - 10);

        gridPaneToday.setPadding(new Insets(10,10,10,10));

        //screen all screening movies for today and get number of movies - appropriation of variable list
        List<ScreeningsEntity> screeningsEntities = movieService.getScreeningsForMovie(CurrentMovie.getMoviesEntity().getId_movie(), Timestamp.valueOf(LocalDateTime.now()));

        //check for movies is empty
        if (!screeningsEntities.isEmpty()) {
            int row = 0;
            int col = 0;
            for (ScreeningsEntity screenings : screeningsEntities){
                JFXButton button = new JFXButton();
                button.setPrefWidth(buttonWidth);
                button.setPrefHeight(buttonHeight);
                button.setRipplerFill(Color.BLACK);

                button.setUserData(screenings);

                String topTextString = String.format("%02d:%02d", screenings.getTime_screening().toLocalDateTime().getHour(), screenings.getTime_screening().toLocalDateTime().getMinute());
                String bottomTextString = screenings.getNumber_screening();

                Text topText = new Text(topTextString);
                topText.setFont(Font.font("Arial", 18));
                topText.setFill(Color.WHITE);
                StackPane.setAlignment(topText, Pos.TOP_CENTER);
                StackPane.setMargin(topText, new Insets(5, 0, 0, 0));

                Text bottomText = new Text(bottomTextString);
                bottomText.setFont(Font.font("Arial", 12));
                bottomText.setFill(Color.WHITE);
                StackPane.setAlignment(bottomText, Pos.BOTTOM_CENTER);
                StackPane.setMargin(bottomText, new Insets(0, 0, 5, 0));

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(bottomText, topText);
                stackPane.setAlignment(Pos.CENTER);

                button.setGraphic(stackPane);

                button.setStyle("-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: white;" +
                        "-fx-background-color: transparent;");


                button.setOnAction(actionEvent -> {

                    isOpenSceneForBuyTickets = true;

                    CurrentScreeningMovie.setScreeningsEntity((ScreeningsEntity) button.getUserData());

                    anchorPaneMenu.setVisible(false);
                    anchorPaneMenuBuyTicket.setVisible(true);
                    createSeatsAndUpdate();
                });

                gridPaneToday.add(button, col, row);

                col++; // Увеличаване на текущата колона

                // Проверка дали сме стигнали до последната колона и ако да, да минем на следващия ред и да нулираме текущата колона
                if (col >= maxCountOfColumn) {
                    col = 0;
                    if (row >= maxCountOfRows){
                        return;
                    }
                    row++;

                }

            }
        }
    }

    private void createButtonHourForTomorrow(){
        int maxCountOfRows = gridPaneTomorrow.getRowCount();
        int maxCountOfColumn = gridPaneTomorrow.getColumnCount();

        double buttonWidth = ((gridPaneTomorrow.getWidth() / maxCountOfColumn) - 10);
        double buttonHeight = ((gridPaneTomorrow.getHeight() / maxCountOfRows) - 10);

        gridPaneTomorrow.setPadding(new Insets(10,10,10,10));

        //screen all screening movies for today and get number of movies - appropriation of variable list
        List<ScreeningsEntity> screeningsEntities = movieService.getScreeningsForMovie(CurrentMovie.getMoviesEntity().getId_movie(), Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

        //check for movies is empty
        if (!screeningsEntities.isEmpty()) {
            int row = 0;
            int col = 0;
            for (ScreeningsEntity screenings : screeningsEntities){
                JFXButton button = new JFXButton();
                button.setPrefWidth(buttonWidth);
                button.setPrefHeight(buttonHeight);
                button.setRipplerFill(Color.BLACK);

                button.setUserData(screenings);

                String topTextString = String.format("%02d:%02d", screenings.getTime_screening().toLocalDateTime().getHour(), screenings.getTime_screening().toLocalDateTime().getMinute());
                String bottomTextString = screenings.getNumber_screening();

                Text topText = new Text(topTextString);
                topText.setFont(Font.font("Arial", 18));
                topText.setFill(Color.WHITE);
                StackPane.setAlignment(topText, Pos.TOP_CENTER);
                StackPane.setMargin(topText, new Insets(5, 0, 0, 0));

                Text bottomText = new Text(bottomTextString);
                bottomText.setFont(Font.font("Arial", 12));
                bottomText.setFill(Color.WHITE);
                StackPane.setAlignment(bottomText, Pos.BOTTOM_CENTER);
                StackPane.setMargin(bottomText, new Insets(0, 0, 5, 0));

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(bottomText, topText);
                stackPane.setAlignment(Pos.CENTER);

                button.setGraphic(stackPane);

                button.setStyle("-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: white;" +
                        "-fx-background-color: transparent;");


                button.setOnAction(actionEvent -> {

                    isOpenSceneForBuyTickets = true;

                    CurrentScreeningMovie.setScreeningsEntity((ScreeningsEntity) button.getUserData());

                    anchorPaneMenu.setVisible(false);
                    anchorPaneMenuBuyTicket.setVisible(true);
                    createSeatsAndUpdate();
                });

                gridPaneTomorrow.add(button, col, row);

                col++; // Увеличаване на текущата колона

                // Проверка дали сме стигнали до последната колона и ако да, да минем на следващия ред и да нулираме текущата колона
                if (col >= maxCountOfColumn) {
                    col = 0;
                    if (row >= maxCountOfRows){
                        return;
                    }
                    row++;

                }

            }
        }
    }

    private void createSeatsAndUpdate(){

        List<String> soldSeats = new ArrayList<>();
        List<SalesEntity> salesEntities = movieService.getAllSalesForScreening(CurrentScreeningMovie.getScreeningsEntity().getId_screening(), ((CustomerEntity)CurrentUser.getUser()).getId_customer());
        for (SalesEntity sales : salesEntities){
            soldSeats.add(sales.getRow_number() + "-" + sales.getSeat_number());
        }

        Color availableSeatsColor = Color.rgb(172,172,230,0.9);
        Color selectedSeatsColor = Color.rgb(141, 224, 0, 0.9);
        Color soldSeatsColor = Color.rgb(132, 132, 132, 0.9);

        labelSeatAvailable.setBackground(new Background(new BackgroundFill(availableSeatsColor, null, null)));
        labelSeatSelected.setBackground(new Background(new BackgroundFill(selectedSeatsColor, null, null)));
        labelSeatSold.setBackground(new Background(new BackgroundFill(soldSeatsColor, null, null)));

        int maxRow = CurrentScreeningMovie.getScreeningsEntity().getMax_row();
        int maxColumn = CurrentScreeningMovie.getScreeningsEntity().getMax_column();
        for (int i = 0 ; i <  maxRow; i++){
            for (int j = 0 ; j <  maxColumn; j++){
                // създайте текст с номера на реда
                if (i == 0){
                    if ((j > 1) && (j < maxColumn - 2)){
                        setGridPaneSeats(gridPaneSeats,i ,j, soldSeats);
                    }
                }
                else if (i == 1){
                    if ((j > 0) && (j < maxColumn - 1)){
                        setGridPaneSeats(gridPaneSeats,i ,j, soldSeats);
                    }
                }
                else{
                    setGridPaneSeats(gridPaneSeats, i, j, soldSeats);
                }
            }
        }
    }

    private void setGridPaneSeats(GridPane gridPane, int i, int j, List<String> salesEntityList){


        Color availableSeatsColor = Color.rgb(172,172,230,0.9);
        Color selectedSeatsColor = Color.rgb(141, 224, 0, 0.9);
        Color soldSeatsColor = Color.rgb(132, 132, 132, 0.9);

        Text text = new Text(String.valueOf(i + 1));
        text.setFont(new Font("Arial", 15));

        text.setFill(Color.WHITE);

        StackPane cell = new StackPane(text);

        // проверка за закупени места
        if (salesEntityList.contains(i + "-" + j)) {
            cell.setBackground(new Background(new BackgroundFill(soldSeatsColor, null, null)));
        } else {
            cell.setBackground(new Background(new BackgroundFill(availableSeatsColor, null, null)));
        }

        // добавете слушател за щракване върху клетката
        cell.setOnMouseClicked(event -> {

            // променете цвета на фона на клетката
            if (cell.getBackground().getFills().get(0).getFill() == selectedSeatsColor) {
                soldTicketsRowColumn.remove(i + "-" + j);
                cell.setBackground(new Background(new BackgroundFill(availableSeatsColor, null, null)));

                String[] rowSplit = labelSelectedRow.getText().split(", ");
                List<String> newRowSplit = new ArrayList<>();

                boolean removedRow = false;
                for (String s : rowSplit) {
                    if (!s.equals(String.valueOf(i)) || removedRow) {
                        newRowSplit.add(s);
                    } else {
                        removedRow = true;
                    }
                }
                String newLabelRow = String.join(", ", newRowSplit);
                labelSelectedRow.setText(newLabelRow);


                String[] columnSplit = labelSelectedPlace.getText().split(", ");
                List<String> newColumnSplit = new ArrayList<>();

                boolean removedColumn = false;
                for (String s : columnSplit) {
                    if (!s.equals(String.valueOf(j)) || removedColumn) {
                        newColumnSplit.add(s);
                    } else {
                        removedColumn = true;
                    }
                }

                String newLabelColumn = String.join(", ", newColumnSplit);
                labelSelectedPlace.setText(newLabelColumn);

                totalPrice -= 15.0;
                labelTotalPrice.setText("$" + totalPrice);
            }
            else if(cell.getBackground().getFills().get(0).getFill() == availableSeatsColor){
                soldTicketsRowColumn.add(i +"-" +j);
                cell.setBackground(new Background(new BackgroundFill(selectedSeatsColor, null, null)));

                String selectedRow = labelSelectedRow.getText();
                labelSelectedRow.setText(selectedRow + i + ", ");

                String selectedColumn = labelSelectedPlace.getText();
                labelSelectedPlace.setText(selectedColumn + j + ", ");

                totalPrice += 15.0;
                labelTotalPrice.setText("$" + totalPrice);
            }
        });

        // добавете StackPane в клетката
        gridPane.add(cell, j, i);
    }

    private void savePreferencesFirstCard(String cNumber, String eDate, String cHolderName, boolean saveCard) {
        preferences.put(cNumberFirst_PREF_KEY, cNumber);
        preferences.put(eDateFirst_PREF_KEY, eDate);
        preferences.put(cHolderNameFirst_PREF_KEY, cHolderName);
        preferences.putBoolean(saveCardFirst_PREF_KEY, saveCard);
    }

    private void savePreferencesSecondCard(String cNumber, String eDate, String cHolderName, boolean saveCard) {
        preferences.put(cNumberSecond_PREF_KEY, cNumber);
        preferences.put(eDateSecond_PREF_KEY, eDate);
        preferences.put(cHolderNameSecond_PREF_KEY, cHolderName);
        preferences.putBoolean(saveCardSecond_PREF_KEY, saveCard);
    }

    private void messagesForCustomer(String message){
        BoxBlur blur = new BoxBlur(4, 4, 4);

        VBox vboxBody = new VBox();
        vboxBody.setAlignment(Pos.CENTER);
        vboxBody.setSpacing(30);


        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXDialog dialog = new JFXDialog();

        JFXButton button = new JFXButton("Ok");
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-background-color: transparent;" +
                "-fx-border-radius: 50px;" +
                "-fx-background-radius: 50px;" +
                "-fx-border-color: white;"+
                "-fx-pref-width: 190px;"+
                "-fx-pref-height: 40px;" +
                "-fx-text-fill: white;");


        Label labelMessage = new Label(message);
        labelMessage.setTextFill(Color.WHITE);
        labelMessage.setStyle("-fx-font-size: 15px;");


        vboxBody.getChildren().addAll(labelMessage, button);

        button.setOnMouseClicked(mouseEvent -> {
            dialog.close();
        });

        button.setTextFill(Color.BLACK);

        dialogLayout.setBody(vboxBody);

        dialogLayout.setStyle("-fx-background-color: #1c1c1c;" + "-fx-border-color: white;");

        dialog.setContent(dialogLayout);

        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);

        dialog.setDialogContainer(stackPaneRoot);

        dialog.show();

        dialog.setOnDialogClosed((JFXDialogEvent event1) -> anchorPaneRoot.setEffect(null));

        anchorPaneRoot.setEffect(blur);
    }

    private void playAnimationForSuccessfullyPaid(){

        // Load the gif
        Image gifImage = new Image("file:///C:\\Users\\USER\\IdeaProjects\\CinemaCity\\Image\\successfullyPaid.gif");
        ImageView gifView = new ImageView(gifImage);

        // Create the blur effect
        BoxBlur blur = new BoxBlur(6, 6, 6);

        // Create the fade transition for the gif
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), gifView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);


        // Create the pause transition for the gif
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        // Create the fade out transition for the gif
        FadeTransition fadeOutGif = new FadeTransition(Duration.millis(500), gifView);
        fadeOutGif.setFromValue(1);
        fadeOutGif.setToValue(0);


        // Create the sequential transition
        SequentialTransition seqT = new SequentialTransition(fadeIn, pause, fadeOutGif);
        seqT.setOnFinished(evt -> {
            anchorPaneRoot.setEffect(null);
        });

        // Disable the button and set the blur effect
        // Play the animation
        seqT.play();

        anchorPaneRoot.setEffect(blur);

        stackPaneRoot.getChildren().add(gifView);


    }



}
