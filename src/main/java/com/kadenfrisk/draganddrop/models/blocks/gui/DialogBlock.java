package com.kadenfrisk.draganddrop.models.blocks.gui;

import static javax.swing.JOptionPane.showMessageDialog;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.logic.*;

public class DialogBlock extends Block {

    public DialogBlock() {
        name = "Dialog";
        getStyleClass().add("dialog-block");

        connectsTo.add(DialogBlock.class);
        connectsTo.add(IfBlock.class);
        connectsTo.add(ForLoopBlock.class);
        connectsTo.add(IfElseBlock.class);
        connectsTo.add(SwitchBlock.class);
        connectsTo.add(WhileLoopBlock.class);
    }

    @Override
    public void run() {
        showMessageDialog(null, "Hello, World!");
        runChildren();
    }
}
