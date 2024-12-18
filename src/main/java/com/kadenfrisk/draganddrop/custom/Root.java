package com.kadenfrisk.draganddrop.custom;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.controllers.SettingsManager;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;

public class Root extends BorderPane {

    private static final Logger logger = App.getLogger();

    public Root(
        Grid grid,
        BetterScrollPane scrollPane,
        SettingsManager settingsManager
    ) {
        super();
        logger.info("Init ComponentPanel...");
        VBox componentPanel = ComponentPanel.getComponentPanel();

        logger.info("Init MainGroup...");
        MainGroup group = new MainGroup(grid);

        logger.info("Setting scrollPane content to group...");
        scrollPane.setContent(group);

        logger.info("Init MainPane...");
        setLeft(componentPanel);

        logger.info("Init topContainer...");
        VBox topContainer = new VBox();

        logger.info("Init MainMenuBar...");
        MainMenuBar menuBar = new MainMenuBar(settingsManager);

        logger.info("Init MainToolBar...");
        MainToolBar toolBar = new MainToolBar(grid);

        topContainer.getChildren().addAll(menuBar, toolBar);

        logger.info("Setting top to topContainer...");
        setTop(topContainer);

        logger.info("Adding children to root...");
        setCenter(scrollPane);
    }
}
