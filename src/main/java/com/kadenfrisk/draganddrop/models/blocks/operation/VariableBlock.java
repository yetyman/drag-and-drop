package com.kadenfrisk.draganddrop.models.blocks.operation;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.util.LabelCreator;

public class VariableBlock extends Block {

    private final String name;

    public VariableBlock() {
        name = "Variable";
        getChildren().add(LabelCreator.createLabel(name));
        getStyleClass().add("variable-block");
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
