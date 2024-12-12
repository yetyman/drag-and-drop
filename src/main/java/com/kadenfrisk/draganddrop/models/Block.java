package com.kadenfrisk.draganddrop.models;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.custom.DraggableLabel;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Block extends DraggableLabel {
    private static final double CONNECTION_THRESHOLD = 100; // Max distance for auto-connection
    private static final double DISCONNECT_THRESHOLD = 150; // Distance to force disconnect
    private final ContextMenu contextMenu;
    private final Block[] parents = new Block[1];
    private final Block[] children = new Block[1];
    private Line connectionLine;

    public Block() {
        super("Block", App.getWorkspaceScroll(), App.getGrid(), 100, 100);
        contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDelete);
        contextMenu.getItems().add(deleteItem);
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.isSecondaryButtonDown()) contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });

        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        super.handleMouseDragged(event);
        updateConnectionLine();
        checkAndDisconnect();
    }

    private void handleMouseReleased(MouseEvent event) {
        this.setCursor(Cursor.DEFAULT);
        Mouse.getInstance().setDraggingBlock(false);

        // Snap to grid
        snapToGrid();

        // Check for connection with other blocks
        Block closestBlock = null;
        if (this.getParent() instanceof Pane parentPane) {
            closestBlock = findClosestBlock(parentPane);
            if (closestBlock != null && isWithinConnectionThreshold(closestBlock)) {
                App.getLogger().info("Connecting blocks {} and {}", this.getName(), closestBlock.getName());
                connectTo(closestBlock);
            }
        }

        // Allow custom handling of block release
        onBlockReleased(event, closestBlock);
    }

    private void handleDelete(ActionEvent event) {
        if (this.getParent() instanceof Pane parentPane) {
            if (connectionLine != null) {
                parentPane.getChildren().remove(connectionLine);
                connectionLine = null;
            }
            parentPane.getChildren().remove(this);
            onBlockRemoved();
        }
    }

    public void run() {
        System.out.println(this.getName());
        runChildren();
    }

    /**
     * Override this method to add custom behavior when the block is first pressed
     *
     * @param event The mouse event that triggered the press
     */
    @SuppressWarnings("unused")
    protected void onBlockPressed(MouseEvent event) {
        // Default implementation does nothing
    }

    /**
     * Override this method to add custom behavior during block dragging
     *
     * @param event  The mouse event that triggered the drag
     * @param deltaX The horizontal distance moved
     * @param deltaY The vertical distance moved
     */
    @SuppressWarnings("unused")
    protected void onBlockDragged(MouseEvent event, double deltaX, double deltaY) {
        // Default implementation does nothing
    }

    /**
     * Override this method to add custom behavior when the block is released
     *
     * @param event        The mouse event that triggered the release
     * @param nearestBlock The closest block found during release (maybe null)
     */
    @SuppressWarnings("unused")
    protected void onBlockReleased(MouseEvent event, Block nearestBlock) {
        // Default implementation does nothing
    }

    private void updateConnectionLine() {
        if (connectionLine != null) {
            if (parents[0] != null) {
                // Update line from parent to this block
                connectionLine.setStartX(parents[0].getLayoutX() + parents[0].getWidth() / 2);
                connectionLine.setStartY(parents[0].getLayoutY() + parents[0].getHeight());
                connectionLine.setEndX(this.getLayoutX() + this.getWidth() / 2);
                connectionLine.setEndY(this.getLayoutY());
            } else if (children[0] != null) {
                // Update line from this block to child
                connectionLine.setStartX(this.getLayoutX() + this.getWidth() / 2);
                connectionLine.setStartY(this.getLayoutY() + this.getHeight());
                connectionLine.setEndX(children[0].getLayoutX() + children[0].getWidth() / 2);
                connectionLine.setEndY(children[0].getLayoutY());
            }
        }
    }

    private void checkAndDisconnect() {
        // If this block has a parent
        if (parents[0] != null) {
            double distance = calculateDistance(this, parents[0]);

            // Disconnect if dragged too far from parent
            if (distance > DISCONNECT_THRESHOLD) {
                disconnectFromParent();
            }
        }
    }

    private void disconnectFromParent() {
        if (parents[0] == null) return;

        Block parentBlock = parents[0];

        // Remove the connection line
        if (this.getParent() instanceof Pane parentPane && connectionLine != null) {
            parentPane.getChildren().remove(connectionLine);
            connectionLine = null;
        }

        // Clear parent-child references
        parentBlock.children[0] = null;
        this.parents[0] = null;

        System.out.println(this.getName() + " disconnected from " + parentBlock.getName());
    }

    private double calculateDistance(Block block1, Block block2) {
        double centerX1 = block1.getLayoutX() + block1.getWidth() / 2;
        double centerY1 = block1.getLayoutY() + block1.getHeight() / 2;
        double centerX2 = block2.getLayoutX() + block2.getWidth() / 2;
        double centerY2 = block2.getLayoutY() + block2.getHeight() / 2;

        return Math.sqrt(Math.pow(centerX2 - centerX1, 2) + Math.pow(centerY2 - centerY1, 2));
    }

    private Block findClosestBlock(Pane parentPane) {
        Block closestBlock = null;
        double minDistance = Double.MAX_VALUE;

        for (var node : parentPane.getChildren()) {
            if (node instanceof Block targetBlock && targetBlock != this) {
                double distance = calculateDistance(this, targetBlock);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestBlock = targetBlock;
                }
            }
        }

        return closestBlock;
    }

    private boolean isWithinConnectionThreshold(Block other) {
        return calculateDistance(this, other) <= CONNECTION_THRESHOLD;
    }

    private void snapToGrid() {
        double gridSize = 20;
        double snappedX = Math.round(this.getLayoutX() / gridSize) * gridSize;
        double snappedY = Math.round(this.getLayoutY() / gridSize) * gridSize;
        this.setLayoutX(snappedX);
        this.setLayoutY(snappedY);
    }

    private void connectTo(Block parent) {
        // Disconnect any existing connections
        if (parents[0] != null) {
            disconnectFromParent();
        }
        if (parent.children[0] != null) {
            parent.children[0].disconnectFromParent();
        }

        // Update parent-child relationship
        parent.children[0] = this;
        this.parents[0] = parent;

        // Create a connection line
        if (this.getParent() instanceof Pane parentPane) {
            connectionLine = new Line(parent.getLayoutX() + parent.getWidth() / 2, parent.getLayoutY() + parent.getHeight(), this.getLayoutX() + this.getWidth() / 2, this.getLayoutY());
            connectionLine.setStroke(Color.BLUE);
            connectionLine.setStrokeWidth(2);
            parentPane.getChildren().add(connectionLine);
        }

        // Snap visually to the parent block (Inside the parent block), shifted down a bit
        this.setLayoutX(parent.getLayoutX() + 1);
        this.setLayoutY(parent.getLayoutY() + parent.getHeight() + 1);

        System.out.println(this.getName() + " connected to " + parent.getName());
    }

    // Call block children run method
    public void runChildren() {
        if (children[0] != null) {
            children[0].run();
        }
    }

    public String getName() {
        return "Block";
    }

    // Method called when you delete the block from the workspace
    protected void onBlockRemoved() {
        // Default implementation does nothing
    }

    /**
     * Method called when the block is created and added to the workspace
     * Override this method to add custom behavior when the block is created
     */
    @SuppressWarnings("unused")
    public void onCreation() {
        // Default implementation does nothing
    }
}