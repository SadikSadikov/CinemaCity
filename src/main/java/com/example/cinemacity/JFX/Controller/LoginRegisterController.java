package com.example.cinemacity.JFX.Controller;

import com.example.cinemacity.Helpers.CurrentUser;
import com.example.cinemacity.HibernateOracle.Model.User;
import com.example.cinemacity.Service.Classes.UserServiceImpl;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXDialog;
import javafx.scene.layout.Region;

public class LoginRegisterController implements Initializable {

    @FXML
    public AnchorPane anchorPaneLogin;
    @FXML
    public AnchorPane anchorPaneLoginRegisterForm;
    @FXML
    public JFXTextField textFieldUsernameLogin;
    @FXML
    public JFXPasswordField passwordFieldPasswordLogin;
    @FXML
    public JFXCheckBox checkBoxRememberMe;
    @FXML
    public Hyperlink hyperlinkForgotPassword;
    @FXML
    public JFXButton buttonLogin;
    @FXML
    public JFXButton buttonSignIn;
    @FXML
    public JFXButton buttonSignUp;
    @FXML
    public AnchorPane anchorPaneRegister;
    @FXML
    public JFXTextField textFieldUsernameRegister;
    @FXML
    public JFXPasswordField passwordFieldPasswordRegister;
    @FXML
    public JFXButton buttonRegister;
    @FXML
    public Label labelMessageLogin;
    @FXML
    public Label labelMessageRegister;
    @FXML
    public JFXPasswordField passwordFieldConfirmPasswordRegister;
    @FXML
    public AnchorPane anchorPaneLoginRegister;
    @FXML
    public AnchorPane anchorPaneMenu;
    @FXML
    public JFXButton buttonClose;
    @FXML
    public JFXButton buttonMinimize;
    @FXML
    public Line lineSignIn;
    @FXML
    public Line lineSingUp;
    @FXML
    public StackPane rootPane;
    @FXML
    public StackPane stackPaneLRForm;

    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    private boolean isLoading;
    private boolean isLogin;
    private boolean isRegister;

