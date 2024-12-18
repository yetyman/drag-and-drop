package com.kadenfrisk.draganddrop.models.blocks.logic;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;
import com.kadenfrisk.draganddrop.models.blocks.operation.VariableBlock;

public class IfElseBlock extends Block {

    private final String name;

    public IfElseBlock() {
        name = "If Else";
        this.getStyleClass().add("if-else-block");

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
