package com.seeg2.logicable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    public static final String GIT_HUB_PAGE_LINK = "https://github.com/seeg2/Logicable";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 540);
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setTitle("Logicable");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void close() {
        System.exit(0);
    }
}