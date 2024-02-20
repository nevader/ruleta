module com.nevader.casino {
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.fxml;
    requires java.desktop;
    requires tess4j;
    requires java.logging;
    requires org.json;
    requires opencv;


    exports com.nevader.casino.view;
    exports com.nevader.casino.controller;
    exports com.nevader.casino.services;
    exports com.nevader.casino;

    opens com.nevader.casino.view to javafx.fxml;
    opens com.nevader.casino.controller to javafx.fxml;
    opens com.nevader.casino.services to javafx.fxml;
    opens com.nevader.casino to javafx.fxml;
}