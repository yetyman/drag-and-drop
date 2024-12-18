package com.kadenfrisk.draganddrop.models.blocks.sensors;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.util.LabelCreator;

public class SensorBlock extends Block {

    private final String name;

    public SensorBlock() {
        name = "Sensor";
        getChildren().add(LabelCreator.createLabel(name));
        getStyleClass().add("sensor-block");
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
