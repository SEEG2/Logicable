package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.XNORGate;
import com.seeg2.logicable.logicGate.XORGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class XNORElement extends SimulationElement {
    private final Pane screen;
    public XNORElement(Pane screen) {
        this.LOGIC_PROVIDER = new XNORGate();
        this.SPRITE = new ImageView();
        this.screen = screen;

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/XNOR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load XNOR-sprite");
        }
        SPRITE.setPreserveRatio(true);
        SPRITE.setFitHeight(50);
        screen.getChildren().add(SPRITE);
    }

    public void remove() {
        screen.getChildren().remove(SPRITE);
    }

    public void setPosition(double x, double y) {
        SPRITE.setLayoutX(x);
        SPRITE.setLayoutY(y);
    }
}
