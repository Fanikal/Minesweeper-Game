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

    private String timeSpent;
    private String timeLabelString;
    private Stage primaryStage;

    public winningScreen(String formattedTimeSpent) {
        this.timeSpent = formattedTimeSpent;

        if (timeSpent.contains(":")) {
            // Splitting the time into minutes and seconds
            String[] parts = timeSpent.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);

            // Determining the appropriate time label
            if (minutes > 0) {
                timeLabelString = "Time spent in game: " + minutes + " minute" + (minutes > 1 ? "s" : "");
                if (seconds > 0) {
                    timeLabelString += " and " + seconds + " second" + (seconds > 1 ? "s" : "");
                }
            } else {
                timeLabelString = "Time spent in game: " + seconds + " second" + (seconds > 1 ? "s" : "");
            }
        } else {
            timeLabelString = "Time spent in game: " + timeSpent + " seconds";
        }

    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Minesweeper");

        // VBox that will contain the content
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root  ");

        // Load the "congratulations" icon
        Image congratsIcon = new Image(winningScreen.class.getResourceAsStream("congratulation.png"));
        ImageView iconImageView = new ImageView(congratsIcon);
        iconImageView.setFitWidth(160);
        iconImageView.setFitHeight(160);

        // label with the congratulatory message
        Label congratsLabel = new Label("Congrats! You won!");
        congratsLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: black; -fx-font-weight: bold;");

        // label to display the time spent in the game
        String time = "10";
        Label timeLabel = new Label(timeLabelString);
        timeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");

        // Button to restart the game
        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("main-button");
        restartButton.setOnAction(e -> {
            try {
                // go back to the difficulty screen
                openDifficulty();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Button to exit the game
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("main-button");
        exitButton.setOnAction(e -> {
            // exit the app
            Platform.exit();
        });

        // Hbox for the buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // add buttons to the hbox I created
        buttonBox.getChildren().addAll(restartButton, exitButton);

        // Add elements to the VBox
        root.getChildren().addAll(iconImageView, congratsLabel, timeLabel, buttonBox);

        // Create the scene
        Scene scene = new Scene(root, 800, 600);

        // Load CSS
        scene.getStylesheets().add(winningScreen.class.getResource("mycss.css").toExternalForm());

        // We display the scene we just created in the stage
        primaryStage.setScene(scene);

        //We show the stage
        primaryStage.show();
    }

    // method to open the difficultyScreen
    private void openDifficulty() throws Exception {
        primaryStage.close(); // Close the win screen
        difficultyScreen dScreen = new difficultyScreen();
        dScreen.start(new Stage());
    }
}
