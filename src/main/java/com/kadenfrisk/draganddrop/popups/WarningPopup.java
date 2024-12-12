package com.kadenfrisk.draganddrop.popups;

import javax.swing.*;

public class WarningPopup {
    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
