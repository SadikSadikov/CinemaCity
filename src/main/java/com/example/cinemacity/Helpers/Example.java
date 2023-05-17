package com.example.cinemacity.Helpers;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Example extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int BLUR_RADIUS = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the gif
        Image gifImage = new Image("file:///C:\\Users\\USER\\IdeaProjects\\CinemaCity\\Image\\successfullyPaid.gif");
        ImageView gifView = new ImageView(gifImage);

        // Create the payment button
        Button paymentButton = new Button("Make payment");
        paymentButton.setOnAction(e -> {

            // Create the blur effect
            BoxBlur blur = new BoxBlur(BLUR_RADIUS, BLUR_RADIUS, 3);

            // Create the fade transition for the gif
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), gifView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Create the fade transition for the button
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), paymentButton);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            // Create the pause transition for the gif
            PauseTransition pause = new PauseTransition(Duration.seconds(1));

            // Create the fade out transition for the gif
            FadeTransition fadeOutGif = new FadeTransition(Duration.millis(500), gifView);
            fadeOutGif.setFromValue(1);
            fadeOutGif.setToValue(0);

            // Create the fade in transition for the button
            FadeTransition fadeInButton = new FadeTransition(Duration.millis(500), paymentButton);
            fadeInButton.setFromValue(0);
            fadeInButton.setToValue(1);

            // Create the sequential transition
            SequentialTransition seqT = new SequentialTransition(fadeIn, pause, fadeOutGif, fadeInButton);
            seqT.setOnFinished(evt -> {
                // Reset the button
                paymentButton.setOpacity(1);
                paymentButton.setDisable(false);
            });

            // Disable the button and set the blur effect
            paymentButton.setDisable(true);
            paymentButton.setEffect(blur);

            // Play the animation
            seqT.play();
        });

        // Create the root pane
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(gifView, paymentButton);

        // Create the scene and show the stage
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
