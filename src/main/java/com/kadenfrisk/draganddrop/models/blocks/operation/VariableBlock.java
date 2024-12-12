package com.kadenfrisk.draganddrop.models.blocks.operation;

import com.kadenfrisk.draganddrop.models.Block;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

public class VariableBlock extends Block {

    private final String name;

    public VariableBlock() {
        this.name = "Variable";

        // Add the name to the actual block to be displayed on screen
        this.getChildren().add(createLabel(name));

        // Set the block's css class
        this.getStyleClass().add("variable-block");
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
