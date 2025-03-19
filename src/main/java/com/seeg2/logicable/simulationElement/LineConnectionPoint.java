package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class LineConnectionPoint implements ConnectionPoint {
    private Pane screen;
    private SceneElement root;
    private Circle boundCircle;
    // true -> input; false -> output
    private ConnectionLine connection;
    private ConnectionPoint otherConnectionPoint;
    private ConnectionLine origin;

    public LineConnectionPoint(Pane screen, SceneElement root, Line motherLine, ConnectionLine origin,float offsetXRatio, float offsetYRatio) {
        this.screen = screen;
        this.root = root;
        this.origin = origin;

        boundCircle = new Circle(9, Color.GREEN);

        if (!MainController.isDebugMode()) {
            this.hide();
        }

        boundCircle.centerXProperty().bind(
                motherLine.startXProperty().add(
                        motherLine.endXProperty().subtract(motherLine.startXProperty()).multiply(offsetXRatio)
                )
        );
        boundCircle.centerYProperty().bind(motherLine.startYProperty().add(
                        motherLine.endYProperty().subtract(motherLine.startYProperty()).multiply(offsetYRatio)
                )
        );

        boundCircle.setOnMouseClicked((action) -> {
            this.remove();
            action.consume();
        });

        screen.getChildren().add(boundCircle);
        boundCircle.toFront();
    }

    public Circle getCircle() {
        return boundCircle;
    }

    public boolean isInput() {
        return false;
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
        origin.removeLineConnectionPoint(this);
        screen.getChildren().remove(boundCircle);

        if (connection != null) {
            connection.remove(this);
        }
    }

    public void remove(boolean isHandled) {
        if (isHandled) {
            screen.getChildren().remove(boundCircle);

            if (connection != null) {
                connection.remove(this);
            }
            return;
        }

        remove();
    }

    public void removeConnection() {
        remove();
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
