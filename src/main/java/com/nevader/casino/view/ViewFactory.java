package com.nevader.casino.view;

import com.nevader.casino.controller.BaseController;
import com.nevader.casino.controller.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {


    public void initializeStage(BaseController controller) {

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
        stage.show();

    }


    public void showMainMenu() {

        BaseController controller = new MainMenuController(this, "MainMenu.fxml");
        initializeStage(controller);

    }

}
