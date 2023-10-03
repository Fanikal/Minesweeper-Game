package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MineSweeperGame extends Application {

    //size of grid
    private int GRID_SIZE = 30;
    //number of mines in the grid
    private int NUM_MINES = 15;
    // string to store difficulty
    private String dif;
    private GridPane grid = new GridPane();

    private int openedCellsCount = 0;
    private int flaggedMinesCount = 0;
    private boolean gameEnded = false;

    public MineSweeperGame(String difficulty) {
        if ("Easy".equals(difficulty)) {
            GRID_SIZE = 8;
            NUM_MINES = 8;
            dif = "Easy";
        } else if ("Medium".equals(difficulty)) {
            GRID_SIZE = 10;
            NUM_MINES = 10;
            dif = "Medium";
        } else if ("Hard".equals(difficulty)) {
            GRID_SIZE = 11;
            NUM_MINES = 15;
            dif = "Hard";
        } else {
            // Default values if no valid difficulty is provided
            GRID_SIZE = 8;
            NUM_MINES = 5;
            dif = "Easy";
        }
        openedCellsCount = 0;
        flaggedMinesCount = 0;
        gameEnded = false;
    }

    private Button[][] cells = new Button[GRID_SIZE][GRID_SIZE];
    private boolean[][] mines = new boolean[GRID_SIZE][GRID_SIZE];


    public static void main(String[] args)
    {
        //Launch of the application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // title of the stage
        primaryStage.setTitle("Minesweeper");

        // border pane that will hold the content
        BorderPane gameLayout = new BorderPane();

        // Load the custom font (GILSANUB)
        Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 36);

        // We initialize the User Interface (UI) of the application
        initGUI(gameLayout, primaryStage);

        //Creation of the scene that contains gameLayout as a root pane
        Scene scene = new Scene(gameLayout, 1000, 800);

        // Load the CSS file
        scene.getStylesheets().add
                (MineSweeperGame.class.getResource("mycss.css").toExternalForm());

        // We display the scene we just created in the stage
        primaryStage.setScene(scene);

        // We show the scene
        primaryStage.show();
    }

    private void initGUI(BorderPane gameLayout, Stage primaryStage) {

        // Top section - title
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 10, 0, 10));

        //Add label
        Label titleLabel = new Label("Fani's Minesweeper");
        titleLabel.setFont(Font.font("Gill Sans Ultra Bold", 35)); // Use the custom font
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #302c2c;");
        //titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //Add titleBox to gameLayout
        titleBox.getChildren().add(titleLabel);
        gameLayout.setTop(titleBox);

        // Top Section - Status
        HBox statusBox = new HBox();
        statusBox.setSpacing(14);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPadding(new Insets(10, 10, 10, 10));

        // Status - remaining time box
        HBox timeBox = new HBox();
        timeBox.setAlignment(Pos.CENTER_RIGHT);
        Image clockImage = new Image(getClass().getResourceAsStream("time_limit.png"));
        ImageView clockImageView = new ImageView(clockImage);
        clockImageView.setFitHeight(30);
        clockImageView.setPreserveRatio(true);
        Label timeLabel = new Label(" Remaining Time:");
        Label remainingTimeLabel = new Label(" 00:00");
        timeLabel.getStyleClass().add("status-text");
        remainingTimeLabel.getStyleClass().add("status-text");
        timeBox.getChildren().addAll(clockImageView, timeLabel, remainingTimeLabel);

        // Status - Opened/Total Cells
        HBox cellsBox = new HBox();
        cellsBox.setAlignment(Pos.CENTER_RIGHT);
        Image cellsImage = new Image(getClass().getResourceAsStream("open.png"));
        ImageView cellsImageView = new ImageView(cellsImage);
        cellsImageView.setFitHeight(30);
        cellsImageView.setPreserveRatio(true);
        Label cellsLabel = new Label(" Opened/All Cells:");
        Label openedCellsLabel = new Label(" 0/0");
        cellsLabel.getStyleClass().add("status-text");
        openedCellsLabel.getStyleClass().add("status-text");
        cellsBox.getChildren().addAll(cellsImageView, cellsLabel, openedCellsLabel);

        // Status - Unflagged/Total Bombs
        HBox minesBox = new HBox();
        minesBox.setAlignment(Pos.CENTER_RIGHT);
        Image mineImage = new Image(getClass().getResourceAsStream("flag.png"));
        ImageView mineImageView = new ImageView(mineImage);
        mineImageView.setFitHeight(30);
        mineImageView.setPreserveRatio(true);
        Label minesLabel = new Label(" Unflagged/All Mines:");
        Label mineCountLabel = new Label(" 0/0");
        minesLabel.getStyleClass().add("status-text");
        mineCountLabel.getStyleClass().add("status-text");
        minesBox.getChildren().addAll(mineImageView, minesLabel, mineCountLabel);

        // Center section - grid
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);

        // Add all status elements to status box and fix alignment
        statusBox.getChildren().addAll(timeBox, cellsBox, minesBox);
        centerBox.getChildren().addAll(statusBox, grid);
        grid.setAlignment(Pos.CENTER);
        gameLayout.setCenter(centerBox);

        // VBox for record Button and toggle Button
        HBox recordPercentagesBox = new HBox(10);
        recordPercentagesBox.setAlignment(Pos.CENTER);

        // Voice record button
        Button recordButton = new Button("Record");
        Image recordIcon = new Image(getClass().getResourceAsStream("record.png"));
        ImageView recordImageView = new ImageView(recordIcon);
        recordImageView.setFitHeight(20);
        recordImageView.setPreserveRatio(true);
        recordButton.setGraphic(recordImageView);

        // Show percentages button
        Button percentagesButton = new Button("Show percentages");
        recordButton.getStyleClass().add("record-perc-button");
        percentagesButton.getStyleClass().add("record-perc-button");

        // Add the Voice Record button and Show Percentages button to the VBox
        recordPercentagesBox.getChildren().addAll(recordButton, percentagesButton);

        // Restart and exit buttons box
        HBox buttonsRestartExitBox = new HBox();
        buttonsRestartExitBox.setSpacing(10);
        buttonsRestartExitBox.setAlignment(Pos.CENTER);
        buttonsRestartExitBox.setPadding(new Insets(10, 10, 10, 10));

        // Restart and exit buttons
        Button restartButton = new Button("Restart");
        Button exitButton = new Button("Exit");
        restartButton.setPrefWidth(160);
        exitButton.setPrefWidth(160);
        exitButton.getStyleClass().add("main-button");
        restartButton.getStyleClass().add("main-button");
        buttonsRestartExitBox.getChildren().addAll(restartButton, exitButton);

        // VBox to contain both sets of buttons
        VBox bottomButtonsBox = new VBox(10);
        bottomButtonsBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomButtonsBox.getChildren().addAll(recordPercentagesBox, buttonsRestartExitBox);

        // Set the bottom section of the BorderPane to contain both sets of buttons
        gameLayout.setBottom(bottomButtonsBox);

        // Set margins
        BorderPane.setMargin(centerBox, new Insets(0, 0, 0, 0));
        VBox.setMargin(grid, new Insets(0, 0, 0, 0));

        // Restart - exit button actions
        restartButton.setOnAction(e -> {
            try {
                openDifficulty(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        exitButton.setOnAction(e -> {
            // Exit the app
            Platform.exit();
        });

        //Load bomb image
        Image bombImage = new Image(getClass().getResourceAsStream("malware.png"));
        ImageView bombImageView = new ImageView(bombImage);
        bombImageView.setFitHeight(30); // Set the desired height
        bombImageView.setPreserveRatio(true); // Maintain the aspect ratio
        bombImageView.setVisible(false);

        //Load red flag image
        Image redFlag = new Image(getClass().getResourceAsStream("red-flag.png"));
        ImageView redFlagImageView = new ImageView(redFlag);

        // calling of the method that generates the mines
        generateMines();

        // Build grid
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new Button();
                cells[row][col].setMinSize(45, 45);
                cells[row][col].getStyleClass().add("cell-button");
                int finalRow = row;
                int finalCol = col;
                cells[row][col].setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        // Right-click detected, set the flag icon
                        ImageView flagImageView = new ImageView(redFlag);
                        flagImageView.setFitHeight(8);
                        flagImageView.setPreserveRatio(true);
                        cells[finalRow][finalCol].setGraphic(flagImageView);
                        cells[finalRow][finalCol].getStyleClass().add("flagged");

                        // increase the count of flagged cells
                        flaggedMinesCount++;

                        if (flaggedMinesCount == NUM_MINES) {
                            gameEnded = true;
                            showWinScreen(primaryStage);
                        }
                    } else {
                        // Handle left-click
                        handleCellClick(finalRow, finalCol, bombImageView, primaryStage);
                    }
                });
                grid.add(cells[row][col], col, row);
            }
        }


        // center the grid better
        HBox.setMargin(centerBox, new Insets(0, 0, 0, 0));
    }


    private void generateMines() {
        int minesPlaced = 0;
        while (minesPlaced < NUM_MINES) {
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);

            if (!mines[row][col]) {
                mines[row][col] = true;
                minesPlaced++;
            }
        }
    }

    private void handleCellClick(int row, int col, ImageView bombImageView, Stage primaryStage) {

        if (gameEnded) {
            return;
        }

        if (mines[row][col]) {
            cells[row][col].setGraphic(bombImageView);
            bombImageView.setVisible(true);
            gameEnded = true;

            // Add a delay before showing the gameOverScreen
            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 1 second delay
            pause.setOnFinished(event -> showGameOverScreen(primaryStage));
            pause.play();
        } else {
            openedCellsCount++;

            // Check if the user has won
            if (openedCellsCount == (GRID_SIZE * GRID_SIZE - NUM_MINES) && flaggedMinesCount == NUM_MINES) {
                gameEnded = true;
                showWinScreen(primaryStage);
            }
            int adjacentMines = countAdjacentMines(row, col);
            cells[row][col].setText(Integer.toString(adjacentMines));
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        // Check in all adjacent cells
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;

                if (isValidCell(newRow, newCol) && mines[newRow][newCol]) {
                    count++;
                }
            }
        }

        return count;
    }

    private void showGameOverScreen(Stage primaryStage) {
        primaryStage.close(); //close the current screen
        //open the game over screen
        gameOverScreen gameOver = new gameOverScreen();
        gameOver.start(new Stage());
    }

    private void showWinScreen(Stage primaryStage) {
        primaryStage.close(); //close the current screen
        //open the winning screen
        winningScreen winScreen = new winningScreen();
        winScreen.start(new Stage());
    }

    private void openDifficulty(Stage primaryStage) throws Exception {
        primaryStage.close(); //close the current screen
        //open the difficulty screen
        difficultyScreen dScreen = new difficultyScreen();
        dScreen.start(new Stage());
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }
}
