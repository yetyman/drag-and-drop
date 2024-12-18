package com.kadenfrisk.draganddrop.custom;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.controllers.SettingsManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.slf4j.Logger;

public class MainMenuBar extends MenuBar {

    private static final Logger logger = App.getLogger();

    public MainMenuBar(SettingsManager settingsManager) {
        logger.info("MainMenuBar init...");
        Menu fileMenu = new Menu("File");
        fileMenu
            .getItems()
            .addAll(
                new MenuItem("New"),
                new MenuItem("Open"),
                new MenuItem("Save"),
                new SeparatorMenuItem(),
                new MenuItem("Exit")
            );

        Menu editMenu = new Menu("Edit");
        MenuItem options = new MenuItem("Options");
        options.setOnAction(event -> settingsManager.showSettingsDialog());
        editMenu.getItems().addAll(options);

        Menu viewMenu = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom In");
        zoomIn.setOnAction(event -> App.zoomIn());
        MenuItem zoomOut = new MenuItem("Zoom Out");
        zoomOut.setOnAction(event -> App.zoomOut());
        MenuItem resetZoom = new MenuItem("Reset Zoom");
        resetZoom.setOnAction(event -> App.resetZoom());
        viewMenu.getItems().addAll(zoomIn, zoomOut, resetZoom);

        getMenus().addAll(fileMenu, editMenu, viewMenu);
    }
}
