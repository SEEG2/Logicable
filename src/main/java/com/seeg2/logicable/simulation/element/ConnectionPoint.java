package com.seeg2.logicable.simulation.element;

import javafx.scene.shape.Circle;

public interface ConnectionPoint {
    SimulationElement getRoot();
    ConnectionPoint getOtherConnectionPoint();
    void removeConnection();
    ConnectionLine getConnection();
    Circle getCircle();
    void setConnection(ConnectionLine connectionLine, ConnectionPoint connectionPoint);
    boolean isInput();
    void remove();
}
