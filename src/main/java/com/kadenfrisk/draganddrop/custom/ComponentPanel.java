package com.kadenfrisk.draganddrop.custom;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.items.BlockLabel;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;

public class ComponentPanel {

    private static final Logger logger = App.getLogger();

    public static VBox getComponentPanel() {
        logger.info("Creating component panel...");
        return createComponentPalette();
    }

    private static VBox createComponentPalette() {
        logger.info("Creating component palette");
        VBox palette = new VBox(10);
        // ID for css styling
        palette.setId("component-palette");
        palette.setPadding(new Insets(10));
        palette.setMinWidth(200);

        palette
            .getChildren()
            .addAll(
                createComponentSection(
                    "Control Flow",
                    createControlFlowComponents()
                ),
                createComponentSection("Logic", createLogicComponents()),
                createComponentSection(
                    "Operations",
                    createOperationComponents()
                ),
                createComponentSection("Sensors", createSensorComponents()),
                createComponentSection("GUI", createGUIComponents())
            );

        ScrollPane scrollPane = new ScrollPane(palette);
        // ID for css styling
        scrollPane.setId("component-palette-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return new VBox(scrollPane);
    }

    private static VBox createGUIComponents() {
        VBox container = new VBox(5);
        container
            .getChildren()
            .addAll(createDraggableComponent("Dialog Block"));
        return container;
    }

    private static VBox createSensorComponents() {
        VBox container = new VBox(5);
        container
            .getChildren()
            .addAll(
                createDraggableComponent("Sensor"),
                createDraggableComponent("Sensor"),
                createDraggableComponent("Sensor"),
                createDraggableComponent("Sensor")
            );
        return container;
    }

    private static VBox createControlFlowComponents() {
        VBox container = new VBox(5);
        container
            .getChildren()
            .addAll(
                createDraggableComponent("Start Block"),
                createDraggableComponent("Stop Block"),
                createDraggableComponent("Wait Block")
            );
        return container;
    }

    private static TitledPane createComponentSection(
        String title,
        VBox content
    ) {
        TitledPane section = new TitledPane(title, content);
        section.setExpanded(true);

        // Class for css styling
        section.getStyleClass().add("component-section");

        // Allow dragging items over the section
        section.setOnDragOver(event -> {
            // logger.info("Dragging over section: {}", title);
            if (event.getGestureSource() != section) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });

        return section;
    }

    private static VBox createLogicComponents() {
        VBox container = new VBox(5);
        container
            .getChildren()
            .addAll(
                createDraggableComponent("If Block"),
                createDraggableComponent("If/Else Block"),
                createDraggableComponent("While Loop Block"),
                createDraggableComponent("For Loop Block"),
                createDraggableComponent("Switch Block"),
                createDraggableComponent("Boolean Expression Block")
            );
        return container;
    }

    private static VBox createOperationComponents() {
        VBox container = new VBox(5);
        container
            .getChildren()
            .addAll(
                createDraggableComponent("Math Block"),
                createDraggableComponent("Variable Block")
            );
        return container;
    }

    private static BlockLabel createDraggableComponent(String name) {
        return getComponent(name);
    }

    private static BlockLabel getComponent(String name) {
        return new BlockLabel(name);
    }
}
