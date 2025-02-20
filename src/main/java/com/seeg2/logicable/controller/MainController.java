package com.seeg2.logicable.controller;

import com.seeg2.logicable.Application;
import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulationElement.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private SimulationElement payload;
    private final ArrayList<SimulationElement> simulationElements = new ArrayList<>();
    private double mouseX, mouseY;
    @FXML
    public AnchorPane screen;
    @FXML
    public ButtonBar bottomBar;

    @FXML
    public void aboutClicked() {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(Application.GIT_HUB_PAGE_LINK));
        } catch (Exception e) {
            Logger.error("Failed to open About-link");
        }
    }

    @FXML
    public void closeClicked() {
        Application.close();
    }

    @FXML
    public void showLogClicked() throws IOException {
        Stage stage = new Stage();
        LogController.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("log.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setMinWidth(480);
        stage.setMinHeight(270);
        stage.setTitle("Log");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void NOTClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new NOTElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void ANDClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new ANDElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void NANDClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new NANDElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void ORClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new ORElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void NORClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new NORElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void XORClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new XORElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void XNORClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new XNORElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void screenClicked() {
        if (payload != null) {
            simulationElements.add(payload);
            payload.SPRITE.toBack();
            payload = null;
        }
    }

    public void removeSimulationElement(SimulationElement element) {
        simulationElements.remove(element);
        element.remove();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        screen.setOnMouseMoved(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            if (payload != null) {
                // Making sure that it doesn't hit the bottom-bar
                payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
            }
        });
    }
}