    private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());
    private static final String USERNAME_PREF_KEY = "username";
    private static final String PASSWORD_PREF_KEY = "password";
    private static final String REMEMBER_ME_PREF_KEY = "rememberMe";

    private static final String successfulLabelStyle = "-fx-background-color: rgba(15, 15, 12, 0.9);" +
            "-fx-background-image: url('file:C:/Users/USER/IdeaProjects/CinemaCity/Image/check-mark.png');"
            + "-fx-background-size: 15px;"
            + "-fx-background-position: left+5px center;"
            + "-fx-background-repeat: no-repeat;";

    private static final String unsuccessfulLabelStyle = "-fx-background-color: rgba(15, 15, 12, 0.9);" +
            "-fx-background-image: url('file:C:/Users/USER/IdeaProjects/CinemaCity/Image/cross.png');"
            + "-fx-background-size: 15px;"
            + "-fx-background-position: left+5px center;"
            + "-fx-background-repeat: no-repeat;";

    private static final String colorRed = "#E42E18";

    private static final String colorGreen = "#45961A";

    private final UserServiceImpl userService = new UserServiceImpl();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (preferences != null){

            Platform.runLater(() -> {
                String savedUsername = preferences.get(USERNAME_PREF_KEY, "");
                String savedPassword = preferences.get(PASSWORD_PREF_KEY, "");
                boolean isRememberMeChecked = preferences.getBoolean(REMEMBER_ME_PREF_KEY, false);

                textFieldUsernameLogin.textProperty().setValue(savedUsername);
                passwordFieldPasswordLogin.textProperty().setValue(savedPassword);
                checkBoxRememberMe.selectedProperty().setValue(isRememberMeChecked);
            });

        }

        buttonClose();
        buttonMinimize();

    }

    @FXML
    public void buttonLoginAction(ActionEvent actionEvent) {

        if (!validFieldsLogin()){
            labelMessageLogin.setStyle(unsuccessfulLabelStyle);
            labelMessageLogin.setTextFill(Color.web(colorRed));
            labelMessageLogin.setText("Please enter username or password!");
            return;
        }


        if (!isLoading) {
            isLoading = true;
            anchorPaneLoginRegister.setDisable(true);

            progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

            anchorPaneLoginRegister.getChildren().add(progressIndicator);

            AnchorPane.setTopAnchor(progressIndicator, (anchorPaneLoginRegister.getHeight() - 52.8) / 2);
            AnchorPane.setLeftAnchor(progressIndicator, (anchorPaneLoginRegister.getWidth() - 52) / 2);

            // Създаваме Task за изпълнение на calculateMethod()
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    long startTime = System.nanoTime();
                    // Login Method
                    isLogin = validateLogin();
                    long endTime = System.nanoTime();
                    long elapsedTimeInNanos = endTime - startTime;
                    double elapsedTimeInSeconds = elapsedTimeInNanos / 1_000_000_000.0;

                    // Обновяваме напредъка на ProgressIndicator във визуалния нишков контекст
                    Platform.runLater(() -> progressIndicator.setProgress(elapsedTimeInSeconds));

                    Platform.runLater(() -> {
                        isLoading = false;
                        anchorPaneLoginRegister.setDisable(false);
                        anchorPaneLoginRegister.getChildren().remove(progressIndicator);

                        if (isLogin){
                            labelMessageLogin.setStyle(successfulLabelStyle);
                            labelMessageLogin.setTextFill(Color.web(colorGreen));
                            labelMessageLogin.setText("Successfully Sign In!");

                            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
                            pauseTransition.setOnFinished(event -> {
                                try {
                                    SceneController.getMainScene(actionEvent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            pauseTransition.play();

                        }
                        else{
                            labelMessageLogin.setStyle(unsuccessfulLabelStyle);
                            labelMessageLogin.setTextFill(Color.web(colorRed));
                            labelMessageLogin.setText("Incorrect username or password!");
                        }
                    });
                    return null;
                }
            };

            // Стартираме Task-a в отделна нишка
            new Thread(task).start();
        }

    }

    @FXML
    public void buttonSignInAction(ActionEvent actionEvent) {
        buttonSignUp.setStyle("-fx-text-fill: #666666");
        buttonSignIn.setStyle("-fx-text-fill: white");
        anchorPaneRegister.setVisible(false);
        anchorPaneLogin.setVisible(true);
        lineSingUp.setVisible(false);
        lineSignIn.setVisible(true);
        clearRegister();

    }

    @FXML
    public void buttonSignUpAction(ActionEvent actionEvent) {
        buttonSignIn.setStyle("-fx-text-fill: #666666");
        buttonSignUp.setStyle("-fx-text-fill: white");
        anchorPaneLogin.setVisible(false);
        anchorPaneRegister.setVisible(true);
        lineSignIn.setVisible(false);
        lineSingUp.setVisible(true);
        if (!checkBoxRememberMe.isSelected()){
            clearLogin();
        }

    }

    @FXML
    public void buttonRegisterAction(ActionEvent actionEvent) {
        if (!validFieldsRegister()){
            labelMessageRegister.setStyle(unsuccessfulLabelStyle);
            labelMessageRegister.setTextFill(Color.web(colorRed));
            labelMessageRegister.setText("Please enter username or password!");
            return;
        }

        if (!checkConfirmPassword()){
            labelMessageRegister.setStyle(unsuccessfulLabelStyle);
            labelMessageRegister.setTextFill(Color.web(colorRed));
            labelMessageRegister.setText("Confirm password not match!");
            return;
        }

        if (!isLoading) {
            isLoading = true;
            anchorPaneLoginRegister.setDisable(true);

            progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

            anchorPaneLoginRegister.getChildren().add(progressIndicator);

            AnchorPane.setTopAnchor(progressIndicator, (anchorPaneLoginRegister.getHeight() - 52.8) / 2);
            AnchorPane.setLeftAnchor(progressIndicator, (anchorPaneLoginRegister.getWidth() - 52) / 2);

            // Създаваме Task за изпълнение на calculateMethod()
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    long startTime = System.nanoTime();
                    // Login Method
                    isRegister = validateRegister();
                    long endTime = System.nanoTime();
                    long elapsedTimeInNanos = endTime - startTime;
                    double elapsedTimeInSeconds = elapsedTimeInNanos / 1_000_000_000.0;

                    // Обновяваме напредъка на ProgressIndicator във визуалния нишков контекст
                    Platform.runLater(() -> progressIndicator.setProgress(elapsedTimeInSeconds));

                    Platform.runLater(() -> {
                        isLoading = false;
                        anchorPaneLoginRegister.setDisable(false);
                        anchorPaneLoginRegister.getChildren().remove(progressIndicator);

                        if (isRegister){
                            labelMessageRegister.setStyle(successfulLabelStyle);
                            labelMessageRegister.setTextFill(Color.web(colorGreen));
                            labelMessageRegister.setText("Successfully Sign Up!");
                            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
                            pauseTransition.setOnFinished(event -> {
                                try {
                                    SceneController.getLoginScene(actionEvent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            pauseTransition.play();
                        }
                        else{
                            labelMessageRegister.setStyle(unsuccessfulLabelStyle);
                            labelMessageRegister.setTextFill(Color.web(colorRed));
                            labelMessageRegister.setText("Username has already been taken!");
                        }
                    });
                    return null;
                }
            };

            // Стартираме Task-a в отделна нишка
            new Thread(task).start();
        }
    }

    @FXML
    public void hyperlinkForgotPasswordAction(ActionEvent actionEvent) {
        BoxBlur blur = new BoxBlur(4, 4, 4);

        VBox vBoxHeader = new VBox();
        vBoxHeader.setAlignment(Pos.CENTER);
        vBoxHeader.setSpacing(20);


        VBox vboxBody = new VBox();
        vboxBody.setAlignment(Pos.CENTER);
        vboxBody.setSpacing(30);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXDialog dialog = new JFXDialog();

        JFXButton button = new JFXButton("Reset password");

        JFXTextField textField = new JFXTextField();
        textField.setFocusColor(Color.DIMGRAY);
        textField.setUnFocusColor(Color.WHITE);
        textField.getStyleClass().add("forgot-dialog-textField");
        textField.getStyleClass().add("prompt-text");
        textField.setLabelFloat(true);
        textField.setPromptText("Enter your email");

        Label labelBody = new Label("No worries, we'll send you reset instructions. ");
        labelBody.setTextFill(Color.WHITE);

        Label labelHeader = new Label("Forgot Password?");
        labelHeader.setTextFill(Color.WHITE);
        labelHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        vboxBody.getChildren().addAll(labelBody, textField, button);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> dialog.close());

        button.setTextFill(Color.WHITE);
        button.getStyleClass().add("forgot-dialog-button");

        vBoxHeader.getChildren().addAll(labelHeader);


        dialogLayout.setHeading(vBoxHeader);

        dialogLayout.setBody(vboxBody);

        dialogLayout.getStyleClass().add("forgot-dialog");

        dialog.setContent(dialogLayout);

        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);

        dialog.setDialogContainer(stackPaneLRForm);

        dialog.show();

        dialog.setOnDialogClosed((JFXDialogEvent event1) -> anchorPaneLoginRegisterForm.setEffect(null));

        anchorPaneLoginRegisterForm.setEffect(blur);


    }

    private void buttonClose(){
        buttonClose.setOnAction(SceneController::close);
    }

    private void buttonMinimize(){
        buttonMinimize.setOnAction(SceneController::minimize);
    }

    private boolean validFieldsLogin(){
        return !textFieldUsernameLogin.getText().isEmpty() && !passwordFieldPasswordLogin.getText().isEmpty();
    }

    private boolean validFieldsRegister(){
        return !textFieldUsernameRegister.getText().isEmpty() && !passwordFieldPasswordRegister.getText().isEmpty() && !passwordFieldConfirmPasswordRegister.getText().isEmpty();
    }

    private boolean checkConfirmPassword(){
        return passwordFieldPasswordRegister.getText().equals(passwordFieldConfirmPasswordRegister.getText());
    }

    private boolean validateLogin(){
        User user = userService.login(textFieldUsernameLogin.getText(), passwordFieldPasswordLogin.getText());

        if (user == null){

            return false;
        }
        else{

            String username = textFieldUsernameLogin.getText();
            String password = passwordFieldPasswordLogin.getText();
            boolean rememberMe = checkBoxRememberMe.isSelected();
            if (rememberMe){
                savePreferences(username, password, rememberMe);
            }
            else{
                savePreferences("","", false);
            }

            CurrentUser.setUser(user);

            return true;
        }
    }

    private boolean validateRegister(){

        return userService.register(textFieldUsernameRegister.getText(), passwordFieldPasswordRegister.getText());
    }

    private void clearLogin(){
        textFieldUsernameLogin.clear();
        passwordFieldPasswordLogin.clear();
        checkBoxRememberMe.setSelected(false);
        hyperlinkForgotPassword.setVisited(false);
        labelMessageLogin.getStyleClass().clear();
        labelMessageLogin.setStyle(null);
        labelMessageLogin.setText("");

    }

    private void clearRegister(){
        textFieldUsernameRegister.clear();
        passwordFieldPasswordRegister.clear();
        passwordFieldConfirmPasswordRegister.clear();
        labelMessageRegister.getStyleClass().clear();
        labelMessageRegister.setStyle(null);
        labelMessageRegister.setText("");
    }

    private void savePreferences(String username, String password, boolean rememberMe) {
        preferences.put(USERNAME_PREF_KEY, username);
        preferences.put(PASSWORD_PREF_KEY, password);
        preferences.putBoolean(REMEMBER_ME_PREF_KEY, rememberMe);
    }


}
