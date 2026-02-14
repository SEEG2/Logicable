package com.seeg2.logicable.simulation.element;

import com.seeg2.logicable.logger.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class NANDElement extends GateElement {
    public NANDElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/NAND.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load NAND-sprite");
        }

        initSprite();
    }

    protected boolean calcValueForInputs(boolean value1, boolean value2) {
        return !(value1 && value2);
    }
}
