package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import com.seeg2.logicable.logicGate.LogicGate;
import javafx.scene.effect.Blend;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class GateElement {
    public ImageView SPRITE;
    public LogicGate LOGIC_PROVIDER;
    protected ConnectionPoint input1, input2, output;
    protected final Pane screen;
    protected boolean isActive;
    private double mouseX, mouseY;
    private float centerLineY, inputOffsetY;

    protected GateElement(Pane screen) {
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

    public void deselect() {
        SPRITE.setEffect(null);
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);

        input1.update();
        input2.update();
        output.update();
    }

    protected void initSprite() {
        isActive = false;
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(50);
        screen.getChildren().add(SPRITE);
        SPRITE.setPickOnBounds(true);

        centerLineY = (float) (SPRITE.getFitHeight() / 2f);

        // Position of the connector relative to the center line. Based on the images used.
        inputOffsetY = (float) (SPRITE.getFitHeight() / 3.6f);

        input1 = new ConnectionPoint(screen, SPRITE, 0, centerLineY + inputOffsetY);
        input2 = new ConnectionPoint(screen, SPRITE, 0, centerLineY - inputOffsetY);
        output = new ConnectionPoint(screen, SPRITE, (float) SPRITE.getBoundsInLocal().getWidth(), centerLineY, false);

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

    public void remove() {
        screen.getChildren().remove(SPRITE);

        input1.remove();
        input2.remove();
        output.remove();
    }

    public void setActive() {
        isActive = true;
    }

    public void showConnectionPoints() {
        input1.show();
        input2.show();
        output.show();
    }


    public void hideConnectionPoints() {
        input1.hide();
        input2.hide();
        output.hide();
    }
}
