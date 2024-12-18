package com.kadenfrisk.draganddrop.models.blocks.logic;

import com.kadenfrisk.draganddrop.models.Block;

public class BooleanExpressionBlock extends Block {

    public BooleanExpressionBlock() {
        name = "Boolean Expression";

        super.getTitle().setStyle("-fx-font-size: 10");
        getStyleClass().add("boolean-expression-block");
    }
}
