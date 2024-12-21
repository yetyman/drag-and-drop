package com.kadenfrisk.draganddrop.util;

import com.kadenfrisk.draganddrop.models.Block;

public class DragOut {

    private static final DragOut instance = new DragOut();
    private Block currentBlock;

    private DragOut() {}

    public static DragOut getInstance() {
        return instance;
    }

    public Block getCurrentBlock() {
        Block block = currentBlock;
        currentBlock = null;
        return block;
    }

    public void setCurrentBlock(Block block) {
        currentBlock = block;
    }
}
