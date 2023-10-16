package com.example.demo;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class gameScreen extends Application {
    ////// STATIC INT VARIABLES //////
    private static int GRID_SIZE = 50;
    private static int NUM_MINES = 15;

    ////// STRING VARIABLES //////
    private String dif;

    ////// BOOLEAN VARIABLES //////
    private boolean bombsRevealed = false;
    private boolean gameEnded = false;
    private boolean triviaChallengeInProgress = false;
    private boolean drawingCompleted;
    private boolean timeIsUp;

    ////// INT VARIABLES //////
    private int openedCellsCount = 0;
    private int flaggedMinesCount = 0;
    private int buffer;
    private int totalTimeSeconds;
    private int remainingTimeSeconds;
    private int initialRemainingTimeSeconds;

    ////// TIMELINE VARIABLES //////
    private Timeline timeline;
    private Timeline countdownTimeline;

    ////// LABEL VARIABLES //////
    private Label remainingTimeLabel;
    private Label totalTimeLabel;
    private Label cellsLabel;
    private Label mineCountLabel;
    private Label cellsOpened;
    private Label flaggedCellsLabel;
    private Label countdownLabel;

    ////// IMAGE VARIABLES //////
    private Image clockImage;
    private ImageView clockImageView;
    private ImageView bombImageView;
    private ImageView redFlagImageView;
    private ImageView cellsImageView;
    private ImageView realRedFlagImageView;

    ////// BUTTON VARIABLES //////
    private Button recordButton;
    private Button drawButton;

    ////// SCHEDULED EXECUTOR SERVICE //////
    private ScheduledExecutorService scheduler;

    ////// INTEGER VARIABLES //////
    private int totalCells;
    private int totalMines;
    private int unflaggedMines;

    ////// OTHER VARIABLES //////
    private TranscriberDemo transcriberDemo;


    public gameScreen(String difficulty) {
        if ("easy".equals(difficulty)) {
            GRID_SIZE = 8;
            NUM_MINES = 8;
            dif = "Easy";
            totalTimeSeconds = 2 * 60; // 2 minutes
            totalCells = 8 * 8;
            totalMines = 8;
        } else if ("medium".equals(difficulty)) {
            GRID_SIZE = 10;
            NUM_MINES = 10;
            dif = "Medium";
            totalTimeSeconds = 5 * 60; // 5 minutes
            totalCells = 10 * 10;
            totalMines = 15;
        } else if ("hard".equals(difficulty)) {
            GRID_SIZE = 11;
            NUM_MINES = 15;
            dif = "Hard";
            totalTimeSeconds = 2 * 60; // 2 minutes
            totalCells = 12 * 12;
            totalMines = 20;
        }
        openedCellsCount = 0;
        flaggedMinesCount = 0;
        gameEnded = false;
        transcriberDemo = new TranscriberDemo();

        // set scheduler
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(this::handleTriviaChallenge, 10, TimeUnit.SECONDS);

        // set remaining time initially equal to total time
        remainingTimeSeconds = totalTimeSeconds;

        //set buffer to 0
        buffer = 0;

        // generate the mines
        generateMines();
    }

    // additional constructor in case the MineSweeperGame is run directly (not by being called from difficultyScreen)
    public gameScreen() {
        // default values if no difficulty is specified
        GRID_SIZE = 8;
        NUM_MINES = 5;
        dif = "Easy";
        totalTimeSeconds = 2 * 60; // 2 minutes
        totalCells = 8 * 8;
        totalMines = 8;
        openedCellsCount = 0;
        flaggedMinesCount = 0;
        gameEnded = false;
        transcriberDemo = new TranscriberDemo();

        // set scheduler
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(this::handleTriviaChallenge, 10, TimeUnit.SECONDS);

        // set remaining time initially equal to total time
        remainingTimeSeconds = totalTimeSeconds;

        //set buffer to 0
        buffer = 0;

        // generate the mines
        generateMines();
    }

    private Button[][] cells = new Button[GRID_SIZE][GRID_SIZE];
    private boolean[][] mines = new boolean[GRID_SIZE][GRID_SIZE];
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");

        int sceneWidth = 800;
        int sceneHeight = 600;

        if ("Medium".equals(dif)) {
            sceneWidth = 800;
            sceneHeight = 700;
        } else if ("Hard".equals(dif)) {
            sceneWidth = 800;
            sceneHeight = 750;
        }

        // Load the custom fonts
        Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 35);
        Font.loadFont("C:\\WINDOWS\\FONTS\\TEMPSITC.TTF", 35);

        ////////////////////IMAGES////////////////////////////////
        clockImage = new Image(getClass().getResourceAsStream("time_limit.png"));
        clockImageView = new ImageView(clockImage);
        clockImageView.setFitHeight(50);
        clockImageView.setPreserveRatio(true);
        Image cellsImage = new Image(getClass().getResourceAsStream("open.png"));
        cellsImageView = new ImageView(cellsImage);
        cellsImageView.setFitHeight(30);
        cellsImageView.setPreserveRatio(true);
        Image mineImage = new Image(getClass().getResourceAsStream("flag.png"));
        ImageView mineImageView = new ImageView(mineImage);
        mineImageView.setFitHeight(30);
        mineImageView.setPreserveRatio(true);
        Image recordIcon = new Image(getClass().getResourceAsStream("record.png"));
        ImageView recordImageView = new ImageView(recordIcon);
        recordImageView.setFitHeight(20);
        recordImageView.setPreserveRatio(true);
        Image drawIcon = new Image(getClass().getResourceAsStream("pencil.png"));
        ImageView drawImageView = new ImageView(drawIcon);
        drawImageView.setFitHeight(20);
        drawImageView.setPreserveRatio(true);
        Image redFlag = new Image(getClass().getResourceAsStream("destroyed-planet.png"));
        redFlagImageView = new ImageView(redFlag);
        redFlagImageView.setFitHeight(28);
        redFlagImageView.setPreserveRatio(true);
        Image realRedFlag = new Image(getClass().getResourceAsStream("flag.png"));
        realRedFlagImageView = new ImageView(redFlag);
        realRedFlagImageView.setFitHeight(28);
        realRedFlagImageView.setPreserveRatio(true);
        Image bombImage = new Image(getClass().getResourceAsStream("malware.png"));
        bombImageView = new ImageView(bombImage);
        bombImageView.setFitHeight(28); // Set the desired height
        bombImageView.setPreserveRatio(true); // Maintain the aspect ratio
        bombImageView.setVisible(false);

        // Title on top
        Text title = new Text("Minesweeper");
        title.setFont(Font.font("Gill Sans Ultra Bold", 35));
        title.setStyle("-fx-font-weight: bold; -fx-fill: black;");
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 20, 0));

        // grid for the Minesweeper
        GridPane gridPane = createMinesweeperGrid(primaryStage);

        // HBox for the Restart and Exit buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setTranslateX(-70);

        // Restart button
        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("main-button");

        // Exit button
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("main-button");

        // Add restart, exit buttons to the HBox
        buttonBox.getChildren().addAll(restartButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        // Record button
        recordButton = new Button("Record", recordImageView);
        recordButton.getStyleClass().add("record-draw-button");

        // Record button action
        recordButton.setOnAction(event -> {
            transcriberDemo.startVoiceRecognition(this);
        });

        // Draw button
        drawButton = new Button("Draw", drawImageView);
        drawButton.getStyleClass().add("record-draw-button");

        // Draw button action
        drawButton.setOnAction(event -> {
            boolean isGameCompleted = startDrawingGame(primaryStage);
            if (isGameCompleted) {
                // Add 30 seconds
            } else {
                // Continue game without adding time
            }
        });

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

        // VBox for the status pane
        VBox statusPane = new VBox(
                createRemainingTimeStatusPane(),
                createOpenedCellsStatusPane(),
                createMinesStatusPane(),
                createRecordStatusPane(),
                createDrawStatusPane()
        );
        statusPane.setPadding(new Insets(10));
        statusPane.setAlignment(Pos.CENTER);
        statusPane.setSpacing(10);
        statusPane.setTranslateY(-20);

        // top-level container
        BorderPane root = new BorderPane();
        root.setTop(titleBox);

        // GridPane to hold the Minesweeper grid and status pane
        GridPane mainGrid = new GridPane();
        mainGrid.setTranslateX(40);
        mainGrid.setHgap(10);
        mainGrid.add(gridPane, 0, 0);
        mainGrid.add(statusPane, 1, 0);
        mainGrid.add(buttonBox, 0, 1, 2, 1);

        // set mainGrid to the center
        root.setCenter(mainGrid);

        // display the scene
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load the CSS file
        scene.getStylesheets().add
                (gameScreen.class.getResource("gameScreen.css").toExternalForm());

        // Implement the countdown logic
        updateCountdown(primaryStage);
    }

    private void openDifficulty(Stage primaryStage) throws Exception {
        primaryStage.close(); //close the current screen
        //open the difficulty screen
        difficultyScreen dScreen = new difficultyScreen();
        dScreen.start(new Stage());
    }

    private void generateMines() {
        int minesPlaced = 0;
        while (minesPlaced < NUM_MINES) {
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);

            if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE && !mines[row][col]) {
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
            countdownTimeline.stop();

            // Add a delay before showing the gameOverScreen
            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 1 second delay
            pause.setOnFinished(event -> showGameOverScreen(primaryStage));
            pause.play();
        } else {
            if (!cells[row][col].isDisabled()) {
                // The cell hasn't been opened before, proceed to reveal it
                openedCellsCount++;
                cells[row][col].setDisable(true); // Disable the cell to prevent further clicks

                // Update the cellsOpened label
                cellsOpened.setText(openedCellsCount + "/" + totalCells);

                // Check if the user has won
                if (openedCellsCount == (GRID_SIZE * GRID_SIZE - NUM_MINES) && flaggedMinesCount == NUM_MINES) {
                    gameEnded = true;
                    countdownTimeline.stop();
                    showWinScreen(primaryStage);
                }

                int adjacentMines = countAdjacentMines(row, col);
                cells[row][col].setText(Integer.toString(adjacentMines));
            }
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

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }

    private void showWinScreen(Stage primaryStage) {
        primaryStage.close(); //closing the current screen

        int adjustedTotalTime = totalTimeSeconds + 30;
        int timeSpentInSeconds = adjustedTotalTime - remainingTimeSeconds;


        // Calculate remaining minutes and seconds
        int minutes = timeSpentInSeconds / 60;
        int seconds = timeSpentInSeconds % 60;

        // Convert the time spent to a formatted string
        String formattedTimeSpent = String.format("%02d:%02d", minutes, seconds);

        // Open the game over screen with the accurate time spent
        winningScreen winScreen = new winningScreen(formattedTimeSpent);
        winScreen.start(new Stage());
        
    }

    private void showGameOverScreen(Stage primaryStage) {
        int adjustedTotalTime = totalTimeSeconds;
        primaryStage.close(); //closing the current screen

        if (drawingCompleted){
            adjustedTotalTime = totalTimeSeconds + 30;
        }

        int timeSpentInSeconds = adjustedTotalTime - remainingTimeSeconds;


        // Calculate remaining minutes and seconds
        int minutes = timeSpentInSeconds / 60;
        int seconds = timeSpentInSeconds % 60;

        // Convert the time spent to a formatted string
        String formattedTimeSpent = String.format("%02d:%02d", minutes, seconds);

        // Open the game over screen with the accurate time spent
        gameOverScreen gameOver = new gameOverScreen(formattedTimeSpent, timeIsUp);
        gameOver.start(new Stage());

    }

    private GridPane createMinesweeperGrid(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new Button();
                cells[row][col].setMinSize(45, 45);
                cells[row][col].getStyleClass().add("cell-button");
                int finalRow = row;
                int finalCol = col;
                cells[row][col].setOnMouseClicked(event -> {
                    if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                        // Right-click detected, set the flag icon
                        cells[finalRow][finalCol].setGraphic(redFlagImageView);
                        cells[finalRow][finalCol].getStyleClass().add("flagged");

                        // Increase the count of flagged cells
                        flaggedMinesCount++;
                        flaggedCellsLabel.setText(flaggedMinesCount + "/" + totalMines);

                        // Increase the count of opened cells
                        openedCellsCount++;

                        // Update the cellsOpened label
                        cellsOpened.setText(openedCellsCount + "/" + totalCells);

                        if (flaggedMinesCount == NUM_MINES) {
                            gameEnded = true;
                            countdownTimeline.stop();
                            showWinScreen(primaryStage);
                        }
                    } else {
                        // Handle left-click
                        handleCellClick(finalRow, finalCol, bombImageView, primaryStage);
                    }
                });

                // Initialize the graphic with an empty ImageView
                cells[row][col].setGraphic(new ImageView()); // Initialize with an empty graphic
                gridPane.add(cells[row][col], col, row);
            }
        }
        return gridPane;
    }


    private AnchorPane createStatusPane(String labelText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-alignment: CENTER_LEFT;");

        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(label);

        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);

        return statusPane;
    }

    private AnchorPane createRemainingTimeStatusPane() {
        // HBox to hold the clockImageView and countdown timer
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        clockImageView.setFitHeight(40);
        clockImageView.setPreserveRatio(true);

        // label to display the countdown timer
        countdownLabel = new Label();
        countdownLabel.setFont(Font.font("Tempus Sans ITC", 16));
        countdownLabel.setStyle("-fx-font-weight: bold; -fx-fill: black;");

        // Add the clockImageView and countdownLabel to the timeBox
        timeBox.getChildren().addAll(clockImageView, countdownLabel);

        // AnchorPane to hold the timeBox
        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(timeBox);

        // Set the AnchorPane's top, left, bottom, and right anchors to zero
        AnchorPane.setTopAnchor(timeBox, 0.0);
        AnchorPane.setLeftAnchor(timeBox, 0.0);
        AnchorPane.setBottomAnchor(timeBox, 0.0);
        AnchorPane.setRightAnchor(timeBox, 0.0);

        return statusPane;
    }

    private AnchorPane createOpenedCellsStatusPane() {

        // HBox to hold the OpenedCells
        HBox openedCellsBox = new HBox(10);
        openedCellsBox.setAlignment(Pos.CENTER_LEFT);
        cellsImageView.setFitHeight(40);
        cellsImageView.setPreserveRatio(true);

        // label to display the opened cells count
        cellsOpened = new Label("0/" + totalCells); // Initialize with "0/X" where X is the total cells
        cellsOpened.setFont(Font.font("Tempus Sans ITC", 16));
        cellsOpened.setStyle("-fx-font-weight: bold; -fx-fill: black;");

        // Add the clockImageView and countdownLabel to the timeBox
        openedCellsBox.getChildren().addAll(cellsImageView, cellsOpened);

        // AnchorPane to hold the timeBox
        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(openedCellsBox);

        // Set the AnchorPane's top, left, bottom, and right anchors to zero
        AnchorPane.setTopAnchor(openedCellsBox, 0.0);
        AnchorPane.setLeftAnchor(openedCellsBox, 0.0);
        AnchorPane.setBottomAnchor(openedCellsBox, 0.0);
        AnchorPane.setRightAnchor(openedCellsBox, 0.0);

        return statusPane;
    }

    private AnchorPane createMinesStatusPane() {
        // HBox to hold the OpenedCells
        HBox flaggedCellsBox = new HBox(10);
        flaggedCellsBox.setPrefWidth(100);
        flaggedCellsBox.setAlignment(Pos.CENTER_LEFT);
        realRedFlagImageView.setFitHeight(40);
        realRedFlagImageView.setPreserveRatio(true);

        // label to display the opened cells count
        flaggedCellsLabel = new Label("0/" + totalMines);
        flaggedCellsLabel.setFont(Font.font("Tempus Sans ITC", 16));
        flaggedCellsBox.setStyle("-fx-font-weight: bold; -fx-fill: black;");

        // Add the clockImageView and countdownLabel to the timeBox
        flaggedCellsBox.getChildren().addAll(realRedFlagImageView, flaggedCellsLabel);

        // AnchorPane to hold the timeBox
        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(flaggedCellsBox);

        // Set the AnchorPane's top, left, bottom, and right anchors to zero
        AnchorPane.setTopAnchor(flaggedCellsBox, 0.0);
        AnchorPane.setLeftAnchor(flaggedCellsBox, 0.0);
        AnchorPane.setBottomAnchor(flaggedCellsBox, 0.0);
        AnchorPane.setRightAnchor(flaggedCellsBox, 0.0);

        return statusPane;

    }

    private AnchorPane createRecordStatusPane() {
        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(recordButton);

        AnchorPane.setTopAnchor(recordButton, 0.0);
        AnchorPane.setLeftAnchor(recordButton, 0.0);
        AnchorPane.setBottomAnchor(recordButton, 0.0);
        AnchorPane.setRightAnchor(recordButton, 0.0);

        return statusPane;
    }

    private AnchorPane createDrawStatusPane() {
        AnchorPane statusPane = new AnchorPane();
        statusPane.getChildren().addAll(drawButton);

        AnchorPane.setTopAnchor(drawButton, 0.0);
        AnchorPane.setLeftAnchor(drawButton, 0.0);
        AnchorPane.setBottomAnchor(drawButton, 0.0);
        AnchorPane.setRightAnchor(drawButton, 0.0);

        return statusPane;
    }

    private void updateCountdown(Stage primaryStage) {
        initialRemainingTimeSeconds = totalTimeSeconds;
        // Calculate the initial remaining time based on total time (difficulty-specific)
        final int[] initialRemainingTimeSeconds = {totalTimeSeconds};
        int remainingMinutes = initialRemainingTimeSeconds[0] / 60;
        int remainingSeconds = initialRemainingTimeSeconds[0] % 60;

        String initialTimeText = String.format("%02d:%02d", remainingMinutes, remainingSeconds);
        // Set the initial time text to the countdownLabel
        countdownLabel.setText(initialTimeText);

        // the timeline for the countdown timer
        countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (remainingTimeSeconds > 0) {
                        // Decrease the remaining time by 1 second
                        remainingTimeSeconds--;

                        // Calculate remaining minutes and seconds
                        int minutes = remainingTimeSeconds / 60;
                        int seconds = remainingTimeSeconds % 60;

                        // Update the countdown label
                        String timeText = String.format("%02d:%02d", minutes, seconds);
                        countdownLabel.setText(timeText);
                    } else {
                        // here I'm handling the time's up
                        countdownLabel.setText("00:00");
                        countdownTimeline.stop(); // stop the countdown timer
                        timeIsUp = true; // set the flag to indicate time's up
                        gameEnded = true;
                        countdownTimeline.stop();
                        showGameOverScreen(primaryStage);
                    }
                })
        );
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.play();
    }

    private void hideBombs() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (mines[row][col]) {
                    ImageView bombImageView = (ImageView) cells[row][col].getGraphic();
                    bombImageView.setVisible(false);
                }
            }
        }
    }

    private void revealBombAt(int row, int col) {
        Image bombImage = new Image(getClass().getResourceAsStream("malware.png"));
        ImageView bombImageView = new ImageView(bombImage);
        bombImageView.setFitHeight(30);
        bombImageView.setPreserveRatio(true);
        cells[row][col].setGraphic(bombImageView);

    }

    public void revealBombsFor5Seconds() {
        System.out.println("reached reveal bombs method");
        // flag to indicate that the bombs should be revealed
        bombsRevealed = true;

        // Iterate through the grid and reveal bombs
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (mines[row][col]) {
                    revealBombAt(row, col);
                }
            }
        }

        // Scheduled task to hide the bombs after 5 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Hide the bombs and update the UI
                hideBombs();
                bombsRevealed = false;
            }
        }, 5000);
    }

    private boolean startDrawingGame(Stage primaryStage) {
        FreeformDrawingGame drawingGame = new FreeformDrawingGame();
        Stage drawingGameStage = new Stage(StageStyle.UTILITY);

        // prompt for the user
        Label promptLabel = new Label("You have 5 seconds to connect the dots!");
        VBox promptBox = new VBox(promptLabel);
        promptBox.setAlignment(Pos.CENTER);

        // adding the prompt to the drawing game stage
        Scene drawingGameScene = new Scene(promptBox, 400, 100);
        drawingGameStage.setScene(drawingGameScene);

        // flag to track if the drawing game was completed successfully
        AtomicBoolean isCompleted = new AtomicBoolean(false);

        // timer to close the drawing game after 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            drawingGameStage.close();
        });
        pause.play();

        // starting drawing game
        drawingGame.start(drawingGameStage);

        drawingGameStage.setOnHidden(e -> {
            // Checking if the drawing game was successfully completed
            isCompleted.set(drawingGame.isGameCompleted());

            if (isCompleted.get()) {
                System.out.println("in example Class: completed the drawing");
                drawingCompleted = true;
                buffer = 30;
                remainingTimeSeconds = remainingTimeSeconds + 30;
            } else {
                System.out.println("in example Class: not complete the drawing");
            }
        });

        return false;
    }

    private void handleTriviaChallenge() {

        if (triviaChallengeInProgress || gameEnded) {
            return;
        }
        triviaChallengeInProgress = true;

        // Calculating the correct answer based on the current difficulty level
        int correctAnswer;
        if ("Easy".equals(dif)) {
            correctAnswer = 8 * 8;
        } else if ("Medium".equals(dif)) {
            correctAnswer = 10 * 10;
        } else if ("Hard".equals(dif)) {
            correctAnswer = 12 * 12;
        } else {
            // Default to the easy level if difficulty is not recognized
            correctAnswer = 8 * 8;
        }

        // Generate a reasonable wrong answer
        int wrongAnswer;
        do {
            wrongAnswer = correctAnswer + (int) (Math.random() * 10) - 5;
        } while (wrongAnswer == correctAnswer);

        // trivia question with the correct and wrong answers
        String triviaQuestion = "How many cells does the grid have?";
        String correctOption = "A. " + correctAnswer + " cells";
        String wrongOption = "B. " + wrongAnswer + " cells";

        if (!gameEnded) {
            Platform.runLater(() -> {
                // Present the question to the user
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Trivia Challenge");
                alert.setHeaderText("Answer a trivia question to reveal mines:");
                alert.setContentText(triviaQuestion);

                ButtonType buttonA = new ButtonType(correctOption, ButtonBar.ButtonData.LEFT);
                ButtonType buttonB = new ButtonType(wrongOption, ButtonBar.ButtonData.LEFT);

                alert.getButtonTypes().setAll(buttonA, buttonB);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == buttonA) {
                        // correct answer, so reveal mines
                        revealBombsFor5Seconds();
                        bombsRevealed = true;
                    } else if (result.get() == buttonB) {
                        // user chose the wrong answer, do nothing
                    }
                }
            });
        }
    }

}

