package com.seeg2.logicable.simulationElement.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import static com.seeg2.logicable.controller.MainController.gridSize;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class INPUTElement extends SimulationElement {
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
        valueCircle.centerXProperty().bind(Bindings.add(SPRITE.layoutXProperty(), 26f));
        valueCircle.centerYProperty().bind(Bindings.add(SPRITE.layoutYProperty(), Bindings.divide(SPRITE.fitHeightProperty(), 2)));
        valueCircle.setCursor(Cursor.HAND);
        valueCircle.setOnMouseClicked((event) -> {
            if (circleDragged) {
                SPRITE.setCursor(Cursor.HAND);
                valueCircle.setCursor(Cursor.HAND);
                circleDragged = false;
                return;
            }

            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            value ^= true;

            updateCircleColor();
            pushValue();
            event.consume();
        });
        valueCircle.setOnMousePressed(SPRITE.getOnMousePressed());
        valueCircle.setOnMouseDragged((action) -> {
            SPRITE.getOnMouseDragged().handle(action);
            circleDragged = true;
        });
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

        output.update();
    }

    public void debugOn() {
        output.show();
    }

    public void debugOff() {
        output.hide();
    }

    public void remove() {
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

    public void snapToGrid() {
        SPRITE.setLayoutX(Math.round(SPRITE.getLayoutX() / gridSize) * gridSize);
        SPRITE.setLayoutY(Math.round(SPRITE.getLayoutY() / gridSize) * gridSize);

        output.update();
    }
}
