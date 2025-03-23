package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulationElement.simulationElement.SimulationElement;
import javafx.scene.Cursor;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static javafx.scene.text.Font.font;

public class TextElement extends SceneElement{
    private final Text text;
    private final Pane screen;
    private final boolean isBold;

    public TextElement(double x, double y, String message, Pane screen) {
        this(x, x, message, screen, false);
    }

    public TextElement(double x, double y, String message, Pane screen, boolean isBold) {
        this.screen = screen;
        this.isBold = isBold;

        text = new Text(x, y, message);
        if (isBold) {
            text.setFont(font("System", javafx.scene.text.FontWeight.BOLD, 20));
        } else {
            text.setFont(font("System", FontWeight.NORMAL, 20));
        }
        screen.getChildren().add(text);

        text.setOnMouseClicked((action) -> {
            if (!this.isActive) {
                return;
            }

            select();
            text.setCursor(Cursor.HAND);
            action.consume();
        });

        text.setOnMousePressed(event -> {
            if (!this.isActive) {
                return;
            }

            mouseX = event.getSceneX() - text.getLayoutX();
            mouseY = event.getSceneY() - text.getLayoutY();

            event.consume();
        });

        text.setOnMouseDragged(event -> {
            if (!this.isActive) {
                return;
            }

            select();
            text.setCursor(Cursor.CLOSED_HAND);
            setPosition(event.getSceneX() - mouseX, event.getSceneY() - mouseY);
            event.consume();
        });
    }

    @Override
    public void remove() {
        screen.getChildren().remove(text);
    }

    @Override
    public void setPosition(double x, double y) {
        if (MainController.shouldSnapToGrid()) {
            x = Math.round(x / 30) * 30;
            y = Math.round(y / 30) * 30;
        }

        text.setLayoutX(x);
        text.setLayoutY(y);
    }

    @Override
    public void snapToGrid(int gridSize) {
        text.setLayoutX(Math.round(text.getLayoutX() / gridSize) * gridSize);
        text.setLayoutY(Math.round(text.getLayoutY() / gridSize) * gridSize);
    }

    @Override
    public void select() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.4d);
        colorAdjust.setHue(0.01d);
        colorAdjust.setSaturation(0.5d);

        Blend blend = new Blend();
        blend.setTopInput(colorAdjust);
        blend.setBottomInput(new Glow(0.8d));

        text.setEffect(blend);
        MainController.selectSceneElement(this);
    }

    @Override
    public void deselect() {
        text.setEffect(null);
    }

    @Override
    public void debugOn() {}

    @Override
    public void debugOff() {}

    @Override
    public void setActive() {
        isActive = true;
        text.setCursor(Cursor.HAND);
        text.setViewOrder(-1);
        initContextMenu(text);
    }

    @Override
    public void copyElement() {
        TextElement copy;
        try {
            if (MainController.shouldSnapToGrid()) {
                copy = new TextElement(this.text.getLayoutX() + 15, this.text.getLayoutY() + 15, text.getText(), screen, isBold);
            } else {
                copy = new TextElement(this.text.getLayoutX() + 10, this.text.getLayoutY() + 10, text.getText(), screen, isBold);
            }

            MainController.addSceneElement(copy);
            copy.select();
        } catch (Exception e) {
            Logger.error("Failed to create a copy of a scene element");
        }
    }
}
