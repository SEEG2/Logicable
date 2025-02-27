package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.XORGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class XORElement extends GateElement {
    public XORElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new XORGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/XOR.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load XOR-sprite");
        }
        initSprite();
    }
}
