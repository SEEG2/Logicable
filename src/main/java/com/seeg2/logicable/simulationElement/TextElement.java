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

import static com.seeg2.logicable.controller.MainController.gridSize;
import static javafx.scene.text.Font.font;

public class TextElement extends SceneElement{
    private final Text text;
    private final Pane screen;
    private final boolean isBold;

    public TextElement(double x, double y, String message, Pane screen) {
        this(x, y, message, screen, false);
    }

    public TextElement(double x, double y, String message, Pane screen, boolean isBold) {
        this.screen = screen;
        this.isBold = isBold;

        text = new Text(x, y, message); // For some reason using LayoutX/Y for the text is buggy
        if (isBold) {
            text.setFont(font("System", javafx.scene.text.FontWeight.BOLD, 20));
        } else {
            text.setFont(font("System", FontWeight.NORMAL, 20));
        }
        screen.getChildren().add(text);

        if (MainController.shouldSnapToGrid()) {
            snapToGrid();
        }

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

            mouseX = event.getSceneX() - text.getX();
            mouseY = event.getSceneY() - text.getY();

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
            x = Math.round(x / gridSize) * gridSize;
            y = Math.round(y / gridSize) * gridSize;
        }

        text.setX(x);
        text.setY(y);
    }

    @Override
    public void snapToGrid() {
        text.setX(Math.round(text.getX() / gridSize) * gridSize);
        text.setY(Math.round(text.getY() / gridSize) * gridSize);
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
        TextElement copy = new TextElement(this.text.getX() + 10, this.text.getY() + 10, text.getText(), screen, isBold);
        MainController.addSceneElement(copy);
        copy.select();

    }
}
