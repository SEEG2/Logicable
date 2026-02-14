package com.seeg2.logicable.simulation.element;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulation.SceneElement;
import javafx.scene.Cursor;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class SimulationElement extends SceneElement {
    public ImageView SPRITE;
    protected final Pane screen;
    protected double mouseX, mouseY;

    protected SimulationElement(Pane screen) {
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
        MainController.selectSceneElement(this);
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
        SPRITE.setViewOrder(-1);
        initContextMenu(SPRITE);
    }
    public abstract void pushValue();
    protected abstract void pushValue(ConnectionPoint source, boolean value);
    public void copyElement() {
        try {
            SimulationElement copy = this.getClass().getConstructor(Pane.class).newInstance(screen);
            if (MainController.shouldSnapToGrid()) {
                copy.setPosition(this.SPRITE.getLayoutX() + 15, this.SPRITE.getLayoutY() + 15);
            } else {
                copy.setPosition(this.SPRITE.getLayoutX() + 10, this.SPRITE.getLayoutY() + 10);
            }

            MainController.addSceneElement(copy);
            copy.select();
        } catch (Exception e) {
            Logger.error("Failed to create a copy of a scene element");
        }
    }
}
