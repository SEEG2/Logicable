package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.XNORGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class XNORElement extends GateElement {
    public XNORElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new XNORGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/XNOR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load XNOR-sprite");
        }

        initSprite();
    }
}
