package com.kadenfrisk.draganddrop.models.blocks.control;

import com.kadenfrisk.draganddrop.controllers.BlockManager;
import com.kadenfrisk.draganddrop.models.Block;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

public class StartBlock extends Block {

    private final String name;

    public StartBlock() {
        this.name = "Start";

        // Add the name to the actual block to be displayed on screen
        this.getChildren().add(createLabel(name));

        // Set the blocks css class
        this.getStyleClass().add("start-block");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void onCreation() {
        // Add the block to the block manager
        BlockManager.getInstance().addStartBlock(this);
    }
}
