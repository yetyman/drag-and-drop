package com.kadenfrisk.draganddrop.controllers;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static javafx.application.Platform.runLater;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.popups.WarningPopup;
import java.io.File;

public class ThemeManager {

    private static final ThemeManager instance = new ThemeManager();
    private String currentTheme;

    private ThemeManager() {
        currentTheme = "debug";
    }

    public static ThemeManager getInstance() {
        return instance;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String theme) {
        currentTheme = theme;
        applyTheme();
    }

    public void applyTheme() {
        applyTheme(currentTheme);
    }

    public void applyTheme(String themeName) {
        // Do nothing if the theme is the same
        if (themeName.equals(currentTheme)) {
            return;
        }

        // Apply the theme without blocking the UI thread, prevents ConcurrentModificationException
        runLater(() -> {
            try {
                // Clear current theme
                App.getScene().getStylesheets().clear();
                String themePath = requireNonNull(
                    getClass().getResource("/themes/" + themeName + ".css")
                ).toExternalForm();
                // Apply the new theme
                App.getScene().getStylesheets().add(themePath);
            } catch (NullPointerException e) {
                // Handle missing theme, applying default
                App.getScene().getStylesheets().clear();
                App.getScene()
                    .getStylesheets()
                    .add(
                        requireNonNull(
                            getClass().getResource("/themes/light.css")
                        ).toExternalForm()
                    );
                WarningPopup.showWarning(
                    "Theme not found. Default theme applied."
                );
            }

            // Always apply all the css from the resources/shared/*.css
            applySharedStyles();
        });
    }

    private void applySharedStyles() {
        // Get a list of all the shared stylesheets
        File sharedDir = new File(
            requireNonNull(getClass().getResource("/sharedStyles")).getPath()
        );
        File[] files = sharedDir.listFiles();
        if (files == null) {
            return;
        }
        stream(files).forEach(file ->
            App.getScene().getStylesheets().add(file.toURI().toString())
        );
    }

    public String[] getThemes() {
        // Return a list of available themes from the theme directory in the resources
        String themeDir = requireNonNull(
            getClass().getResource("/themes")
        ).getPath();
        File[] files = new File(themeDir).listFiles();

        return stream(requireNonNull(files))
            .map(File::getName)
            .toArray(String[]::new);
    }
}
