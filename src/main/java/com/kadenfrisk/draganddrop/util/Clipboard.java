package com.kadenfrisk.draganddrop.util;

import static javafx.scene.input.Clipboard.getSystemClipboard;

public class Clipboard {

    public static boolean hasClipboardManager() {
        return getSystemClipboard() != null;
    }
}
