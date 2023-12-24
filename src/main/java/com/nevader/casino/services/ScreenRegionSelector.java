package com.nevader.casino.services;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenRegionSelector {

    private final ScreenshotService screenshotService;
    private final Stage primaryStage;
    private final Stage screemshotStage;
    private double initialX;
    private double initialY;

    public ScreenRegionSelector(Stage primaryStage) {
        this.screemshotStage = new Stage();
        this.screenshotService = new ScreenshotService();
        this.primaryStage = primaryStage;
    }

    public void startSelection() {


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
