package com.nevader.casino;


import com.nevader.casino.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.opencv.core.Core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainMenu();
    }

    public static void main(String[] args) {

        launch();
    }
}