package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SceneElementConnectionPoint implements ConnectionPoint {
    private Pane screen;
    private SceneElement root;
    private Circle boundCircle;
    // true -> input; false -> output
    private boolean isInput;
    private ConnectionLine connection;
    private ConnectionPoint otherConnectionPoint;

    public SceneElementConnectionPoint(Pane screen, SceneElement root, float xOffset, float yOffset) {
        this(screen, root, xOffset, yOffset, true);
    }

    public SceneElementConnectionPoint(Pane screen, SceneElement root, float xOffset, float yOffset, boolean isInput) {
        this.screen = screen;
        this.root = root;
        this.isInput = isInput;

        boundCircle = new Circle(9, Color.RED);

        if (!MainController.isDebugMode()) {
            this.hide();
        }

        boundCircle.centerXProperty().bind(Bindings.add(root.getSprite().layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.getSprite().layoutYProperty(), yOffset));

        boundCircle.setOnMouseClicked((action) -> {
            if (!MainController.instance.isConnectionPicked() && this.connection != null) {
                removeConnection();
                return;
            }

            MainController.instance.setPickedConnection(this, false);
            action.consume();
        });

        screen.getChildren().add(boundCircle);
        boundCircle.toFront();
    }

    public Circle getCircle() {
        return boundCircle;
    }

    public ConnectionLine getConnection() {
        return connection;
    }

    public void setConnection(ConnectionLine connection, ConnectionPoint connectedElement) {
        this.connection = connection;
        this.otherConnectionPoint = connectedElement;
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
        connection = null;
    }

    public boolean isInput() {
        return isInput;
    }

    public SceneElement getRoot() {
        return root;
    }

    public void show() {
        boundCircle.setOpacity(1);
    }

    public void hide() {
        boundCircle.setOpacity(0);
    }

    public ConnectionPoint getOtherConnectionPoint() {
        return otherConnectionPoint;
    }
}