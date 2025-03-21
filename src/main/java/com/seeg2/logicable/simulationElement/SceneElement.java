package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class SceneElement {
    public ImageView SPRITE;
    protected final Pane screen;
    protected boolean isActive;
    protected double mouseX, mouseY;
    protected SceneElement(Pane screen) {
        this.screen = screen;
    }
    public void select() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.4d);
        colorAdjust.setHue(0.01d);
        colorAdjust.setSaturation(0.5d);

        Blend blend = new Blend();
        blend.setTopInput(colorAdjust);
        blend.setBottomInput(new Glow(0.8d));

        SPRITE.setEffect(blend);
        MainController.selectSimulationElement(this);
    }

    public ImageView getSprite() {
        return SPRITE;
    }

    public void deselect() {
        SPRITE.setEffect(null);
    }

    public void setActive() {
        isActive = true;
        SPRITE.setCursor(Cursor.HAND);
        initContextMenu(SPRITE);
    }

    protected void initContextMenu(Node node) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem copyItem = new MenuItem("Copy");
        deleteItem.setOnAction(event -> MainController.removeSimulationElement(this));
        copyItem.setOnAction(event -> copyElement());
        contextMenu.getItems().addAll(deleteItem, copyItem);
        node.setOnContextMenuRequested(event -> contextMenu.show(SPRITE, event.getScreenX(), event.getScreenY()));
    }
    public abstract void showConnectionPoints();
    public abstract void pushValue();
    protected abstract void pushValue(ConnectionPoint source, boolean value);
    public abstract void hideConnectionPoints();
    public abstract void remove();
    public void copyElement() {
        try {
            SceneElement copy = this.getClass().getConstructor(Pane.class).newInstance(screen);
            if (MainController.shouldSnapToGrid()) {
                copy.setPosition(this.SPRITE.getLayoutX() + 15, this.SPRITE.getLayoutY() + 15);
            } else {
                copy.setPosition(this.SPRITE.getLayoutX() + 10, this.SPRITE.getLayoutY() + 10);
            }

            MainController.addSimulationElement(copy);
            copy.select();
        } catch (Exception e) {
            Logger.error("Failed to create a copy of a scene element");
        }
    }
    public abstract void setPosition(double x, double y);
    public abstract void snapToGrid();
}
