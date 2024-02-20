package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.util.ArrayList;

public class RouletteController extends BaseController{

    @FXML
    private AnchorPane coloursGrid;

    private ArrayList<Rectangle> rectangles;

    public RouletteController(ViewFactory viewFactory, String fxmlName, ScreenshotService screenshotService, OCRProcessorService ocrProcessorService) {
        super(viewFactory, fxmlName, screenshotService, ocrProcessorService);
        this.rectangles = new ArrayList<>();
    }

    public void initialize() {

    }


}
