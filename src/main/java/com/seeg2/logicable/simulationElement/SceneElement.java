package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.Cursor;
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
    }
    public abstract void showConnectionPoints();
    public abstract void pushValue();
    protected abstract void pushValue(ConnectionPoint source, boolean value);
    public abstract void hideConnectionPoints();
    public abstract void remove();
    public abstract void setPosition(double x, double y);
    public abstract void snapToGrid();
}
