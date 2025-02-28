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
    private ImageView root;
    private Circle boundCircle;
    // true -> input; false -> output
    private boolean isInput;
    private ConnectionLine connection;

    public ConnectionPoint(Pane screen, ImageView root, float xOffset, float yOffset) {
        this.screen = screen;
        this.root = root;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        isInput = true;

        boundCircle = new Circle(5, Color.RED);

        boundCircle.centerXProperty().bind(Bindings.add(root.layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.layoutYProperty(), yOffset));

        boundCircle.setOnMouseClicked((action) -> {
            MainController.instance.setPickedConnection(this);
            action.consume();
        });

        screen.getChildren().add(boundCircle);
    }

    public ConnectionPoint(Pane screen, ImageView root, float xOffset, float yOffset, boolean isInput) {
        this.screen = screen;
        this.root = root;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.isInput = isInput;

        boundCircle = new Circle(5, Color.RED);

        boundCircle.centerXProperty().bind(Bindings.add(root.layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.layoutYProperty(), yOffset));

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

    public void setConnection(ConnectionLine connection) {
        this.connection = connection;
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

    public ImageView getRoot() {
        return root;
    }

    public void setRoot(ImageView root) {
        this.root = root;
    }
}