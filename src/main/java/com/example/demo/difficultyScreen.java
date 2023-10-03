package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class difficultyScreen extends Application {

    private Stage primaryStage;
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    private Button startButton;

    public static void main(String[] args)
    {
        //Launch of the application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        // Definition of the root pane which will contain all the other elements
        FlowPane root = new FlowPane(Orientation.VERTICAL);
        root.setAlignment(Pos.CENTER);

        //Creation of the scene that contains root as a root pane
        Scene scene = new Scene(root, 800, 600);

        //Title of the stage
        primaryStage.setTitle("Minesweeper");

        // Load the custom font (GILSANUB)
        Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 36);

        //We set the height of the stage
        primaryStage.setHeight(600);
        //primaryStage.setMaxHeight(600);
        //primaryStage.setMinHeight(500);
        //We set the width of the stage
        primaryStage.setWidth(800);
        //primaryStage.setMaxWidth(800);
        //primaryStage.setMinWidth(700);

        // We display the scene we just created in the stage
        primaryStage.setScene(scene);

        // We display the stage
        primaryStage.show();

        // We initialize the User Interface (UI) of the application
        initGUI(root);

        // CSS styles
        scene.getStylesheets().add
                (difficultyScreen.class.getResource("mycss.css").toExternalForm());


    }

    private void initGUI(FlowPane root) {

        //VBox for the title
        VBox titlePane = new VBox();
        titlePane.setPadding(new Insets(21, 10, 10, 10));
        titlePane.setAlignment(Pos.CENTER);
        //titlePane.setMinHeight(75);
        //titlePane.setMinWidth(175);
        root.getChildren().add(titlePane);

        //VBox for the difficulty levels
        VBox levels = new VBox();
        levels.setPadding(new Insets(10, 10, 10, 10));
        levels.setMinHeight(75);
        levels.setMinWidth(175);
        root.getChildren().add(levels);

        //Define buttons for difficulty
        easyButton = new Button("Easy");
        mediumButton = new Button("Medium");
        hardButton = new Button("Hard");
        startButton = new Button("Start Game");

        //Vbox for the Start Game button
        VBox startPane = new VBox();
        startPane.setPadding(new Insets(10, 10, 5, 10));
        startPane.setMinHeight(75);
        startPane.setMinWidth(175);
        root.getChildren().add(startPane);
        // Define the style of the start button
        startButton.getStyleClass().add("main-button");

        // Create tooltips for the difficulty buttons
        Tooltip easyTooltip = new Tooltip("Easy: 8x8 grid, 8 mines, 2 minutes");
        Tooltip mediumTooltip = new Tooltip("Medium: 10x10 grid, 10 mines, 5 minutes");
        Tooltip hardTooltip = new Tooltip("Hard: 11x11 grid, 15 mines, 5 minutes");

        // Set the tooltips on the buttons
        easyButton.setTooltip(easyTooltip);
        mediumButton.setTooltip(mediumTooltip);
        hardButton.setTooltip(hardTooltip);

        // Define width, css id of buttons
        easyButton.setPrefWidth(170);
        mediumButton.setPrefWidth(170);
        hardButton.setPrefWidth(170);
        easyButton.getStyleClass().add("game-button");
        mediumButton.getStyleClass().add("game-button");
        hardButton.getStyleClass().add("game-button");

        //add the difficulty levels to the levels Vbox
        levels.setAlignment(Pos.CENTER);
        startPane.setAlignment(Pos.CENTER);
        levels.setSpacing(10);
        levels.getChildren().addAll(easyButton, mediumButton, hardButton);

        //add start button to startPane
        startPane.getChildren().add(startButton);

        //Define title of scene
        Text mainTitle = new Text("Select Difficulty");
        mainTitle.setFont(Font.font("Gill Sans Ultra Bold", 32)); // Use the custom font
        mainTitle.setStyle("-fx-fill: black");
        mainTitle.setTextAlignment(TextAlignment.CENTER);
        titlePane.getChildren().add(mainTitle);

        // When user clicks on a difficulty button: setOnAction events
        easyButton.setOnAction(event -> handleDifficultySelection(easyButton));
        mediumButton.setOnAction(event -> handleDifficultySelection(mediumButton));
        hardButton.setOnAction(event -> handleDifficultySelection(hardButton));

        // When user clicks on start button: open the game
        startButton.setOnMouseClicked(e -> openMineSweeperGame());

    }

    private void handleDifficultySelection(Button button) {
        // Remove ´game-button-clicked´ id from all buttons
        if (easyButton != null) {
            easyButton.getStyleClass().remove("game-button-clicked");
        }
        if (mediumButton != null) {
            mediumButton.getStyleClass().remove("game-button-clicked");
        }
        if (hardButton != null) {
            hardButton.getStyleClass().remove("game-button-clicked");
        }

        // Apply the "clicked" style to the current button
        if (button != null) {
            button.getStyleClass().add("game-button-clicked");
        }

        // Update the selected button
        if (button == easyButton) {
            easyButton = button;
        } else if (button == mediumButton) {
            mediumButton = button;
        } else if (button == hardButton) {
            hardButton = button;
        }
    }

    private void openMineSweeperGame() {
        String difficulty = "Easy";

        if (easyButton != null && easyButton.getStyleClass().contains("game-button-clicked")) {
            difficulty = "Easy";
        } else if (mediumButton != null && mediumButton.getStyleClass().contains("game-button-clicked")) {
            difficulty = "Medium";
        } else if (hardButton != null && hardButton.getStyleClass().contains("game-button-clicked")) {
            difficulty = "Hard";
        } else {
            // If the user hasn't selected a difficulty, display a popup
            showDifficultySelectionAlert();
            return; 
        }

        //open the game
        MineSweeperGame mineSweeperGame = new MineSweeperGame(difficulty);
        mineSweeperGame.start(new Stage());
        //close the current stage
        primaryStage.close();
    }

    //method that handles the alert popup, in case the user hasn't selected a difficulty
    private void showDifficultySelectionAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Difficulty Selection");
        alert.setHeaderText("Please select a level of difficulty.");
        alert.setContentText("You must choose a difficulty level before starting the game.");

        alert.showAndWait();
    }
}

