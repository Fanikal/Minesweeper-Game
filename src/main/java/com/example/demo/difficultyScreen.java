package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class difficultyScreen extends Application {

    private List<swipeImage> images = new ArrayList<swipeImage>();
    private int currentImageIndex = 0;
    private ImageView displayImageView = new ImageView();
    private Stage primaryStage;
    private double initialX;

    public static void main(String[] args) {
        // Launch of the application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Title of the stage
        primaryStage.setTitle("Minesweeper");

        // Definition of the root pane which will contain all the other elements
        FlowPane root = new FlowPane(Orientation.VERTICAL);
        root.setAlignment(Pos.CENTER);

        // Creation of the scene that contains root as a root pane
        Scene scene = new Scene(root, 800, 600);

        // Set the height and width of the stage
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);

        // Initialize images & add them to list
        images.add(new swipeImage("easy", "/com/example/demo/easy.png", "Easy: 8x8 grid, 8 mines, 2 minutes"));
        images.add(new swipeImage("medium", "/com/example/demo/medium.png", "Medium. 10x10 grid, 15 mines, 5 minutes"));
        images.add(new swipeImage("hard", "/com/example/demo/hard.png", "Hard. 12x12 grid, 20 mines, 2 minutes"));

        // Set up the initial level (planet) image
        displayImage(currentImageIndex);

        // keep track of the swiping movements
        displayImageView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            initialX = event.getSceneX();
            event.consume();
        });

        displayImageView.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            double deltaX = event.getSceneX() - initialX;
            if (deltaX > 50 && currentImageIndex > 0) {
                // Right swipe
                currentImageIndex--;
            } else if (deltaX < -50 && currentImageIndex < images.size() - 1) {
                // Left swipe
                currentImageIndex++;
            }

            // update of the displayed character
            displayImage(currentImageIndex);
            event.consume();
        });

        // VBox for the title
        VBox titlePane = new VBox();
        titlePane.setPadding(new Insets(0, 0, 0, 0));
        titlePane.setAlignment(Pos.CENTER);

        // Load the custom font (GILSANUB)
        Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 35);

        // VBox for the title "Select level"
        Text titleLabel = new Text("Select level");
        titleLabel.setFont(Font.font("Gill Sans Ultra Bold", 35));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-fill: black;");
        titlePane.getChildren().add(titleLabel);

        // VBox that contains the label and button under the image
        VBox labelAndButtonBox = new VBox(10);
        labelAndButtonBox.setAlignment(Pos.CENTER);

        // label for "Swipe to select level"
        Label underLabel = new Label("Swipe to select level");
        underLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: black; -fx-font-style: italic;");
        underLabel.setPadding(new Insets(0, 0, 20, 0));

        // button for "Let's go"
        Button letsstartButton = new Button("Let's go");
        letsstartButton.setStyle("-fx-font-size: 18px;");
        letsstartButton.getStyleClass().add("main-button");

        // add label and button to the VBox
        labelAndButtonBox.getChildren().addAll(underLabel, letsstartButton);

        // handle click on "Let's go" button
        letsstartButton.setOnAction(e -> {
            //retrieve the level of maze that should open in the next screen
            String selectedDifficulty = images.get(currentImageIndex).getDifficulty();
            // create the MineSweeperGame screen with the selected difficulty
            gameScreen game = new gameScreen(selectedDifficulty);

            // Apply fade-out transition to the difficulty screen
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                // Open the MineSweeperGame screen
                try {
                    game.start(new Stage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                // Close the difficulty screen
                primaryStage.close();
            });

            // Play the fade-out transition
            fadeOut.play();
        });

        // VBox that contains the planet image and the label/button VBox
        VBox planetBox = new VBox(0);
        planetBox.setAlignment(Pos.CENTER);
        planetBox.getChildren().addAll(displayImageView, labelAndButtonBox);
        root.getChildren().addAll(titlePane, planetBox);

        // We display the scene we just created in the stage
        primaryStage.setScene(scene);

        // Center the VBox containing the button
        root.setAlignment(Pos.CENTER);

        //We show the stage
        primaryStage.show();

        // CSS styles
        scene.getStylesheets().add(difficultyScreen.class.getResource("mycss.css").toExternalForm());
    }

    // method that handles the display of each image (planet)
    private void displayImage(int currentImageIndex) {
        if (currentImageIndex >= 0 && currentImageIndex < images.size()) {
            // show the correct image
            swipeImage currImage = images.get(currentImageIndex);
            String imageUrl = currImage.getImageUrl();
            Image image = new Image(Objects.requireNonNull(difficultyScreen.class.getResourceAsStream(imageUrl)));
            displayImageView.setFitHeight(200);
            displayImageView.setPreserveRatio(true);
            displayImageView.setImage(image);

            // Set tooltip on hover
            Tooltip tooltip = new Tooltip(currImage.getTooltipMessage());
            Tooltip.install(displayImageView, tooltip);
        }

    }

}
