package com.kadenfrisk.draganddrop.models.blocks.control;

import com.kadenfrisk.draganddrop.models.Block;

public class WaitBlock extends Block {

    public WaitBlock() {
        name = "Wait";

        //  TODO: Add field for stop duration

        getStyleClass().add("wait-block");
    }

    @Override
    public void run() {
        int stopInSeconds = 5;

        try {
            Thread.sleep(stopInSeconds * 1000);
        } catch (InterruptedException e) {
            logger.error("Error while waiting :<", e);
        }
        runChildren();
    }
}
