package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

public class welcomeScreen extends Application {

    public static void main(String[] args) {
        // Launch of the application
        launch(args);
    }

        @Override
        public void start(Stage welcomeStage) {
            // Definition of the stage
            welcomeStage.setTitle("Minesweeper");

            // Definition of the root pane which will contain all the other elements
            FlowPane welcomeRoot = new FlowPane(Orientation.VERTICAL);
            welcomeRoot.setAlignment(Pos.CENTER);
            welcomeRoot.getStyleClass().add("welcomeRoot");

            // We set the height of the stage
            welcomeStage.setHeight(600);
            welcomeStage.setWidth(600);

            // Creation of the scene that contains root as a root pane
            Scene scene = new Scene(welcomeRoot, 600, 600);

            // Load the custom font (GILSANUB)
            Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 35);

            // VBox for the title
            VBox titlePane = new VBox();
            titlePane.setPadding(new Insets(30, 10, 10, 10));
            titlePane.setAlignment(Pos.CENTER);
            welcomeRoot.getChildren().add(titlePane);

            // Title text with the custom font
            Text titleText = new Text("Fani's Minesweeper");
            titleText.setFont(Font.font("Gill Sans Ultra Bold", 35));
            titleText.setStyle("-fx-font-weight: bold; -fx-fill: black;");

            // Add the text to the title VBox
            titlePane.getChildren().addAll(titleText);

            // VBox (empty for the moment)
            VBox buttonPane = new VBox();
            buttonPane.setPadding(new Insets(10, 10, 10, 10));
            buttonPane.setAlignment(Pos.CENTER);
            welcomeRoot.getChildren().add(buttonPane);

            // HBox for the buttons
            HBox paneForButtons = new HBox();
            paneForButtons.setAlignment(Pos.CENTER);
            paneForButtons.setSpacing(10);
            paneForButtons.setPadding(new Insets(10));

            // button for "Let's go"
            Button letsstartButton = new Button("Go");

            // Setting style of "Let's go" button
            letsstartButton.setPrefWidth(160);
            letsstartButton.getStyleClass().add("main-button");

            // Adding button to paneForButtons
            paneForButtons.getChildren().add(letsstartButton);

            //Adding pane to root
            welcomeRoot.getChildren().add(paneForButtons);

            // Handle event: click on "Let's go" button
            letsstartButton.setOnMouseClicked(e ->{
                // Creating the difficulty screen
                HowItWorksScreen howItWorks = new HowItWorksScreen();

                // Applying fade-out transition to the welcome screen
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), welcomeRoot);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event -> {
                    // Open the difficulty screen
                    try {
                        howItWorks.start(new Stage());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    // Close the welcome screen
                    welcomeStage.close();
                });

                // Play the fade-out transition
                fadeOut.play();
            });

            // We display the scene we just created in the stage
            welcomeStage.setScene(scene);

            //We show the stage
            welcomeStage.show();

            // Definition of css file
            scene.getStylesheets().add
                    (Objects.requireNonNull(welcomeScreen.class.getResource("mycss.css")).toExternalForm());
        }
}

