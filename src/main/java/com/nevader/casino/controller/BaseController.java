package com.nevader.casino.controller;

import com.nevader.casino.services.OCRProcessorService;
import com.nevader.casino.services.ScreenshotService;
import com.nevader.casino.view.ViewFactory;

public abstract class BaseController {

    protected ViewFactory viewFactory;
    protected String fxmlName;
    protected ScreenshotService screenshotService;
    protected OCRProcessorService ocrProcessorService;

    public BaseController(ViewFactory viewFactory, String fxmlName, ScreenshotService screenshotService, OCRProcessorService ocrProcessorService) {
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
        this.screenshotService = screenshotService;
        this.ocrProcessorService = ocrProcessorService;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}
