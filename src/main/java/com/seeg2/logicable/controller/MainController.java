package com.seeg2.logicable.controller;

import com.seeg2.logicable.Application;
import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulationElement.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static GateElement payload;
    private static GateElement selectedElement;
    private final static ArrayList<GateElement> simulationElements = new ArrayList<>();
    private final static ArrayList<ConnectionLine> connections = new ArrayList<>();
    public static MainController instance;
    private static ConnectionPoint pickedConnection;
    private double mouseX, mouseY;
    private boolean isDebugMode;
    @FXML
    private AnchorPane screen;
    @FXML
    public ButtonBar bottomBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        screen.setOnMouseMoved(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            if (payload != null) {
                // Making sure that it doesn't hit the bottom-bar
                payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
            }

            // For some reason key presses are not registered if I do not manually call this (no idea why)
            screen.requestFocus();
        });

        screen.setOnKeyPressed(this::handleKeyPress);
    }

    @FXML
    public void clearScene() {
        for (GateElement element : simulationElements) {
            element.remove();
        }

        simulationElements.clear();
    }

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
    public void creditsClicked() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("credits.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setMinWidth(480);
        stage.setMinHeight(270);
        stage.setTitle("credits");
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
            payload.setActive();
            payload = null;
        }

        if (selectedElement != null) {;
            selectSimulationElement(null);
        }

        pickedConnection = null;
    }

    @FXML
    public void toggleDebugMode() {
        if (isDebugMode) {
            for (GateElement element : simulationElements) {
                element.hideConnectionPoints();
            }
        } else {
            for (GateElement element : simulationElements) {
                element.showConnectionPoints();
            }
        }

        isDebugMode ^= true;
    }

    public static void removeSimulationElement(GateElement element) {
        if (selectedElement == element) {
            selectedElement = null;
        }

        simulationElements.remove(element);
        element.remove();
    }

    public static void selectSimulationElement(GateElement element) {
        if (element == selectedElement) {
            return;
        }

        if (selectedElement != null) {
            selectedElement.deselect();
        }
        
        selectedElement = element;
    }

    private void handleKeyPress(KeyEvent event) {
        if (selectedElement != null) {
            if (event.getCode() == KeyCode.DELETE || (event.getCode() == KeyCode.D && event.isControlDown())) {
                removeSimulationElement(selectedElement);
            }
        }
    }

    public void setPickedConnection(ConnectionPoint connectionPoint) {
        if (pickedConnection == null) {
            pickedConnection = connectionPoint;
            return;
        }

        if (connectionPoint.isInput() == pickedConnection.isInput()) {
            pickedConnection = connectionPoint;
            return;
        }

        if (pickedConnection.getRoot().equals(connectionPoint.getRoot())) {
            pickedConnection = connectionPoint;
            return;
        }

        if (pickedConnection.getConnection() != null) {
            pickedConnection.removeConnection();
        }

        if (connectionPoint.getConnection() != null) {
            connectionPoint.removeConnection();
        }

        ConnectionLine connection = new ConnectionLine(pickedConnection, connectionPoint, screen);
        connections.add(connection);
        pickedConnection.setConnection(connection);
        connectionPoint.setConnection(connection);
        pickedConnection = null;
    }

    public static ConnectionPoint getPickedConnection() {
        return pickedConnection;
    }
}