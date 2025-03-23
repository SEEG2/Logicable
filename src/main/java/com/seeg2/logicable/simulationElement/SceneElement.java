package com.seeg2.logicable.simulationElement;

import com.seeg2.logicable.controller.MainController;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public abstract class SceneElement {
    public boolean isActive;
    protected double mouseX, mouseY;

    public abstract void remove();
    public abstract void setPosition(double x, double y);
    public abstract void snapToGrid(int gridSize);
    public abstract void select();
    public abstract void deselect();
    public abstract void debugOn();
    public abstract void debugOff();
    public abstract void setActive();
    protected void initContextMenu(Node node) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem copyItem = new MenuItem("Copy");
        deleteItem.setOnAction(event -> MainController.removeSceneElement(this));
        copyItem.setOnAction(event -> copyElement());
        contextMenu.getItems().addAll(deleteItem, copyItem);
        node.setOnContextMenuRequested(event ->{
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    protected abstract void copyElement();
}
