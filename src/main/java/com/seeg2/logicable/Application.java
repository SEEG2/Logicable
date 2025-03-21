package com.seeg2.logicable;

import com.seeg2.logicable.logger.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        stage.setOnCloseRequest((action) -> Application.close(0));
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setTitle(APPLICATION_NAME);
        try {
            stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toExternalForm())); // JavaFX does not support .ico files
        } catch (Exception e) {
            Logger.error("Failed to load application icon");
        }
        stage.setScene(scene);
        stage.show();
        Logger.info("Window created");

    }

    public static void main(String[] args) {
        launch();
    }

    public static void close(int exitCode) {
        Logger.info("Program closed with exit code: " + exitCode);
        System.exit(exitCode);
    }
}