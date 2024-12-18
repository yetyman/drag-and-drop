package com.kadenfrisk.draganddrop.models;

public class Mouse {

    private static final Mouse instance = new Mouse();
    private boolean draggingBlock;
    private Block selectedBlock;

    private Mouse() {}

    public static Mouse getInstance() {
        return instance;
    }

    public boolean isDraggingBlock() {
        return draggingBlock;
    }

    public void setDraggingBlock(boolean draggingBlock) {
        this.draggingBlock = draggingBlock;
    }

    public Block getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
    }
}
