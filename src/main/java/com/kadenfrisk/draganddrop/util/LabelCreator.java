package com.kadenfrisk.draganddrop.util;

import javafx.scene.control.Label;

public class LabelCreator {

    public static Label createLabel(String name) {
        Label label = new Label(name);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }
}
