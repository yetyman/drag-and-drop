package com.kadenfrisk.draganddrop.models.blocks.gui;

import com.kadenfrisk.draganddrop.models.Block;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;
import static javax.swing.JOptionPane.showMessageDialog;

public class DialogBlock extends Block {
    private final String name;

    public DialogBlock() {
        this.name = "Dialog";

        // Add the name to the actual block to be displayed on screen
        this.getChildren().add(createLabel(name));

        // Set the block's css class
        this.getStyleClass().add("dialog-block");
    }

    @Override
    public void run() {
        // Show a simple popup dialog with JOptionPane
        showMessageDialog(null, "Hello, World!");
        runChildren();
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
