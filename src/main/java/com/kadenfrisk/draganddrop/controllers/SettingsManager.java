package com.kadenfrisk.draganddrop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kadenfrisk.draganddrop.App;
import org.slf4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kadenfrisk.draganddrop.util.FileNameTools.removeExtensions;

public class SettingsManager {
    private static final String SETTINGS_FILE = findSettingsFile(); // Path to settings file. MUST BE RUN FIRST
    private static final Logger logger = App.getLogger();
    private Map<String, Object> settings = new HashMap<>();
    private final ThemeManager themeManager;

    /**
     * Constructor for SettingsManager
     * Loads settings from file if it exists, otherwise starts with defaults
     */
    public SettingsManager() {
        themeManager = ThemeManager.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            try {
                settings = mapper.readValue(file, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Settings file not found. Starting with defaults.");
        }
    }

    private static String findSettingsFile() {
        File file = new File("settings.json");
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        String home = System.getProperty("user.home");
        file = new File(home, "settings.json");
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        String[] choices = {"Create in current directory", "Create in home directory"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Settings file not found. Would you like to create one?",
                "Settings File",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0]
        );

        return choice == 0 ? "settings.json" : new File(System.getProperty("user.home"), "settings.json").getAbsolutePath();
    }

    public Object get(String key, Object defaultValue) {
        return settings.getOrDefault(key, defaultValue);
    }

    public void set(String key, Object value) {
        settings.put(key, value);
        saveSettings();
    }

    public void saveSettings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(SETTINGS_FILE), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings() {
        // Load settings from file
        logger.info("Loading settings...");

        // Check if "theme" setting is present
        if (!settings.containsKey("theme")) {
            // If not, set it to "light"
            settings.put("theme", "light");
        }
        //Load the theme setting
        themeManager.applyTheme(settings.get("theme").toString());
    }

    // Method to launch a GUI dialog to change settings
    public void showSettingsDialog() {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel label = new JLabel("Theme:");
        panel.add(label);

        String[] themes = removeExtensions(themeManager.getThemes());
        JComboBox<String> comboBox = new JComboBox<>(themes);
        comboBox.setSelectedItem(settings.get("theme"));
        panel.add(comboBox);

        JButton button = new JButton("Save");
        button.addActionListener(e -> {
            settings.put("theme", comboBox.getSelectedItem());
            saveSettings();
            themeManager.applyTheme(settings.get("theme").toString());
            frame.dispose();
        });
        panel.add(button);

        frame.setVisible(true);
    }
}
