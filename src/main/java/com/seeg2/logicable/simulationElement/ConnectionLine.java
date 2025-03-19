package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;

public class ConnectionLine {
    private ConnectionPoint source;
    private ConnectionPoint destination;
    private Line verticalLine;
    private Line verticalLineClickBox;
    private Line horizontalLine;
    private Line horizontalLineClickBox;
    private Pane screen;
    private final ArrayList<LineConnectionPoint> lineConnectionPoints = new ArrayList<>();
    private boolean isTemp;
    private boolean invert;

    public ConnectionLine(ConnectionPoint source, ConnectionPoint destination, Pane screen) {
        this(source, destination, screen, false, false);
    }

    public ConnectionLine(ConnectionPoint source, ConnectionPoint destination, Pane screen, boolean invert) {
        this(source, destination, screen, false, invert);
    }

    public ConnectionLine(ConnectionPoint source, ConnectionPoint destination, Pane screen, boolean isTemp, boolean invert) {
        this.source = source;
        this.destination = destination;
        this.screen = screen;
        this.isTemp = isTemp;
        this.invert = invert;
        this.verticalLine = new Line();
        this.horizontalLine = new Line();
        this.verticalLineClickBox = new Line();
        this.horizontalLineClickBox = new Line();

        verticalLine.setStrokeWidth(2);
        verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        verticalLineClickBox.setStrokeWidth(6);
        verticalLineClickBox.setStrokeLineCap(StrokeLineCap.ROUND);
        verticalLineClickBox.setStroke(Color.web("#550055"));
        verticalLineClickBox.setOpacity(0);
        verticalLineClickBox.endXProperty().bind(verticalLine.endXProperty());
        verticalLineClickBox.endYProperty().bind(verticalLine.endYProperty());
        verticalLineClickBox.startYProperty().bind(verticalLine.startYProperty());
        verticalLineClickBox.startXProperty().bind(verticalLine.startXProperty());
        verticalLineClickBox.setOnMouseClicked((event -> {
            if (this.isTemp) {
                return;
            }
            LineConnectionPoint lineConnectionPoint = new LineConnectionPoint(screen, source.getRoot(), verticalLine, this, 1, (float) ((event.getY() - verticalLine.getStartY()) / (verticalLine.getEndY() - verticalLine.getStartY())));
            MainController.instance.setPickedConnection(lineConnectionPoint, true);
            lineConnectionPoints.add(lineConnectionPoint);
            event.consume();
        }));

        horizontalLine.setStrokeWidth(2);
        horizontalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        horizontalLineClickBox.setStrokeWidth(6);
        horizontalLineClickBox.setStrokeLineCap(StrokeLineCap.ROUND);
        horizontalLineClickBox.setStroke(Color.web("#550055"));
        horizontalLineClickBox.setOpacity(0);
        horizontalLineClickBox.endXProperty().bind(horizontalLine.endXProperty());
        horizontalLineClickBox.endYProperty().bind(horizontalLine.endYProperty());
        horizontalLineClickBox.startXProperty().bind(horizontalLine.startXProperty());
        horizontalLineClickBox.startYProperty().bind(horizontalLine.startYProperty());
        horizontalLineClickBox.setOnMouseClicked((event) -> {
            if (this.isTemp) {
                return;
            }
            LineConnectionPoint lineConnectionPoint = new LineConnectionPoint(screen, source.getRoot(), horizontalLine, this, (float) ((event.getX() - horizontalLine.getStartX()) / (horizontalLine.getEndX() - horizontalLine.getStartX())), 1);
            MainController.instance.setPickedConnection(lineConnectionPoint, false);
            lineConnectionPoints.add(lineConnectionPoint);
            event.consume();
        });

        if (MainController.isDebugMode()) {
            setDebugColor();
        } else {
            resetColor();
        }

        screen.getChildren().addAll(verticalLine, horizontalLine, verticalLineClickBox, horizontalLineClickBox);
        verticalLineClickBox.toBack();
        horizontalLineClickBox.toBack();
        verticalLine.toBack();
        horizontalLine.toBack();
    }

    public void pushValue(ConnectionPoint source, boolean value) {
        if (destination == null) { // Just in case
            return;
        }

        for (LineConnectionPoint lineConnectionPoint : lineConnectionPoints) {
            ConnectionLine nextConnection = lineConnectionPoint.getConnection();
            if (nextConnection != null) {
                nextConnection.pushValue(source, value);
            }
        }

        destination.getRoot().pushValue(source, value);
    }

