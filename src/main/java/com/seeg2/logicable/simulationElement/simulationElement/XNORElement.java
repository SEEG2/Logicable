package com.seeg2.logicable.simulationElement.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.simulationElement.simulationElement.GateElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class XNORElement extends GateElement {
    public XNORElement(Pane screen) {
        super(screen);
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/XNOR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load XNOR-sprite");
        }

        initSprite();
    }

    protected boolean calcValueForInputs(boolean value1, boolean value2) {
        return value1 == value2;
    }
}
