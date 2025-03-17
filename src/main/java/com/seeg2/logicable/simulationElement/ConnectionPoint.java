package com.seeg2.logicable.simulationElement;

import javafx.scene.shape.Circle;

public interface ConnectionPoint {
    SceneElement getRoot();
    ConnectionPoint getOtherConnectionPoint();
    void removeConnection();
    ConnectionLine getConnection();
    Circle getCircle();
    void setConnection(ConnectionLine connectionLine, ConnectionPoint connectionPoint);
    boolean isInput();
    void remove();
}
