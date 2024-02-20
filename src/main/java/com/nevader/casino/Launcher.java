package com.nevader.casino;


import com.nevader.casino.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainMenu();
    }

    public static void main(String[] args) {
        try {
            // Load the OpenCV library
            System.load("C:\\Users\\Nevader\\Downloads\\opencv\\build\\java\\x64\\opencv_java480.dll");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }

        launch();
    }
}