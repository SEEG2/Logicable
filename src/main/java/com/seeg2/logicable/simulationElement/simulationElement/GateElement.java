package com.seeg2.logicable.simulationElement.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

import static com.seeg2.logicable.controller.MainController.gridSize;
import static java.lang.Math.ceil;

public abstract class GateElement extends SimulationElement {
    protected SceneElementConnectionPoint input1, input2, output;
    protected boolean cached1, cached2;
    protected boolean isProcessing;

    protected GateElement(Pane screen) {
        super(screen);
    }

    public void setPosition(double x, double y) {
        if (MainController.shouldSnapToGrid()) {
            x = Math.round(x / gridSize) * gridSize;
            y = Math.round(y / gridSize) * gridSize;
        }

        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input1.update();
        input2.update();
        output.update();
    }

    protected void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(60);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        // Position of the connector relative to the center line. Based on the images used.
        float inputOffsetY = (float) ceil(SPRITE.getFitHeight() / 3.2f); // If this is not a full pixel the lines will look weird

        input1 = new SceneElementConnectionPoint(screen, this, 0, centerLineY + inputOffsetY);
        input2 = new SceneElementConnectionPoint(screen, this, 0, centerLineY - inputOffsetY);
        output = new SceneElementConnectionPoint(screen, this, (float) SPRITE.getBoundsInLocal().getWidth(), centerLineY, false);

        SPRITE.setOnMouseClicked((action) -> {
            if (!this.isActive) {
                return;
            }

            select();
            SPRITE.setCursor(Cursor.HAND);
            action.consume();
        });

        SPRITE.setOnMousePressed(event -> {
            if (!this.isActive) {
                return;
            }

            mouseX = event.getSceneX() - SPRITE.getLayoutX();
            mouseY = event.getSceneY() - SPRITE.getLayoutY();

            event.consume();
        });

        SPRITE.setOnMouseDragged(event -> {
            if (!this.isActive) {
                return;
            }

            select();
            SPRITE.setCursor(Cursor.CLOSED_HAND);
            setPosition(event.getSceneX() - mouseX, event.getSceneY() - mouseY);
            event.consume();
        });
    }

    public void remove() {
        screen.getChildren().remove(SPRITE);

        input1.remove();
        input2.remove();
        output.remove();
    }

    public void debugOn() {
        input1.show();
        input2.show();
        output.show();
    }

    public void debugOff() {
        input1.hide();
        input2.hide();
        output.hide();
    }

    public void pushValue() {
        isProcessing = true;
        try {
            if (output.getConnection() != null) {
                output.getConnection().pushValue(output, calcValueForInputs(cached1, cached2));
            }
        } finally {
            isProcessing = false;
        }
    }

    protected void pushValue(ConnectionPoint source, boolean value) {
        if (isProcessing) {
            return;
        }


        try {
            isProcessing = true;
            if (source == input1.getOtherConnectionPoint()) {
                if (cached1 == value) {
                    return;
                }
                cached1 = value;
            } else {
                if (cached2 == value) {
                    return;
                }
                cached2 = value;
            }

            if (output.getConnection() != null) {
                output.getConnection().pushValue(output, calcValueForInputs(cached1, cached2));
            }
        } finally {
            isProcessing = false;
        }
    }

    public void snapToGrid() {
        SPRITE.setLayoutX(Math.round(SPRITE.getLayoutX() / gridSize) * gridSize);
        SPRITE.setLayoutY(Math.round(SPRITE.getLayoutY() / gridSize) * gridSize);

        input1.update();
        input2.update();
        output.update();
    }

    protected abstract boolean calcValueForInputs(boolean value1, boolean value2);
}
