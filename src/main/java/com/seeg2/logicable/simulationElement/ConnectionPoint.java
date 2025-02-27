package com.seeg2.logicable.simulationElement;

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

    public ConnectionPoint(Pane screen, ImageView root, float xOffset, float yOffset) {
        this.screen = screen;
        this.root = root;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        isInput = true;

        boundCircle = new Circle(5, Color.RED);

        boundCircle.centerXProperty().bind(Bindings.add(root.layoutXProperty(), xOffset));
        boundCircle.centerYProperty().bind(Bindings.add(root.layoutYProperty(), yOffset));

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

        screen.getChildren().add(boundCircle);
    }

    public Circle getCircle() {
        return boundCircle;
    }
}