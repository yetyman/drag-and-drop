package com.kadenfrisk.draganddrop.models.blocks.control;

import com.kadenfrisk.draganddrop.controllers.BlockManager;
import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.ForLoopBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.IfBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.IfElseBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.SwitchBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.WhileLoopBlock;

public class StartBlock extends Block {

    public StartBlock() {
        name = "Start";

        getStyleClass().add("start-block");

        /*
            Connections rules are this to child. Any blocks that you attempt to add to
            the blocks connections in the workspace will only connect if the block you
            are trying to connect to is in the connectsTo list.
         */
        connectsTo.add(DialogBlock.class);
        connectsTo.add(IfBlock.class);
        connectsTo.add(ForLoopBlock.class);
        connectsTo.add(IfElseBlock.class);
        connectsTo.add(SwitchBlock.class);
        connectsTo.add(WhileLoopBlock.class);
        connectsTo.add(WaitBlock.class);
    }

    @Override
    protected void onBlockRemoved() {
        BlockManager.getInstance().removeStartBlock(this);
    }

    @Override
    public void onCreation() {
        BlockManager.getInstance().addStartBlock(this);
    }
}
