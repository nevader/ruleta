package com.nevader.casino.services;


import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ScreenRegionSelector {

    private final ScreenshotService screenshotService;
    private final Stage primaryStage;
    private final Stage screemshotStage;

    public ScreenRegionSelector(Stage primaryStage) {
        this.screemshotStage = new Stage();
        this.screenshotService = new ScreenshotService();
        this.primaryStage = primaryStage;
    }

    private void setupSelectionRectangle(Rectangle rect, Pane pane) {
        pane.setOnMousePressed(event -> {
            rect.setX(event.getX());
            rect.setY(event.getY());
            rect.setWidth(0);
            rect.setHeight(0);
        });

        pane.setOnMouseDragged(event -> {
            rect.setWidth(Math.abs(event.getX() - rect.getX()));
            rect.setHeight(Math.abs(event.getY() - rect.getY()));
            if (event.getX() < rect.getX()) {
                rect.setX(event.getX());
            }
            if (event.getY() < rect.getY()) {
                rect.setY(event.getY());
            }
        });
    }
}
