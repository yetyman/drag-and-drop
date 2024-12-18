package com.kadenfrisk.draganddrop.models;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.custom.Draggable;
import com.kadenfrisk.draganddrop.popups.WarningPopup;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.slf4j.Logger;

public class Block extends Draggable {

    protected static final Logger logger = App.getLogger();
    private static final double CONNECTION_THRESHOLD = 100;
    private static final double DISCONNECT_THRESHOLD = 150;
    private final ContextMenu contextMenu;
    private final Block[] parents = new Block[1];
    private final Block[] children = new Block[1];

    @SuppressWarnings("rawtypes")
    protected ArrayList<Class> connectsTo = new ArrayList<>(); // Controls this blocks you can connect to this block

    protected String name;
    private Line connectionLine;

    public Block() {
        super("Block", App.getWorkspaceScroll(), App.getGrid(), 100, 100);
        name = "Block";
        contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDelete);
        contextMenu.getItems().add(deleteItem);
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.isSecondaryButtonDown()) contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });

        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);
    }

    @Override
    protected void handleMousePressed(MouseEvent event) {
        if (parents[0] != null) {
            parents[0].handleMousePressed(event);
            return;
        }

        super.handleMousePressed(event);
        this.setCursor(Cursor.CLOSED_HAND);
        updateMouse();
        onBlockPressed(event);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        // If the block has a parent block, use the parent's drag event instead
        if (parents[0] != null) {
            parents[0].handleMouseDragged(event);
            return;
        }

        super.handleMouseDragged(event);
        updateMouse();
        updateConnectionLine();
        checkAndDisconnect();
    }

    private void updateMouse() {
        Mouse mouse = Mouse.getInstance();

        mouse.setSelectedBlock(this);
        mouse.setDraggingBlock(true);
    }

    private void handleMouseReleased(MouseEvent event) {
        this.setCursor(Cursor.DEFAULT);
        Mouse.getInstance().setDraggingBlock(false);

        snapToGrid();

        Block closestBlock = null;
        if (this.getParent() instanceof Pane parentPane) {
            closestBlock = findClosestBlock(parentPane);
            if (closestBlock != null && isWithinConnectionThreshold(closestBlock)) {
                App.getLogger().info("Connecting blocks {} and {}", this.getName(), closestBlock.getName());
                connectTo(closestBlock);
            }
        }

        onBlockReleased(event, closestBlock);

        Mouse.getInstance().setSelectedBlock(null);
        Mouse.getInstance().setDraggingBlock(false);
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
        onBlockRemoved();
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
    }

    /**
     * Override this method to add custom behavior when the block is released
     *
     * @param event        The mouse event that triggered the release
     * @param nearestBlock The closest block found during release (maybe null)
     */
    @SuppressWarnings("unused")
    protected void onBlockReleased(MouseEvent event, Block nearestBlock) {
    }

    private void updateConnectionLine() {
        if (connectionLine != null) {
            if (parents[0] != null) {
                connectionLine.setStartX(parents[0].getLayoutX() + parents[0].getWidth() / 2);
                connectionLine.setStartY(parents[0].getLayoutY() + parents[0].getHeight());
                connectionLine.setEndX(this.getLayoutX() + this.getWidth() / 2);
                connectionLine.setEndY(this.getLayoutY());
            } else if (children[0] != null) {
                connectionLine.setStartX(this.getLayoutX() + this.getWidth() / 2);
                connectionLine.setStartY(this.getLayoutY() + this.getHeight());
                connectionLine.setEndX(children[0].getLayoutX() + children[0].getWidth() / 2);
                connectionLine.setEndY(children[0].getLayoutY());
            }
        }
    }

    private void checkAndDisconnect() {
        if (parents[0] != null) {
            double distance = calculateDistance(this, parents[0]);

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

        logger.info("{} disconnected from {}", this.getName(), parentBlock.getName());
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

    // Method to get the greatest grandparent block
    public Block getGreatestGrandParent() {
        Block current = this;
        while (current.parents[0] != null) {
            current = current.parents[0];
        }
        return current;
    }

    // Method to count all child blocks
    public int countChildBlocks() {
        int count = 0;
        if (children[0] != null) {
            count += 1 + children[0].countChildBlocks();
        }
        return count;
    }

    private void connectTo(Block parent) {
        if (!parent.connectsTo.contains(this.getClass())) {
            logger.info("{} cannot connect to {}", this.getName(), parent.getName());
            WarningPopup.showWarning("Invalid Connection: Cannot connect " + this.getName() + " to " + parent.getName());
            return;
        }

        if (parent == this || isDescendant(parent)) {
            logger.info("Cannot connect {} to itself or its descendant {}", this.getName(), parent.getName());
            WarningPopup.showWarning("Invalid Connection: Cannot connect " + this.getName() + " to itself or its descendant");
            return;
        }

        if (parents[0] != null) {
            disconnectFromParent();
        }

        Block lastChild = parent;
        while (lastChild.children[0] != null) {
            lastChild = lastChild.children[0];
        }

        lastChild.children[0] = this;
        this.parents[0] = lastChild;

        if (this.getParent() instanceof Pane oldParentPane) {
            oldParentPane.getChildren().remove(this);
        }

        lastChild.getChildren().add(this);

        this.applyCss();
        this.layout();

        // Update heights dynamically up the chain
        updateParentHeights(this);

        VBox.setMargin(this, new Insets(5, 0, 0, 0));

        logger.info("{} connected to {}", this.getName(), lastChild.getName());
    }

    /**
     * Updates the height of all parent blocks dynamically.
     * Ensures the last child block stays at its default size.
     */
    private void updateParentHeights(Block childBlock) {
        Block currentBlock = childBlock;
        int childDepth = 0;

        // Traverse upward, updating parent block sizes
        while (currentBlock != null) {
            double blockHeight = getHeight();

            // If this is the last child block, set it to default size
            if (childDepth == 0) {
                currentBlock.setPrefHeight(blockHeight); // Default size for last child block
            } else {
                // Resize parent blocks dynamically
                double newHeight = blockHeight * (currentBlock.countChildBlocks() + 1 - childDepth);
                currentBlock.setPrefHeight(newHeight);
            }

            childDepth++;
            currentBlock = currentBlock.parents[0];
        }
    }

    private boolean isDescendant(Block block) {
        Block current = this;
        while (current != null) {
            if (current == block) {
                return true;
            }
            current = current.parents[0];
        }
        return false;
    }

    public void runChildren() {
        if (children[0] != null) {
            children[0].run();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Method called when the block is removed from the workspace
     */
    protected void onBlockRemoved() {
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
