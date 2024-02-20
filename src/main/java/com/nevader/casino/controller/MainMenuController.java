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
import net.sourceforge.tess4j.Word;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        try {
            bufferedImage = ImageIO.read(getClass().getResource("test.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage filters = screenshotService.applyFilters(bufferedImage);

        showScreenshot(filters);
        System.out.println(filters.getWidth());
        System.out.println(filters.getHeight());

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
