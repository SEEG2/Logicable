package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logicGate.LogicGate;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class SimulationElement {
    public ImageView SPRITE;
    public LogicGate LOGIC_PROVIDER;
    protected final Pane screen;
    protected boolean isActive;

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
    }

    public void deselect() {
        SPRITE.setEffect(null);
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);
    }

    protected void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(50);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);
        SPRITE.setOnMouseClicked((action) -> {
            if (!this.isActive) {
                return;
            }

            MainController.selectSimulationElement(this);
            action.consume();
        });
    }

    public void remove() {
        screen.getChildren().remove(SPRITE);
    }

    public void setActive() {
        isActive = true;
    }
}
