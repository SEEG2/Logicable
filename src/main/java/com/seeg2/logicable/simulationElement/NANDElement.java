package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.NANDGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class NANDElement extends GateElement {
    public NANDElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new NANDGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/NAND.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load NAND-sprite");
        }

        initSprite();
    }
}
