package com.nevader.casino;


import com.nevader.casino.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

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