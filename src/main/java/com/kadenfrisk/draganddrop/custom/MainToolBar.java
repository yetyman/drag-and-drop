package com.kadenfrisk.draganddrop.custom;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.controllers.BlockManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.slf4j.Logger;

import static com.kadenfrisk.draganddrop.App.*;

public class MainToolBar extends ToolBar {
    private static final Logger logger = App.getLogger();

    public MainToolBar(Grid grid) {
        super();
        logger.info("Init Zoom In button...");
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(this::handleZoomIn);

        logger.info("Init Zoom Out button...");
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(this::handleZoomOut);

        logger.info("Init draw viewport button...");
        Button drawViewport = new Button("Draw Viewport");
        drawViewport.setOnAction(event -> grid.drawRect(getViewportBounds()));

        logger.info("Init start button...");
        Button start = new Button("Start");
        start.setOnAction(event -> BlockManager.getInstance().run());

        logger.info("Adding items to MainToolBar");
        getItems().addAll(zoomIn, zoomOut, drawViewport, start);
    }

    private void handleZoomOut(ActionEvent actionEvent) {
        zoomOut();
        postZoom();
        actionEvent.consume();
    }

    private void handleZoomIn(ActionEvent actionEvent) {
        zoomIn();
        postZoom();
        actionEvent.consume();
    }
}
