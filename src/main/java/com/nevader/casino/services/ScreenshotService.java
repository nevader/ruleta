package com.nevader.casino.services;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public BufferedImage takeScreenshot() {
        return robot.createScreenCapture(rectangle);
    }

    public Image bufferedImageToFx(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }



}
