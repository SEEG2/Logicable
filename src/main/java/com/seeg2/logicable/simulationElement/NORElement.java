package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.NORGate;
import com.seeg2.logicable.logicGate.NOTGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class NORElement extends SimulationElement {
    private final Pane screen;
    public NORElement(Pane screen) {
        this.LOGIC_PROVIDER = new NORGate();
        this.SPRITE = new ImageView();
        this.screen = screen;

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/NOR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load NOR-sprite");
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
