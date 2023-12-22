package com.nevader.casino.services;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScreenshotService {

    private static final Logger LOGGER = Logger.getLogger(ScreenshotService.class.getName());

    private final Robot robot;
    private Rectangle rectangle;

    public ScreenshotService() {

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize Robot class", e);
            throw new RuntimeException("Failed to initialize Robot for Screenshot", e);
        }

        this.rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public void takeScreenshot() {

    }
}
