package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.logicGate.LogicGate;
import javafx.scene.image.ImageView;

public abstract class SimulationElement {
    public ImageView SPRITE;
    public LogicGate LOGIC_PROVIDER;
    public abstract void remove();
    public abstract void setPosition(double x, double y);
}
