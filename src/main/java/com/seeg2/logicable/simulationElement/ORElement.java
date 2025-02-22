package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.ORGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class ORElement extends SimulationElement {
    public ORElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new ORGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/OR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load OR-sprite");
        }

        initSprite();
    }
}
