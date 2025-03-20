package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
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
        valueCircle.centerXProperty().bind(Bindings.add(SPRITE.layoutXProperty(), 34f));
        valueCircle.centerYProperty().bind(Bindings.add(SPRITE.layoutYProperty(), Bindings.divide(SPRITE.fitHeightProperty(), 2)));
        valueCircle.setOnMouseClicked(SPRITE.getOnMouseClicked());
        valueCircle.setOnMousePressed(SPRITE.getOnMousePressed());
        valueCircle.setOnMouseDragged(SPRITE.getOnMouseDragged());
        updateCircleColor();

        screen.getChildren().add(valueCircle);
        valueCircle.toBack();
    }

    public void setPosition(double x, double y) {
        if (MainController.shouldSnapToGrid()) {
            x = Math.round(x / 30) * 30;
            y = Math.round(y / 30) * 30;
        }

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
        SPRITE.setLayoutX(Math.round(SPRITE.getLayoutX() / 30) * 30);
        SPRITE.setLayoutY(Math.round(SPRITE.getLayoutY() / 30) * 30);

        input.update();
    }
}
