package com.kadenfrisk.draganddrop.models.blocks.logic;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;
import com.kadenfrisk.draganddrop.models.blocks.operation.VariableBlock;

public class WhileLoopBlock extends Block {

    private final String name;

    public WhileLoopBlock() {
        this.name = "While";

        // Add the name to the actual block to be displayed on screen
        this.getChildren().add(createLabel(name));

        // Set the block's css class
        this.getStyleClass().add("while-loop-block");

        connectsTo.add(DialogBlock.class);
        connectsTo.add(IfBlock.class);
        connectsTo.add(ForLoopBlock.class);
        connectsTo.add(IfElseBlock.class);
        connectsTo.add(SwitchBlock.class);
        connectsTo.add(WhileLoopBlock.class);
        connectsTo.add(BooleanExpressionBlock.class);
        connectsTo.add(VariableBlock.class);
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
