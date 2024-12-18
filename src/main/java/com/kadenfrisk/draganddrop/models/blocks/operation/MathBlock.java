package com.kadenfrisk.draganddrop.models.blocks.operation;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.util.LabelCreator;

public class MathBlock extends Block {

    private final String name;

    public MathBlock() {
        name = "Math";
        getChildren().add(LabelCreator.createLabel(name));
        getStyleClass().add("math-block");
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
