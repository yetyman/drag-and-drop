package com.kadenfrisk.draganddrop.custom;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.controllers.BlockManager;
import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.Mouse;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class Grid extends Pane {

    private static final Logger logger = App.getLogger();
    private final Canvas gridCanvas;

    public Grid(double gridWidth, double gridHeight) {
        super();
        gridCanvas = new Canvas(gridWidth, gridHeight);
        getChildren().add(gridCanvas);

        drawGrid();

        widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());

        setOnScroll(this::handleScroll);
        setOnDragOver(this::handleDragOver);
        setOnDragDropped(this::handleDragDropped);
        setOnMouseClicked(this::handleMouseClick);
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
        logger.info(
            "Mouse clicked at: ({}, {})",
            mouseEvent.getX(),
            mouseEvent.getY()
        );
    }

    private void handleDragDropped(DragEvent dragEvent) {
        Point2D point = new Point2D(dragEvent.getX(), dragEvent.getY());
        System.out.printf(
            "Drag dropped at (%f, %f)\n",
            point.getX(),
            point.getY()
        );

        // Log clipboard contents
        Dragboard db = dragEvent.getDragboard();
        if (db.hasContent(DataFormat.PLAIN_TEXT)) {
            String clipboardContent = db.getString();
            System.out.println("Clipboard content: " + clipboardContent);
            Block newBlock = BlockManager.getInstance()
                .createBlockByName(clipboardContent);
            newBlock.setText(newBlock.getName());

            newBlock.onCreation();

            getChildren().add(newBlock);
            newBlock.relocate(point);
        }
    }

    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            if (event.getDeltaY() > 0) {
                App.zoomIn();
            } else {
                App.zoomOut();
            }
            event.consume();
        } else {
            Mouse mouse = Mouse.getInstance();
            if (mouse.isDraggingBlock()) {
                Block selectedBlock = mouse.getSelectedBlock();
                if (selectedBlock != null) {
                    double deltaX = event.getDeltaX();
                    double deltaY = event.getDeltaY();

                    double newTranslateX =
                        selectedBlock.getTranslateX() - deltaX;
                    double newTranslateY =
                        selectedBlock.getTranslateY() - deltaY;

                    if (
                        newTranslateX >= 0 &&
                        newTranslateX + selectedBlock.getWidth() <=
                        gridCanvas.getWidth() &&
                        newTranslateY >= 0 &&
                        newTranslateY + selectedBlock.getHeight() <=
                        gridCanvas.getHeight()
                    ) {
                        selectedBlock.setTranslateX(newTranslateX);
                        selectedBlock.setTranslateY(newTranslateY);
                    }
                } else {
                    logger.error("Dragging block is null");
                }
            }
        }
    }

    public void drawGrid() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.setStroke(Color.rgb(100, 100, 100, 1.0));
        gc.setLineWidth(0.5);

        int gridSize = 20;
        for (double x = 0; x < gridCanvas.getWidth(); x += gridSize) {
            gc.strokeLine(x, 0, x, gridCanvas.getHeight());
        }

        for (double y = 0; y < gridCanvas.getHeight(); y += gridSize) {
            gc.strokeLine(0, y, gridCanvas.getWidth(), y);
        }
    }

    public void drawRect(Rectangle2D rectangle) {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();

        gc.setStroke(Color.RED);
        gc.setLineWidth(1.5);

        gc.strokeRect(
            rectangle.getMinX(),
            rectangle.getMinY(),
            rectangle.getWidth(),
            rectangle.getHeight()
        );
    }

    public void drawMouseLocation(MouseEvent e) {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
        drawGrid();

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1.0);

        gc.strokeLine(e.getX(), 0, e.getX(), gridCanvas.getHeight());
        gc.strokeLine(0, e.getY(), gridCanvas.getWidth(), e.getY());

        // Draw a cube around this point
        double squareSize = 100;
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.5);
        gc.strokeRect(
            e.getX() - squareSize / 2,
            e.getY() - squareSize / 2,
            squareSize,
            squareSize
        );
    }

    private void resizeCanvas() {
        gridCanvas.setWidth(getWidth());
        gridCanvas.setHeight(getHeight());

        drawGrid();
    }

    public ArrayList<Rectangle2D> getTriggers(MouseEvent e) {
        // Returns the same triggers as drawTriggers, but as Rectangle2D objects
        double triggerWidth = 20;
        Rectangle2D viewportBounds = App.getViewportBounds();

        ArrayList<Rectangle2D> triggers = new ArrayList<>();
        triggers.add(
            new Rectangle2D(
                viewportBounds.getMinX(),
                viewportBounds.getMinY(),
                triggerWidth,
                viewportBounds.getHeight()
            )
        );
        triggers.add(
            new Rectangle2D(
                viewportBounds.getMaxX() - triggerWidth,
                viewportBounds.getMinY(),
                triggerWidth,
                viewportBounds.getHeight()
            )
        );
        triggers.add(
            new Rectangle2D(
                viewportBounds.getMinX(),
                viewportBounds.getMinY(),
                viewportBounds.getWidth(),
                triggerWidth
            )
        );
        triggers.add(
            new Rectangle2D(
                viewportBounds.getMinX(),
                viewportBounds.getMaxY() - triggerWidth,
                viewportBounds.getWidth(),
                triggerWidth
            )
        );

        return triggers;
    }

    public Rectangle2D getLeftTrigger(MouseEvent e) {
        double triggerWidth = 20;
        Rectangle2D viewportBounds = App.getViewportBounds();
        return new Rectangle2D(
            viewportBounds.getMinX(),
            viewportBounds.getMinY(),
            triggerWidth,
            viewportBounds.getHeight()
        );
    }

    public Rectangle2D getRightTrigger(MouseEvent e) {
        double triggerWidth = 20;
        Rectangle2D viewportBounds = App.getViewportBounds();
        return new Rectangle2D(
            viewportBounds.getMaxX() - triggerWidth,
            viewportBounds.getMinY(),
            triggerWidth,
            viewportBounds.getHeight()
        );
    }

    public Rectangle2D getTopTrigger(MouseEvent e) {
        double triggerWidth = 20;
        Rectangle2D viewportBounds = App.getViewportBounds();
        return new Rectangle2D(
            viewportBounds.getMinX(),
            viewportBounds.getMinY(),
            viewportBounds.getWidth(),
            triggerWidth
        );
    }

    public Rectangle2D getBottomTrigger(MouseEvent e) {
        double triggerWidth = 20;
        Rectangle2D viewportBounds = App.getViewportBounds();
        return new Rectangle2D(
            viewportBounds.getMinX(),
            viewportBounds.getMaxY() - triggerWidth,
            viewportBounds.getWidth(),
            triggerWidth
        );
    }

    public void drawTriggers(MouseEvent e) {
        // Triggers are X amount wide rectangles on each side of the screen
        double triggerWidth = 20;
        // They will need to be drawn inside the viewport bounds
        Rectangle2D viewportBounds = App.getViewportBounds();
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.GREEN);

        // Left trigger
        gc.strokeRect(
            viewportBounds.getMinX(),
            viewportBounds.getMinY(),
            triggerWidth,
            viewportBounds.getHeight()
        );

        // Right trigger
        gc.strokeRect(
            viewportBounds.getMaxX() - triggerWidth,
            viewportBounds.getMinY(),
            triggerWidth,
            viewportBounds.getHeight()
        );

        // Top trigger
        gc.strokeRect(
            viewportBounds.getMinX(),
            viewportBounds.getMinY(),
            viewportBounds.getWidth(),
            triggerWidth
        );

        // Bottom trigger
        gc.strokeRect(
            viewportBounds.getMinX(),
            viewportBounds.getMaxY() - triggerWidth,
            viewportBounds.getWidth(),
            triggerWidth
        );
    }
}
