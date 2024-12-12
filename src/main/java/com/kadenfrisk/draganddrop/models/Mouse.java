package com.kadenfrisk.draganddrop.models;

import javafx.scene.input.MouseEvent;

public class Mouse {
    private static final Mouse instance = new Mouse();
    private boolean draggingBlock;
    private boolean mousePressed;

    private Mouse() {
        // Prevent instantiation
    }

    public static Mouse getInstance() {
        return instance;
    }

    public boolean isDraggingBlock() {
        return draggingBlock;
    }

    public void setDraggingBlock(boolean draggingBlock) {
        this.draggingBlock = draggingBlock;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            mousePressed = true;
        }
    }

    public void handleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            mousePressed = false;
        }
    }

    public void handleMouseExited(MouseEvent event) {
        mousePressed = false;
    }
}