package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class ORElement extends GateElement {
    public ORElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/OR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load OR-sprite");
        }

        initSprite();
    }

    public boolean getValue() {
        return tryForValue1() || tryForValue2();
    }
}
