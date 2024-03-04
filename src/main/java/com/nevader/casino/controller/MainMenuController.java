package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenuController extends BaseController {

    @FXML
    private ImageView screenshot;

    @FXML
    private TextArea ocrText;

    public MainMenuController(ViewFactory viewFactory, String fxmlName, ScreenshotService screenshotService, OCRProcessorService ocrProcessorService) {
        super(viewFactory, fxmlName, screenshotService, ocrProcessorService);
    }

    @FXML
    void readData() {

        BufferedImage bufferedImage;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        try {
            String content = new String(Files.readAllBytes(Paths.get("coordinates.json")));
            JSONObject jsonObject = new JSONObject(content);

            x = jsonObject.getInt("numbers-x");
            y = jsonObject.getInt("numbers-y");
            width = jsonObject.getInt("numbers-width");
            height = jsonObject.getInt("numbers-height");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Rectangle rectangle = new Rectangle(x, y, width, height);

        bufferedImage = screenshotService.takeScreenshot(rectangle);

        BufferedImage filters = screenshotService.applyFilters(bufferedImage);

        showScreenshot(filters);

        String numbers = ocrProcessorService.doOCR(filters);

        StringBuilder stringBuilder = new StringBuilder();

        String[] words = numbers.split(" ");

        for (String word : words) {
            stringBuilder.append(word);
            stringBuilder.append("\n");
        }

        ocrText.setText(stringBuilder.toString());



    }

    @FXML
    void setup() {
        viewFactory.showSetup();
    }

    @FXML
    void setRegion() {
        Stage stage = (Stage) ocrText.getScene().getWindow();
        stage.setIconified(true);
        Stage newStage = viewFactory.showScreenRegionSelector();

        newStage.setOnHidden(event -> {
            stage.setIconified(false);
        });
    }


    public void showScreenshot(BufferedImage bufferedImage) {
        screenshot.setImage(screenshotService.bufferedImageToFx(bufferedImage));
    }


}
