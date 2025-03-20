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
    private static SceneElement payload;
    private static SceneElement selectedElement;
    private final static ArrayList<SceneElement> sceneElements = new ArrayList<>();
    private final static ArrayList<ConnectionLine> connections = new ArrayList<>();
    public static MainController instance;
    private static ConnectionPoint pickedConnection;
    private ConnectionLine connectionLineTemp;
    private double mouseX, mouseY;
    private static boolean isDebugMode;
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

            if (connectionLineTemp != null) {
                // Slight offset here to fix click detection
                connectionLineTemp.updatePos(mouseX-1, mouseY-1);
            }
        });

        screen.setOnKeyPressed(this::handleKeyPress);
    }

    @FXML
    public void clearScene() {
        for (SceneElement element : sceneElements) {
            element.remove();
        }

        sceneElements.clear();
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
        Application.close(0);
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
    public void INPUTClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new INPUTElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
    }

    @FXML
    public void OUTPUTClicked() {
        if (payload != null) {
            payload.remove();
        }
        payload = new OUTPUTElement(screen);
        payload.setPosition(mouseX, Math.min(bottomBar.getLayoutY() - payload.SPRITE.getFitHeight(), mouseY));
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
            sceneElements.add(payload);
            payload.SPRITE.toBack();
            payload.setActive();
            payload = null;
        }

        if (selectedElement != null) {;
            selectSimulationElement(null);
        }

        pickedConnection = null;

        if (connectionLineTemp != null) {
            connectionLineTemp.remove();
            connectionLineTemp = null;
        }
    }

    @FXML
    public void toggleDebugMode() {
        if (isDebugMode) {
            for (SceneElement element : sceneElements) {
                element.hideConnectionPoints();
            }
            if (payload != null) {
                payload.hideConnectionPoints();
            }

            for (ConnectionLine line : connections) {
                line.resetColor();
            }

            if (connectionLineTemp != null) {
                connectionLineTemp.resetColor();
            }
        } else {
            for (SceneElement element : sceneElements) {
                element.showConnectionPoints();
            }
            if (payload != null) {
                payload.showConnectionPoints();
            }

            for (ConnectionLine line : connections) {
                line.setDebugColor();
            }
            if (connectionLineTemp != null) {
                connectionLineTemp.setDebugColor();
            }
        }

        isDebugMode ^= true;
    }

    public static void removeSimulationElement(SceneElement element) {
        if (selectedElement == element) {
            selectedElement = null;
        }

        sceneElements.remove(element);
        element.remove();
    }

    public static void selectSimulationElement(SceneElement element) {
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

    public void setPickedConnection(ConnectionPoint connectionPoint, boolean invert) {
        if (pickedConnection == null) {
            pickedConnection = connectionPoint;
            connectionLineTemp = new ConnectionLine(pickedConnection, null, screen, true, invert);
            return;
        }

        if (connectionPoint.isInput() == pickedConnection.isInput()) {
            if (pickedConnection instanceof LineConnectionPoint) {
                pickedConnection.remove();
            }

            if (connectionPoint instanceof LineConnectionPoint) {
                connectionPoint.remove();
            }

            pickedConnection = null;


            if (connectionLineTemp != null) {
                connectionLineTemp.remove();
            }
            return;
        }

        if (pickedConnection.getRoot().equals(connectionPoint.getRoot())) {
            pickedConnection = connectionPoint;

            if (connectionLineTemp != null) {
                connectionLineTemp.remove();
            }

            connectionLineTemp = new ConnectionLine(pickedConnection, null, screen, invert);
            return;
        }

        if (pickedConnection.getConnection() != null) {
            pickedConnection.removeConnection();
        }

        if (connectionPoint.getConnection() != null) {
            connectionPoint.removeConnection();
        }

        if (connectionPoint instanceof LineConnectionPoint) {
            connectionLineTemp.setInvert(invert);
        }

        connectionLineTemp.setDestination(connectionPoint);
        connectionLineTemp.setToNotTemp();
        connections.add(connectionLineTemp);
        pickedConnection.setConnection(connectionLineTemp, connectionPoint);
        connectionPoint.setConnection(connectionLineTemp, pickedConnection);
        connectionLineTemp.updatePos();

        if (connectionPoint.isInput()) {
            pickedConnection.getRoot().pushValue();
        } else {
            connectionPoint.getRoot().pushValue();
        }

        connectionLineTemp = null;
        pickedConnection = null;
    }


    public boolean isConnectionPicked() {
        return pickedConnection != null;
    }

    public static boolean isDebugMode() {
        return isDebugMode;
    }
}