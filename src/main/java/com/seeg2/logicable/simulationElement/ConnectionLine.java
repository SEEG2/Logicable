package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class ConnectionLine {
    private ConnectionPoint source;
    private ConnectionPoint destination;
    private Line verticalLine;
    private Line horizontalLine;
    private Pane screen;
    private boolean isTemp = false;

    public ConnectionLine(ConnectionPoint source, ConnectionPoint destination, Pane screen) {
        this.source = source;
        this.destination = destination;
        this.screen = screen;
        this.verticalLine = new Line();
        this.horizontalLine = new Line();

        verticalLine.setStrokeWidth(2);
        verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);

        horizontalLine.setStrokeWidth(2);
        horizontalLine.setStrokeLineCap(StrokeLineCap.ROUND);

        if (MainController.isDebugMode()) {
            setDebugColor();
        } else {
            resetColor();
        }

        screen.getChildren().addAll(verticalLine, horizontalLine);
    }

    public ConnectionLine(ConnectionPoint source, ConnectionPoint destination, Pane screen, boolean isTemp) {
        this.source = source;
        this.destination = destination;
        this.isTemp = isTemp;
        this.screen = screen;
        this.verticalLine = new Line();
        this.horizontalLine = new Line();

        verticalLine.setStrokeWidth(2);
        verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);

        horizontalLine.setStrokeWidth(2);
        horizontalLine.setStrokeLineCap(StrokeLineCap.ROUND);

        if (MainController.isDebugMode()) {
            setDebugColor();
        } else {
            resetColor();
        }

        screen.getChildren().addAll(verticalLine, horizontalLine);
    }

    public void updatePos() {
        double startX = source.getCircle().getCenterX();
        double startY = source.getCircle().getCenterY();
        double endX = destination.getCircle().getCenterX();
        double endY = destination.getCircle().getCenterY();

        verticalLine.setStartX(startX);
        verticalLine.setStartY(startY);
        verticalLine.setEndX(startX);
        verticalLine.setEndY(endY);

        horizontalLine.setStartX(startX);
        horizontalLine.setStartY(endY);
        horizontalLine.setEndX(endX);
        horizontalLine.setEndY(endY);
    }

    public void updatePos(double destinationX, double destinationY) {
        double startX = source.getCircle().getCenterX();
        double startY = source.getCircle().getCenterY();

        verticalLine.setStartX(startX);
        verticalLine.setStartY(startY);
        verticalLine.setEndX(startX);
        verticalLine.setEndY(destinationY);

        horizontalLine.setStartX(startX);
        horizontalLine.setStartY(destinationY);
        horizontalLine.setEndX(destinationX);
        horizontalLine.setEndY(destinationY);
    }

    public void remove(ConnectionPoint connectionPoint) {
        if (connectionPoint == source) {
            destination.setConnection(null, null);
        } else {
            source.setConnection(null, null);
        }

        screen.getChildren().remove(verticalLine);
        screen.getChildren().remove(horizontalLine);
    }

    public void setDestination(ConnectionPoint destination) {
        this.destination = destination;
    }

    public void remove() {
        if (isTemp) {
            screen.getChildren().remove(verticalLine);
            screen.getChildren().remove(horizontalLine);
            return;
        }

        if (source != null) {
            source.setConnection(null, null);
        }

        if (destination != null) {
            destination.setConnection(null, null);
        }

        screen.getChildren().remove(verticalLine);
        screen.getChildren().remove(horizontalLine);
    }

    public void setDebugColor() {
        horizontalLine.setStroke(Color.web("#0000ff"));
        verticalLine.setStroke(Color.web("#0000ff"));
    }

    public void resetColor() {
        horizontalLine.setStroke(Color.web("#3a4450"));
        verticalLine.setStroke(Color.web("#3a4450"));
    }

    public void setToNotTemp() {
        isTemp = false;
    }
}