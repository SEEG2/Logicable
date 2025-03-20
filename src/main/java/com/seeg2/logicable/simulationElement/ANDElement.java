package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class ANDElement extends GateElement {
    public ANDElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/AND.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load AND-sprite");
        }
        this.initSprite();
    }

    protected boolean calcValueForInputs(boolean value1, boolean value2) {
        return value1 && value2;
    }
}
