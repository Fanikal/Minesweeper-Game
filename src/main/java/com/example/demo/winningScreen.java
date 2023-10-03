package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class winningScreen extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Minesweeper");


        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root  ");

        // Load the "congratulations" icon
        Image congratsIcon = new Image(winningScreen.class.getResourceAsStream("congratulation.png"));
        ImageView iconImageView = new ImageView(congratsIcon);
        iconImageView.setFitWidth(160);
        iconImageView.setFitHeight(160);

        // Create a label with the congratulatory message
        Label congratsLabel = new Label("Congrats! You won!");
        congratsLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Create a label to display the time spent in the game (replace "10" with the actual time)
        String time = "10";
        Label timeLabel = new Label("Time spent in game: " + time + " seconds");
        timeLabel.setStyle("-fx-font-size: 19px;");

        // Button to restart the game
        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("main-button");
        restartButton.setOnAction(e -> {
            try {
                openDifficulty();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Button to exit the game
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("main-button");
        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        // Hbox for the buttons
        HBox buttonBox = new HBox(20); // Adjust the spacing as needed
        buttonBox.setAlignment(Pos.CENTER);

        // add buttons to the hbox I created
        buttonBox.getChildren().addAll(restartButton, exitButton);

        // Add elements to the VBox
        root.getChildren().addAll(iconImageView, congratsLabel, timeLabel, buttonBox);

        // Create the scene
        Scene scene = new Scene(root, 800, 600);

        // Load CSS
        scene.getStylesheets().add(winningScreen.class.getResource("mycss.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openDifficulty() throws Exception {
        primaryStage.close(); // Close the win screen
        difficultyScreen dScreen = new difficultyScreen();
        dScreen.start(new Stage());
    }
}
