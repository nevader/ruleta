module com.nevader.casino {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires tess4j;
    requires java.logging;


    exports com.nevader.casino.view;
    exports com.nevader.casino.controller;

    opens com.nevader.casino.view to javafx.fxml;
    opens com.nevader.casino.controller to javafx.fxml;
    exports com.nevader.casino;
    opens com.nevader.casino to javafx.fxml;
}