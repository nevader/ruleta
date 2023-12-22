package com.nevader.casino.controller;

import com.nevader.casino.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController extends BaseController {

    @FXML
    private Button test;

    public MainMenuController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }
}
