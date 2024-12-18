package com.kadenfrisk.draganddrop.models.blocks.logic;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;

public class SwitchBlock extends Block {

    private final String name;

    public SwitchBlock() {
        name = "Switch";

        this.getStyleClass().add("switch-block");

        connectsTo.add(DialogBlock.class);
        connectsTo.add(IfBlock.class);
        connectsTo.add(ForLoopBlock.class);
        connectsTo.add(IfElseBlock.class);
        connectsTo.add(SwitchBlock.class);
        connectsTo.add(WhileLoopBlock.class);
        connectsTo.add(BooleanExpressionBlock.class);
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
