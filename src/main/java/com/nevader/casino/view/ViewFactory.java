package com.nevader.casino.view;

import com.nevader.casino.controller.BaseController;
import com.nevader.casino.controller.MainMenuController;
import com.nevader.casino.controller.RouletteController;
import com.nevader.casino.controller.ScreenRegionSelectorController;
import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ViewFactory {

    private ScreenshotService screenshotService;
    private OCRProcessorService ocrProcessorService;

    public ViewFactory() {

        this.screenshotService = new ScreenshotService();
        this.ocrProcessorService = new OCRProcessorService("C:\\Program Files\\Tesseract-OCR\\tessdata");
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

}
