package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logger.Logger;
import com.seeg2.logicable.logicGate.NOTGate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class NOTElement extends GateElement {
    public NOTElement(Pane screen) {
        super(screen);
        this.LOGIC_PROVIDER = new NOTGate();
        this.SPRITE = new ImageView();

        try {
            SPRITE.setImage(new Image(getClass().getResource("/images/logic_gates/NOT.png").toExternalForm()));
        } catch (Exception e) {
            Logger.error("Failed to load NOT-sprite");
        }

        initSprite();
    }
}
