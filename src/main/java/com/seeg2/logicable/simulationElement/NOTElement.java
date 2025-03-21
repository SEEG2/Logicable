package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

// TODO  rework this (just a place holder right now)
public class NOTElement extends SceneElement {
    private SceneElementConnectionPoint input, output;
    private boolean cached;

    public NOTElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/NOT.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load NOT-sprite");
        }

        initSprite();
    }

    private void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(60);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        float centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        input = new SceneElementConnectionPoint(screen, this, 0, centerLineY);
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

    public void setPosition(double x, double y) {
        if (MainController.shouldSnapToGrid()) {
            x = Math.round(x / 30) * 30;
            y = Math.round(y / 30) * 30;
        }

        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input.update();
        output.update();
    }

    public void showConnectionPoints() {
        input.show();
        output.show();
    }

    public void hideConnectionPoints() {
        input.hide();
        output.hide();
    }

    public void remove() {
        pushValue(output, true);
        screen.getChildren().remove(SPRITE);

        input.remove();
        output.remove();
    }

    public void pushValue() {
        if (output.getConnection() != null) {
            output.getConnection().pushValue(output, !cached);
        }
    }

    public void pushValue(ConnectionPoint source, boolean value) {
        cached = value;

        if (output.getConnection() != null) {
            output.getConnection().pushValue(output, !cached);
        }
    }

    public void snapToGrid() {
        SPRITE.setLayoutX(Math.round(SPRITE.getLayoutX() / 30) * 30);
        SPRITE.setLayoutY(Math.round(SPRITE.getLayoutY() / 30) * 30);

        input.update();
        output.update();
    }
}
