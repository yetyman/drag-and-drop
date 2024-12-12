package com.kadenfrisk.draganddrop.custom;

import javafx.scene.Group;

public class MainGroup extends Group {
    public MainGroup(Grid grid) {
        super();
        getChildren().add(grid);
    }
}
