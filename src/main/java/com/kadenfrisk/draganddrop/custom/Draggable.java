package com.kadenfrisk.draganddrop.custom;

import static com.kadenfrisk.draganddrop.util.LabelCreator.createLabel;

import com.kadenfrisk.draganddrop.App;
import com.kadenfrisk.draganddrop.util.Geometry;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Draggable extends VBox {

    private final Grid grid;
    private final ScrollPane scrollPane;
    private final Label titleText;
    private double x, y;

    public Draggable(
        String text,
        ScrollPane scrollPane,
        Grid grid,
        double width,
        double height
    ) {
        titleText = createLabel(text);
        getChildren().add(titleText);
        setPrefSize(width, height);
        this.grid = grid;
        this.scrollPane = scrollPane;

        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
    }

    protected void handleMousePressed(MouseEvent e) {
        x = e.getSceneX();
        y = e.getSceneY();
        e.consume();
    }

    protected void handleMouseDragged(MouseEvent e) {
        double dx = (e.getSceneX() - x) / Geometry.getZoomFactor();
        double dy = (e.getSceneY() - y) / Geometry.getZoomFactor();

        double newX = getLayoutX() + dx;
        double newY = getLayoutY() + dy;

        Rectangle2D viewportBounds = App.getViewportBounds();

        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();
        double hDistance =
            hValue * (grid.getWidth() - viewportBounds.getWidth());
        double vDistance =
            vValue * (grid.getHeight() - viewportBounds.getHeight());

        if (newX < 0) {
            newX = 0;
        } else if (newX + getWidth() > grid.getWidth()) {
            newX = grid.getWidth() - getWidth();
        } else if (newX < hDistance) {
            newX = hDistance;
        } else if (newX + getWidth() > hDistance + viewportBounds.getWidth()) {
            newX = hDistance + viewportBounds.getWidth() - getWidth();
        } else {
            if (newX < hDistance + 10) {
                scrollPane.setHvalue(hValue - 0.01);
            } else if (
                newX + getWidth() > hDistance + viewportBounds.getWidth() - 10
            ) {
                scrollPane.setHvalue(hValue + 0.01);
            }
        }

        if (newY < 0) {
            newY = 0;
        } else if (newY + getHeight() > grid.getHeight()) {
            newY = grid.getHeight() - getHeight();
        } else if (newY < vDistance) {
            newY = vDistance;
        } else if (
            newY + getHeight() > vDistance + viewportBounds.getHeight()
        ) {
            newY = vDistance + viewportBounds.getHeight() - getHeight();
        } else {
            if (newY < vDistance + 10) {
                scrollPane.setVvalue(vValue - 0.01);
            } else if (
                newY + getHeight() > vDistance + viewportBounds.getHeight() - 10
            ) {
                scrollPane.setVvalue(vValue + 0.01);
            }
        }

        setLayoutX(newX);
        setLayoutY(newY);

        x = e.getSceneX();
        y = e.getSceneY();
    }

    public void relocate(Point2D point) {
        relocate(point.getX(), point.getY());
    }

    public void setText(String name) {
        titleText.setText(name);
    }

    protected Label getTitle() {
        return titleText;
    }
}
