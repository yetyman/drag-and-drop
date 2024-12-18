package com.kadenfrisk.draganddrop.models.blocks.control;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.controllers.BlockManager;
import com.kadenfrisk.draganddrop.models.Block;
import org.slf4j.Logger;

public class StopBlock extends Block {

    private static final Logger logger = App.getLogger();

    public StopBlock() {
        name = "End";
        getStyleClass().add("stop-block");
    }

    @Override
    public void run() {
        logger.info("Stopping program");
        BlockManager.getInstance().requestStop();
    }
}
