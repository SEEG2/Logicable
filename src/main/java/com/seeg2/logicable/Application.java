package com.seeg2.logicable;

import com.seeg2.logicable.logger.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    public static final String GIT_HUB_PAGE_LINK = "https://github.com/seeg2/Logicable";
    public static final String VERSION = "PRE-RELEASE 1.0";
    public static final String APPLICATION_NAME = "Logicable";

    @Override
    public void start(Stage stage) throws IOException {
        Logger.info("Starting " + APPLICATION_NAME + " - Version: " + VERSION);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 540);
        stage.setOnCloseRequest((action) -> Application.close());
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.show();
        Logger.info("Window created");
    }

    public static void main(String[] args) {
        launch();
    }

    public static void close() {
        Logger.info("Program Closed");
        System.exit(0);
    }
}