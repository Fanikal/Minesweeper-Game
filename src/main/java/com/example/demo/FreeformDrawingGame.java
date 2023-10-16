package com.example.demo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class FreeformDrawingGame extends Application {

    private Pane drawingPane;
    private Path currentPath;
    private boolean isDrawing = false;
    private Circle pointA, pointB, pointC;
    private int connectedPoints = 0;
    private boolean gameCompleted = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connect Points Game");

        drawingPane = new Pane();
        drawingPane.setOnMousePressed(this::handleMousePressed);
        drawingPane.setOnMouseDragged(this::handleMouseDragged);
        drawingPane.setOnMouseReleased(this::handleMouseReleased);

        // Creating three points (A, B, and C) as circles on the canvas
        pointA = createPoint(100, 100);
        pointB = createPoint(400, 400);
        pointC = createPoint(700, 100);

        Scene scene = new Scene(drawingPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Circle createPoint(double x, double y) {
        Circle point = new Circle(x, y, 10);
        point.setFill(Color.RED);
        drawingPane.getChildren().add(point);
        return point;
    }

    private void handleMousePressed(MouseEvent event) {
        if (!isDrawing) {
            currentPath = new Path();
            currentPath.setStroke(Color.BLACK);
            currentPath.setStrokeWidth(2);
            currentPath.getElements().add(new MoveTo(event.getX(), event.getY()));
            drawingPane.getChildren().add(currentPath);
            isDrawing = true;
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isDrawing) {
            currentPath.getElements().add(new LineTo(event.getX(), event.getY()));
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isDrawing) {
            if (isCloseTo(pointA, currentPath)) {
                connectedPoints++;
                pointA.setFill(Color.GREEN);
            }
            if (isCloseTo(pointB, currentPath)) {
                connectedPoints++;
                pointB.setFill(Color.GREEN);
            }
            if (isCloseTo(pointC, currentPath)) {
                connectedPoints++;
                pointC.setFill(Color.GREEN);
            }

            // Checking if the user has connected all points
            if (connectedPoints == 3) {
                currentPath.setStroke(Color.GREEN);
                System.out.println("completed the drawing");
                gameCompleted = true;
            } else {
                currentPath.setStroke(Color.RED);
                System.out.println("didn't complete the drawing");
            }

            isDrawing = false;

            // Notify the game completion status
            notifyGameCompletion(gameCompleted);
        }
    }

    private Boolean notifyGameCompletion(boolean gameCompleted) {
        if (gameCompleted) {
            System.out.println("Success!");
            return true;
        } else {
            System.out.println("Drawing failed!");
            return false;
        }
    }

    private boolean isCloseTo(Circle point, Path path) {
        for (PathElement pathElement : path.getElements()) {
            if (pathElement instanceof LineTo) {
                LineTo lineTo = (LineTo) pathElement;
                double distance = Math.sqrt(Math.pow(point.getCenterX() - lineTo.getX(), 2)
                        + Math.pow(point.getCenterY() - lineTo.getY(), 2));
                if (distance < 20) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

