package com.nevader.casino.view;

import com.nevader.casino.controller.*;
import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ViewFactory {

    private ScreenshotService screenshotService;
    private OCRProcessorService ocrProcessorService;

    public ViewFactory() {

        this.screenshotService = new ScreenshotService();
        this.ocrProcessorService = new OCRProcessorService();

        try {
            String content = new String(Files.readAllBytes(Paths.get("libsPaths.json")));
            JSONObject jsonObject = new JSONObject(content);

            String tess = jsonObject.getString("tesseractPath");
            String openCV = jsonObject.getString("openCVPath");

            if (!tess.isBlank() && !openCV.isBlank()) {
                System.load(openCV);
                ocrProcessorService.setTesseract(tess);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage initializeStage(BaseController controller) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);

        Parent parent = null;

        try {
            parent = fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);

        if (controller instanceof ScreenRegionSelectorController) {
            stage.initStyle(StageStyle.UNDECORATED);
        }

        stage.show();
        return stage;
    }

    public void closeStage(Stage stageToClose) {
        stageToClose.close();
    }


    public Stage showMainMenu() {

        BaseController controller = new MainMenuController(this, "MainMenu.fxml", this.screenshotService, this.ocrProcessorService);
        return initializeStage(controller);

    }

    public Stage showScreenRegionSelector() {

        BaseController controller = new ScreenRegionSelectorController(this, "ScreenRegionSlector.fxml", this.screenshotService, this.ocrProcessorService);
        return initializeStage(controller);

    }

    public Stage showRouletteController() {

        BaseController controller = new RouletteController(this, "Roulette.fxml", this.screenshotService, this.ocrProcessorService);
        return initializeStage(controller);

    }

    public Stage showSetup() {

        BaseController controller = new SetupController(this, "Setup.fxml", this.screenshotService, this.ocrProcessorService);
        return initializeStage(controller);

    }

}
