package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectionPoint {
    private Pane screen;
    private float xOffset, yOffset;
    private SimulationElement root;
    private Circle boundCircle;
    // true -> input; false -> output
    private boolean isInput;
    private ConnectionLine connection;
    private SimulationElement connectedElement;

    public ConnectionPoint(Pane screen, SimulationElement root, float xOffset, float yOffset) {
        this.screen = screen;
        this.root = root;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        isInput = true;

        boundCircle = new Circle(5, Color.RED);

        if (!MainController.isDebugMode()) {
            this.hide();
        }

        boundCircle.centerXProperty().bind(Bindings.add(root.getSprite().layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.getSprite().layoutYProperty(), yOffset));

        boundCircle.setOnMouseClicked((action) -> {
            MainController.instance.setPickedConnection(this);
            action.consume();
        });

        screen.getChildren().add(boundCircle);
    }

    public ConnectionPoint(Pane screen, SimulationElement root, float xOffset, float yOffset, boolean isInput) {
        this.screen = screen;
        this.root = root;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.isInput = isInput;

        boundCircle = new Circle(5, Color.RED);

        if (!MainController.isDebugMode()) {
            this.hide();
        }

        boundCircle.centerXProperty().bind(Bindings.add(root.getSprite().layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.getSprite().layoutYProperty(), yOffset));

        boundCircle.setOnMouseClicked((action) -> {
            MainController.instance.setPickedConnection(this);
            action.consume();
        });

        screen.getChildren().add(boundCircle);
    }

    public Circle getCircle() {
        return boundCircle;
    }

    public ConnectionLine getConnection() {
        return connection;
    }

    public void setConnection(ConnectionLine connection, SimulationElement connectedElement) {
        this.connection = connection;
        this.connectedElement = connectedElement;
    }

    public void update() {
        if (connection != null) {
            connection.updatePos();
        }
    }

    public void remove() {
        screen.getChildren().remove(boundCircle);

        if (connection != null) {
            connection.remove(this);
        }
    }

    public void removeConnection() {
        if (connection != null) {
            connection.remove();
        }
    }

    public boolean isInput() {
        return isInput;
    }

    public SimulationElement getRoot() {
        return root;
    }

    public void show() {
        boundCircle.setOpacity(1);
    }


    public void hide() {
        boundCircle.setOpacity(0);
    }
}