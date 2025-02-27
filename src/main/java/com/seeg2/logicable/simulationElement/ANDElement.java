package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.ANDGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class ANDElement extends GateElement {
    public ANDElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new ANDGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/AND.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load AND-sprite");
        }

        this.
        initSprite();
    }
}
