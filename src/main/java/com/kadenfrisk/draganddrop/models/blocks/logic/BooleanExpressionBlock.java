package com.kadenfrisk.draganddrop.models.blocks.logic;

import com.kadenfrisk.draganddrop.models.Block;
import javafx.scene.control.Label;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

public class BooleanExpressionBlock extends Block {

    private final String name;

    public BooleanExpressionBlock() {
        this.name = "Boolean Expression";

        // Add the name to the actual block to be displayed on screen
        Label label = createLabel(name);
        // Shrink the font size to fit the block
        label.setStyle("-fx-font-size: 10");
        this.getChildren().add(label);

        // Set the block's css class
        this.getStyleClass().add("boolean-expression-block");
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
