package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SetupController extends BaseController{

    @FXML
    private TextField opencvPath;

    @FXML
    private TextField tesseractPath;

    public SetupController(ViewFactory viewFactory, String fxmlName, ScreenshotService screenshotService, OCRProcessorService ocrProcessorService) {
        super(viewFactory, fxmlName, screenshotService, ocrProcessorService);
    }

    @FXML
    void backButton() {

    }

    @FXML
    void setOpenCV() {
        try {
            JSONObject jsonObject;
            Path path = Paths.get("libsPaths.json");

            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                jsonObject = new JSONObject(content);
            } else {
                jsonObject = new JSONObject();
            }

            String openCVPath = opencvPath.getCharacters().toString();
            String newone = openCVPath + "\\opencv_java480.dll";
            jsonObject.put("openCVPath", newone);

            try {
                // Load the OpenCV library
                System.load(newone);
            } catch (UnsatisfiedLinkError e) {
                e.printStackTrace();
            }


            FileWriter fileWriter = new FileWriter("libsPaths.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void setTesseractPath() {
        try {
            JSONObject jsonObject;
            Path path = Paths.get("libsPaths.json");

            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                jsonObject = new JSONObject(content);
            } else {
                jsonObject = new JSONObject();
            }

            jsonObject.put("tesseractPath", tesseractPath.getCharacters().toString());

            System.out.println(tesseractPath.getCharacters().toString());
            ocrProcessorService.setTesseract(tesseractPath.getCharacters().toString());

            FileWriter fileWriter = new FileWriter("libsPaths.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
