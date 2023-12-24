package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenRegionSelectorController extends BaseController {

    @FXML
    private ImageView image;

    private BufferedImage bufferedImage;
    private Rectangle rectangle;

    public ScreenRegionSelectorController(ViewFactory viewFactory, String fxmlName, ScreenshotService screenshotService, OCRProcessorService ocrProcessorService) {
        super(viewFactory, fxmlName, screenshotService, ocrProcessorService);
    }

    public void initialize() {
        setUpImage();
    }

    public void setUpImage() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> {
            Stage stage = (Stage) image.getScene().getWindow();
            stage.setIconified(true);
            bufferedImage = screenshotService.takeScreenshot();
            image.setImage(screenshotService.bufferedImageToFx(bufferedImage));
            image.setFitWidth(bufferedImage.getWidth());
            image.setFitHeight(bufferedImage.getHeight());
            stage.setIconified(false);
            stage.setMaximized(true);

            rectangle = new Rectangle();
            image.setOnMousePressed(this::handleMousePressed);
            image.setOnMouseDragged(this::handleMouseDragged);
            image.setOnMouseReleased(this::handleMouseReleased);

        });
        pause.play();
    }

    private void handleMouseReleased(MouseEvent event) {

        try {
            JSONObject jsonObject;
            Path path = Paths.get("coordinates.json");

            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                jsonObject = new JSONObject(content);
            } else {
                jsonObject = new JSONObject();
            }

            jsonObject.put("numbers-x", rectangle.getX());
            jsonObject.put("numbers-y", rectangle.getY());
            jsonObject.put("numbers-height", rectangle.getHeight());
            jsonObject.put("numbers-width", rectangle.getWidth());

            FileWriter fileWriter = new FileWriter("coordinates.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        viewFactory.closeStage((Stage) image.getScene().getWindow());

    }

    private void handleMousePressed(MouseEvent event) {
        // Create a new rectangle when the mouse is pressed

        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.RED);
        rectangle.setX(event.getX());
        rectangle.setY(event.getY());

        // Add mouse event handlers to the rectangle for resizing
        rectangle.setOnMouseDragged(this::handleMouseDragged);

        // Add the rectangle to the ImageView's parent
        ((Pane) image.getParent()).getChildren().add(rectangle);
    }

    private void handleMouseDragged(MouseEvent event) {
        // Update the rectangle's width and height as the mouse is dragged
        rectangle.setWidth(Math.abs(event.getX() - rectangle.getX()));
        rectangle.setHeight(Math.abs(event.getY() - rectangle.getY()));
        if (event.getX() < rectangle.getX()) {
            rectangle.setX(event.getX());
        }
        if (event.getY() < rectangle.getY()) {
            rectangle.setY(event.getY());
        }
    }
}