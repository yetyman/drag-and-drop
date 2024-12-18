package com.kadenfrisk.draganddrop.models.blocks.logic;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;

public class ForLoopBlock extends Block {

    public ForLoopBlock() {
        name = "For";

        this.getStyleClass().add("for-loop-block");
        connectsTo.add(DialogBlock.class);
        connectsTo.add(IfBlock.class);
        connectsTo.add(ForLoopBlock.class);
        connectsTo.add(IfElseBlock.class);
        connectsTo.add(SwitchBlock.class);
        connectsTo.add(WhileLoopBlock.class);
        connectsTo.add(BooleanExpressionBlock.class);
    }
}
