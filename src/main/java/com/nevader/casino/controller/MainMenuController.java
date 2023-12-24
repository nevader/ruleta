package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenRegionSelector;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    void button() {

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

        bufferedImage = screenshotService.takeScreenshot();
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
        showScreenshot(croppedImage);
        String ocr = ocrProcessorService.doOCR(croppedImage);
        ocrText.setText(ocr);



    }

    @FXML
    void set() {
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
