package com.seeg2.logicable.controller;

import com.seeg2.logicable.Application;
import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulationElement.*;
import com.seeg2.logicable.simulationElement.simulationElement.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private static SimulationElement payload;
    private static SceneElement selectedElement;
    private static boolean snapToGrid;
    private final static ArrayList<SceneElement> sceneElements = new ArrayList<>();
    private final static ArrayList<ConnectionLine> connections = new ArrayList<>();
    private final static ArrayList<Line> gridLines = new ArrayList<>();
    public static MainController instance;
    private ConnectionPoint pickedConnection;
    private ConnectionLine connectionLineTemp;
    public static final int gridSize = 30;
    private double mouseX, mouseY;
    private static boolean isDebugMode;
    private ContextMenu contextMenu;
    @FXML
    private AnchorPane screen;
    @FXML
    private AnchorPane root;
    @FXML
    public ButtonBar bottomBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        screen.prefHeightProperty().bind(root.heightProperty().subtract(bottomBar.heightProperty()));
        screen.prefWidthProperty().bind(root.widthProperty());

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

        initContextMenu();
        initGridLines();
    }

    @FXML
    public void clearScene() {
        for (SceneElement element : sceneElements) {
            element.remove();
        }

        sceneElements.clear();
    }

    @FXML
    public void toggleSnapToGrid() {
        snapToGrid ^= true;

        if (snapToGrid) {
            for (SceneElement element : sceneElements) {
                element.snapToGrid();
            }
            showGridLines();
            return;
        }

        hideGridLines();
    }

    private void initContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem textItem = new MenuItem("New Text");
        textItem.setOnAction(event -> showNewTextMenu(mouseX, mouseY));
        contextMenu.getItems().addAll(textItem);
        screen.setOnContextMenuRequested(event -> contextMenu.show(screen, event.getScreenX(), event.getScreenY()));
    }

        private void showNewTextMenu(double mouseX, double mouseY) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Text");
        dialog.setHeaderText("Enter the text:");
        dialog.setContentText("Text:");

        // Remove the question mark image
        dialog.setGraphic(null);

        // Add a checkbox for bold text
        CheckBox boldCheckBox = new CheckBox("Bold");
        VBox vbox = new VBox(dialog.getDialogPane().getContent(), boldCheckBox);
        dialog.getDialogPane().setContent(vbox);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(text -> {
            boolean isBold = boldCheckBox.isSelected();
            addSceneElement(new TextElement(mouseX, mouseY, text, screen, isBold));
        });
    }

    private void initGridLines() {
        screen.widthProperty().addListener((obs, oldVal, newVal) -> { // We do have to update vertical and horizontal lines, because if stretched to far the other kind of lines might not be created yet
            screen.getChildren().removeAll(gridLines);
            gridLines.clear();

            for (double x = 0; x < newVal.doubleValue(); x += gridSize) {
                Line verticalLine = new Line(x, 0, x, screen.getHeight());
                verticalLine.setStroke(Color.LIGHTGRAY);
                gridLines.add(verticalLine);
                verticalLine.setOpacity(snapToGrid ? 1 : 0);
                screen.getChildren().add(verticalLine);
                verticalLine.setViewOrder(2);
            }

            for (double y = 0; y < screen.getHeight(); y += gridSize) {
                Line horizontalLine = new Line(0, y, screen.getWidth(), y);
                horizontalLine.setStroke(Color.LIGHTGRAY);
                gridLines.add(horizontalLine);
                horizontalLine.setOpacity(snapToGrid ? 1 : 0);
                screen.getChildren().add(horizontalLine);
                horizontalLine.setViewOrder(2);
            }
        });

        screen.heightProperty().addListener((obs, oldVal, newVal) -> { // We do have to update vertical and horizontal lines, because if stretched to far the other kind of lines might not be created yet
            screen.getChildren().removeAll(gridLines);
            gridLines.clear();

            for (double x = 0; x < screen.getWidth(); x += gridSize) {
                Line verticalLine = new Line(x, 0, x, screen.getHeight());
                verticalLine.setStroke(Color.LIGHTGRAY);
                gridLines.add(verticalLine);
                verticalLine.setOpacity(snapToGrid ? 1 : 0);
                screen.getChildren().add(verticalLine);
                verticalLine.setViewOrder(2);
            }

            for (double y = 0; y < newVal.doubleValue(); y += gridSize) {
                Line horizontalLine = new Line(0, y, screen.getWidth(), y);
                horizontalLine.setStroke(Color.LIGHTGRAY);
                gridLines.add(horizontalLine);
                horizontalLine.setOpacity(snapToGrid ? 1 : 0);
                screen.getChildren().add(horizontalLine);
                horizontalLine.setViewOrder(2);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> {
                    for (double x = 0; x < screen.getWidth(); x += gridSize) {
                        Line verticalLine = new Line(x, 0, x, screen.getHeight());
                        verticalLine.setStroke(Color.LIGHTGRAY);
                        gridLines.add(verticalLine);
                        verticalLine.setOpacity(0);
                        screen.getChildren().add(verticalLine);
                        verticalLine.setViewOrder(2);
                    }

                    for (double y = 0; y < screen.getHeight(); y += gridSize) {
                        Line horizontalLine = new Line(0, y, screen.getWidth(), y);
                        horizontalLine.setStroke(Color.LIGHTGRAY);
                        gridLines.add(horizontalLine);
                        horizontalLine.setOpacity(0);
                        screen.getChildren().add(horizontalLine);
                        horizontalLine.setViewOrder(2);
                    }
                }
        ));
        timeline.play();
    }

    private void showGridLines() {
        for (Line line : gridLines) {
            line.setOpacity(1);
        }
    }

    private void hideGridLines() {
        for (Line line : gridLines) {
            line.setOpacity(0);
        }
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
        if (contextMenu != null) {
            contextMenu.hide();
        }

        if (payload != null) {
            sceneElements.add(payload);
            payload.SPRITE.setViewOrder(-1);
            payload.setActive();
            payload = null;
        }

        if (selectedElement != null) {;
            selectSceneElement(null);
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
                element.debugOff();
            }
            if (payload != null) {
                payload.debugOff();
            }

            for (ConnectionLine line : connections) {
                line.resetColor();
            }

            if (connectionLineTemp != null) {
                connectionLineTemp.resetColor();
            }
        } else {
            for (SceneElement element : sceneElements) {
                element.debugOn();
            }
            if (payload != null) {
                payload.debugOn();
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

    public static void removeSceneElement(SceneElement element) {
        if (selectedElement == element) {
            selectedElement = null;
        }

        sceneElements.remove(element);
        element.remove();
    }

    public static void addSceneElement(SceneElement element) {
        sceneElements.add(element);
        element.setActive();
    }

    public static void selectSceneElement(SceneElement element) {
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
                removeSceneElement(selectedElement);
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

        if (connectionPoint instanceof LineConnectionPoint point) {
            point.getRoot().pushValue();
        } else {
            if (connectionPoint.isInput()) {
                pickedConnection.getRoot().pushValue();
            } else {
                connectionPoint.getRoot().pushValue();
            }
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

    public static boolean shouldSnapToGrid() {
        return snapToGrid;
    }
}