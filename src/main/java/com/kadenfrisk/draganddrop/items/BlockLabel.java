package com.kadenfrisk.draganddrop.items;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class BlockLabel extends Label {

    private final String name;

    public BlockLabel(String text) {
        super(text);
        this.name = text;

        setOnMouseEntered(event -> setCursor(Cursor.HAND));

        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.ANY);
            if (hasClipboardManager()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(name);
                db.setContent(content);
            } else {
                System.out.println("No clipboard manager available");
            }
            event.consume();
        });

        getStyleClass().add("block-label");
    }

    private boolean hasClipboardManager() {
        return Clipboard.getSystemClipboard() != null;
    }
}
