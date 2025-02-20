package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.ORGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class ORElement extends SimulationElement {
    private final Pane screen;
    public ORElement(Pane screen) {
        this.LOGIC_PROVIDER = new ORGate();
        this.SPRITE = new ImageView();
        this.screen = screen;

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/OR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load OR-sprite");
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
