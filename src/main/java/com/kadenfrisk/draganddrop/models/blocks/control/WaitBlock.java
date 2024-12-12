package com.kadenfrisk.draganddrop.models.blocks.control;

import com.kadenfrisk.draganddrop.models.Block;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

public class WaitBlock extends Block {

    private final String name;

    public WaitBlock() {
        this.name = "Wait";

        // Add the name to the actual block to be displayed on screen
        this.getChildren().add(createLabel(name));

        // Set the block's css class
        this.getStyleClass().add("wait-block");
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
