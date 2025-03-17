package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class SPLITTERElement extends SceneElement {
    protected ConnectionPoint input, output1, output2;
    protected boolean value
            ;
    public SPLITTERElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/SPLITTER.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load SPLITTER-sprite");
        }

        this.
        initSprite();
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

        float fullRight = (float) SPRITE.getBoundsInLocal().getWidth();

        input = new ConnectionPoint(screen, this, 0, centerLineY);

        output1 = new ConnectionPoint(screen, this, fullRight, centerLineY + inputOffsetY, false);
        output2 = new ConnectionPoint(screen, this, fullRight, centerLineY - inputOffsetY, false);

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

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input.update();
        output1.update();
        output2.update();
    }

    public void remove() {
        pushValue(output2, false);
        pushValue();
        screen.getChildren().remove(SPRITE);

        input.remove();
        output1.remove();
        output2.remove();
    }

    public void showConnectionPoints() {
        input.show();
        output1.show();
        output2.show();
    }

    public void hideConnectionPoints() {
        input.hide();
        output1.hide();
        output2.hide();
    }

    public boolean getValue() {
        return value;
    }

    public void pushValue() {
        ConnectionPoint otherConnectionPoint1 = output1.getOtherConnectionPoint();
        if (otherConnectionPoint1 != null) {
            otherConnectionPoint1.getRoot().pushValue(output1, value);
        }

        ConnectionPoint otherConnectionPoint2 = output2.getOtherConnectionPoint();
        if (otherConnectionPoint2 != null) {
            otherConnectionPoint2.getRoot().pushValue(output2, value);
        }
    }

    protected void pushValue(ConnectionPoint source, boolean value) {
        this.value = value;

        ConnectionPoint otherConnectionPoint1 = output1.getOtherConnectionPoint();
        if (otherConnectionPoint1 != null) {
            otherConnectionPoint1.getRoot().pushValue(output1, value);
        }

        ConnectionPoint otherConnectionPoint2 = output2.getOtherConnectionPoint();
        if (otherConnectionPoint2 != null) {
            otherConnectionPoint2.getRoot().pushValue(output2, value);
        }
    }
}
