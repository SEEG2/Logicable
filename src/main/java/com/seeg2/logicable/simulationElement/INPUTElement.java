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
public class INPUTElement extends SceneElement {
    private SceneElementConnectionPoint output;
    private boolean value;
    private Circle valueCircle;
    private boolean circleDragged;

    public INPUTElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/INPUT.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load INPUT-sprite");
        }

        initSprite();
        initValueCircle();
    }

    private void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(60);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        output = new SceneElementConnectionPoint(screen, this, (float) SPRITE.getBoundsInLocal().getWidth(), centerLineY, false);

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
        valueCircle = new Circle(18);
        valueCircle.centerXProperty().bind(Bindings.add(SPRITE.layoutXProperty(), 26f));
        valueCircle.centerYProperty().bind(Bindings.add(SPRITE.layoutYProperty(), Bindings.divide(SPRITE.fitHeightProperty(), 2)));
        valueCircle.setOnMouseClicked((event) -> {
            if (circleDragged) {
                circleDragged = false;
                return;
            }

            value ^= true;
            updateCircleColor();
            pushValue();
            event.consume();
        });
        valueCircle.setOnMousePressed(SPRITE.getOnMousePressed());
        valueCircle.setOnMouseDragged((action) -> {
            if (SPRITE.getOnMouseDragged() != null) {
                SPRITE.getOnMouseDragged().handle(action);
            }
            circleDragged = true;
        });

        updateCircleColor();

        screen.getChildren().add(valueCircle);
        valueCircle.toBack();
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        output.update();
    }

    public void showConnectionPoints() {
        output.show();
    }

    public void hideConnectionPoints() {
        output.hide();
    }

    public void remove() {
        value = false;
        pushValue();

        screen.getChildren().remove(SPRITE);
        screen.getChildren().remove(valueCircle);

        output.remove();
    }

    private void updateCircleColor() {
        if (value) {
            valueCircle.setFill(GREEN);
            return;
        }
        valueCircle.setFill(RED);
    }

    public void pushValue() {
        if (output.getConnection() != null) {
            output.getConnection().pushValue(output, value);
        }
    }

    public void pushValue(ConnectionPoint source, boolean value) {}
}