    public void updatePos() {
        double startX = source.getCircle().getCenterX();
        double startY = source.getCircle().getCenterY();
        double endX = destination.getCircle().getCenterX();
        double endY = destination.getCircle().getCenterY();

        if (invert) {
            horizontalLine.setStartX(startX);
            horizontalLine.setStartY(startY);
            horizontalLine.setEndX(endX);
            horizontalLine.setEndY(startY);

            verticalLine.setStartX(endX);
            verticalLine.setStartY(startY);
            verticalLine.setEndX(endX);
            verticalLine.setEndY(endY);
        } else {
            verticalLine.setStartX(startX);
            verticalLine.setStartY(startY);
            verticalLine.setEndX(startX);
            verticalLine.setEndY(endY);

            horizontalLine.setStartX(startX);
            horizontalLine.setStartY(endY);
            horizontalLine.setEndX(endX);
            horizontalLine.setEndY(endY);
        }

        for (LineConnectionPoint connectionPoint : lineConnectionPoints) {
            connectionPoint.update();
        }
    }

    public void updatePos(double destinationX, double destinationY) {
        double startX = source.getCircle().getCenterX();
        double startY = source.getCircle().getCenterY();

        if (invert) {
            horizontalLine.setStartX(startX);
            horizontalLine.setStartY(startY);
            horizontalLine.setEndX(destinationX);
            horizontalLine.setEndY(startY);

            verticalLine.setStartX(destinationX);
            verticalLine.setStartY(startY);
            verticalLine.setEndX(destinationX);
            verticalLine.setEndY(destinationY);
        } else {
            verticalLine.setStartX(startX);
            verticalLine.setStartY(startY);
            verticalLine.setEndX(startX);
            verticalLine.setEndY(destinationY);

            horizontalLine.setStartX(startX);
            horizontalLine.setStartY(destinationY);
            horizontalLine.setEndX(destinationX);
            horizontalLine.setEndY(destinationY);
        }
    }

    public void remove(ConnectionPoint connectionPoint) {
        if (connectionPoint == source) {
            destination.setConnection(null, null);
        } else {
            source.setConnection(null, null);
            if (source instanceof LineConnectionPoint) {
                source.remove();
            }
        }

        screen.getChildren().removeAll(verticalLine, horizontalLine, verticalLineClickBox, horizontalLineClickBox);

        for (LineConnectionPoint lineConnectionPoint : lineConnectionPoints) {
            lineConnectionPoint.remove(true);
        }
    }

    public void setDestination(ConnectionPoint destination) {
        if (destination.isInput()) {
            this.destination = destination;
            return;
        }

        this.destination = source;
        this.source = destination;
    }

    public void remove() {
        if (isTemp) {
            screen.getChildren().removeAll(verticalLine, horizontalLine, verticalLineClickBox, horizontalLineClickBox);
            return;
        }

        if (source != null) {
            source.setConnection(null, null);
        }

        if (destination != null) {
            destination.setConnection(null, null);
        }

        screen.getChildren().removeAll(verticalLine, horizontalLine, verticalLineClickBox, horizontalLineClickBox);

        if (source instanceof LineConnectionPoint) {
            source.remove();
        }

        for (LineConnectionPoint lineConnectionPoint : lineConnectionPoints) {
            lineConnectionPoint.remove(true);
        }
    }

    public void removeLineConnectionPoint(LineConnectionPoint lineConnectionPoint) {
        lineConnectionPoints.remove(lineConnectionPoint);
    }

    public void setDebugColor() {
        horizontalLine.setStroke(Color.web("#0000ff"));
        verticalLine.setStroke(Color.web("#0000ff"));

        horizontalLineClickBox.setOpacity(1);
        verticalLineClickBox.setOpacity(1);
        for (LineConnectionPoint lineConnectionPoint : lineConnectionPoints) {
            lineConnectionPoint.show();
        }
    }

    public void resetColor() {
        horizontalLine.setStroke(Color.web("#3a4450"));
        verticalLine.setStroke(Color.web("#3a4450"));

        horizontalLineClickBox.setOpacity(0);
        verticalLineClickBox.setOpacity(0);
        for (LineConnectionPoint lineConnectionPoint : lineConnectionPoints) {
            lineConnectionPoint.hide();
        }
    }

    public void setToNotTemp() {
        isTemp = false;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }
}