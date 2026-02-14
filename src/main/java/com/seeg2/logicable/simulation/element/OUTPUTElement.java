package com.seeg2.logicable.simulation.element;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import static com.seeg2.logicable.controller.MainController.gridSize;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

// TODO  rework this (just a place holder right now)
public class OUTPUTElement extends SimulationElement {
    private SceneElementConnectionPoint input;
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
        SPRITE.setFitHeight(60);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        input = new SceneElementConnectionPoint(screen, this, 0, centerLineY, true);

        SPRITE.setOnMouseClicked((action) -> {
            if (!this.isActive) {
                return;
            }

            select();
            SPRITE.setCursor(Cursor.HAND);
            valueCircle.setCursor(Cursor.HAND);
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
            valueCircle.setCursor(Cursor.CLOSED_HAND);
            setPosition(event.getSceneX() - mouseX, event.getSceneY() - mouseY);
            event.consume();
        });
    }

    private void initValueCircle() {
        valueCircle = new Circle(18);
        valueCircle.centerXProperty().bind(Bindings.add(SPRITE.layoutXProperty(), 34f));
        valueCircle.centerYProperty().bind(Bindings.add(SPRITE.layoutYProperty(), Bindings.divide(SPRITE.fitHeightProperty(), 2)));
        valueCircle.setCursor(Cursor.HAND);
        valueCircle.setOnMouseClicked(SPRITE.getOnMouseClicked());
        valueCircle.setOnMousePressed(SPRITE.getOnMousePressed());
        valueCircle.setOnMouseDragged(SPRITE.getOnMouseDragged());
        initContextMenu(valueCircle);

        updateCircleColor();

        screen.getChildren().add(valueCircle);
        valueCircle.setViewOrder(-1);
    }

    public void setPosition(double x, double y) {
        if (MainController.shouldSnapToGrid()) {
            x = Math.round(x / gridSize) * gridSize;
            y = Math.round(y / gridSize) * gridSize;
        }

        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input.update();
    }

    public void debugOn() {
        input.show();
    }

    public void debugOff() {
        input.hide();
    }

    public void remove() {
        screen.getChildren().remove(SPRITE);
        screen.getChildren().remove(valueCircle);

        input.remove();
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

    public void snapToGrid() {
        SPRITE.setLayoutX(Math.round(SPRITE.getLayoutX() / gridSize) * gridSize);
        SPRITE.setLayoutY(Math.round(SPRITE.getLayoutY() / gridSize) * gridSize);

        input.update();
    }
}
