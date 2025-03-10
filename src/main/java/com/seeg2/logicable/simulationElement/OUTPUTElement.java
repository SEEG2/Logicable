package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

// TODO  rework this (just a place holder right now)
public class OUTPUTElement extends SceneElement {
    private ConnectionPoint input;
    private Circle valueCircle;
    boolean value;
    public OUTPUTElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/OUTPUT.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load OUTPUT-sprite");
        }

        initSprite();
        initValueCircle();
    }

    private void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(50);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        input = new ConnectionPoint(screen, this, 0, centerLineY, true);

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

    private void initValueCircle() {
        valueCircle = new Circle(15);
        valueCircle.centerXProperty().bind(Bindings.add(SPRITE.layoutXProperty(), 28.5f));
        valueCircle.centerYProperty().bind(Bindings.add(SPRITE.layoutYProperty(), Bindings.divide(SPRITE.fitHeightProperty(), 2)));
        valueCircle.setOnMouseClicked(SPRITE.getOnMouseClicked());
        valueCircle.setOnMousePressed(SPRITE.getOnMousePressed());
        valueCircle.setOnMouseDragged(SPRITE.getOnMouseDragged());
        updateCircleColor();

        screen.getChildren().add(valueCircle);
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input.update();
    }

    public void showConnectionPoints() {
        input.show();
    }

    public void hideConnectionPoints() {
        input.hide();
    }

    public boolean getValue() {
        value = tryForValue();
        updateCircleColor();
        return value;
    }

    public void remove() {
        screen.getChildren().remove(SPRITE);
        screen.getChildren().remove(valueCircle);

        input.remove();
    }

    protected boolean tryForValue() {
        ConnectionPoint otherConnectionPoint = input.getOtherConnectionPoint();
        if (otherConnectionPoint == null) {
            return false;
        }

        return otherConnectionPoint.getRoot().getValue();
    }

    private void updateCircleColor() {
        if (value) {
            valueCircle.setFill(GREEN);
            return;
        }
        valueCircle.setFill(RED);
    }

    public void pushValue(ConnectionPoint source, boolean value) {
        this.value = value;
        updateCircleColor();
    }

    public void pushValue() {}
}
