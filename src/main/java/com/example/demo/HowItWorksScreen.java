package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class HowItWorksScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("How Does It Work?");

        FlowPane root = new FlowPane(Orientation.VERTICAL);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("welcomeRoot");

        stage.setHeight(600);
        stage.setWidth(600);

        Scene scene = new Scene(root, 600, 600);

        // Load the custom fonts
        Font.loadFont("C:\\WINDOWS\\FONTS\\GILSANUB.TTF", 35);
        Font.loadFont("C:\\WINDOWS\\FONTS\\TEMPSITC.TTF", 35);

        // VBox that holds the title
        VBox titlePane = new VBox();
        titlePane.setPadding(new Insets(30, 10, 10, 10));
        titlePane.setAlignment(Pos.CENTER);
        root.getChildren().add(titlePane);

        // title text
        Text titleText = new Text("How Does It Work?");
        titleText.setFont(Font.font("Gill Sans Ultra Bold", 35));
        titleText.setStyle("-fx-font-weight: bold; -fx-fill: black;");
        titlePane.getChildren().add(titleText);

        // VBOx that will hold all the text with the instructions
        VBox instructionsPane = new VBox();
        instructionsPane.setPadding(new Insets(10, 10, 10, 10));
        instructionsPane.setAlignment(Pos.CENTER);
        root.getChildren().add(instructionsPane);

        // Get icon
        Image iconImage = new Image(getClass().getResourceAsStream("bullet.png"));

        // Creation of the labels
        Label label1 = createLabelWithIcon("Left-click on cells to open.", iconImage);
        Label label2 = createLabelWithIcon("Right-click on a cell to flag.", iconImage);
        Label label3 = createLabelWithIcon("Click on 'Record' and say 'Show' in order for mines to be revealed (5 sec!).", iconImage);
        Label label4 = createLabelWithIcon("Click on 'Draw' button to play a game & gain 30 seconds.", iconImage);
        Label label5 = createLabelWithIcon("During the game, a trivia question will appear. You can gain 30 sec!", iconImage);

        label3.setWrapText(true);
        label4.setWrapText(true);
        label5.setWrapText(true);

        // Putting the labels in the instructions pane
        instructionsPane.getChildren().addAll(label1, label2, label3, label4, label5);

        // HBox for the button
        HBox buttonsPane = new HBox();
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.setSpacing(10);
        buttonsPane.setPadding(new Insets(10));

        // Let's go button
        Button letsGoButton = new Button("Let's go");
        letsGoButton.setPrefWidth(160);
        letsGoButton.getStyleClass().add("main-button");
        buttonsPane.getChildren().add(letsGoButton);

        // let's go button event -> opening difficultyScreen
        letsGoButton.setOnMouseClicked(e -> {
            // Return to the previous screen
            difficultyScreen difScreen = new difficultyScreen();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                try {
                    difScreen.start(new Stage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                stage.close();
            });
            fadeOut.play();
        });

        root.getChildren().add(buttonsPane);

        scene.getStylesheets().add(Objects.requireNonNull(HowItWorksScreen.class.getResource("mycss.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private Label createLabelWithIcon(String text, Image iconImage) {
        Label label = new Label(text);
        ImageView imageView = new ImageView(iconImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        label.setGraphic(imageView);
        label.setGraphicTextGap(10);
        label.setFont(Font.font("Tempus Sans ITC", 16));
        return label;
    }
}
