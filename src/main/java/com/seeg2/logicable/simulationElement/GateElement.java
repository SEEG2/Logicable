package com.seeg2.logicable.simulationElement;

import javafx.scene.layout.Pane;

public abstract class GateElement extends SceneElement {
    protected ConnectionPoint input1, input2, output;
    protected boolean cached1, cached2;
    protected GateElement(Pane screen) {
        super(screen);
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input1.update();
        input2.update();
        output.update();
    }

    protected void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(50);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        // Position of the connector relative to the center line. Based on the images used.
        float inputOffsetY = (float) (SPRITE.getFitHeight() / 3.1f);

        input1 = new ConnectionPoint(screen, this, 0, centerLineY + inputOffsetY);
        input2 = new ConnectionPoint(screen, this, 0, centerLineY - inputOffsetY);
        output = new ConnectionPoint(screen, this, (float) SPRITE.getBoundsInLocal().getWidth(), centerLineY, false);

        SPRITE.setOnMouseClicked((action) -> {
            if (!this.isActive) {
                return;
            }

            select();
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

            setPosition(event.getSceneX() - mouseX, event.getSceneY() - mouseY);
            event.consume();
        });
    }

    public void remove() {
        pushValue(output, false);
        pushValue();
        screen.getChildren().remove(SPRITE);

        input1.remove();
        input2.remove();
        output.remove();
    }

    public void showConnectionPoints() {
        input1.show();
        input2.show();
        output.show();
    }

    public void hideConnectionPoints() {
        input1.hide();
        input2.hide();
        output.hide();
    }

    public boolean getValue() {
        return calcValueForInputs(cached1, cached2);
    }

    public void pushValue() {
        ConnectionPoint otherConnectionPoint = output.getOtherConnectionPoint();
        if (otherConnectionPoint != null) {
            otherConnectionPoint.getRoot().pushValue(output, calcValueForInputs(cached1, cached2));
        }
    }

    protected void pushValue(ConnectionPoint source, boolean value) {
        if (source == input1.getOtherConnectionPoint()) {
            cached1 = value;
        } else {
            cached2 = value;
        }

        ConnectionPoint otherConnectionPoint = output.getOtherConnectionPoint();
        if (otherConnectionPoint == null) {
            return;
        }

        otherConnectionPoint.getRoot().pushValue(output, calcValueForInputs(cached1, cached2));
    }

    protected abstract boolean calcValueForInputs(boolean value1, boolean value2);
}
