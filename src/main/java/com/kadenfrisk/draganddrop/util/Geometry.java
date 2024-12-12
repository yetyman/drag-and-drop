package com.kadenfrisk.draganddrop.util;

import com.kadenfrisk.draganddrop.custom.BetterScrollPane;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.Objects;

public class Geometry {

    private static double zoomFactor = 1.0;

    public static double getZoomFactor() {
        return zoomFactor;
    }

    public static void setZoomFactor(double zoomFactor) {
        Geometry.zoomFactor = zoomFactor;
    }


    public static Rectangle2D getViewPortBounds(BetterScrollPane betterScrollPane) {
        Objects.requireNonNull(betterScrollPane);

        double viewportWidth = betterScrollPane.getViewportBounds().getWidth();
        double viewportHeight = betterScrollPane.getViewportBounds().getHeight();

        // Scale the content dimensions by the zoom factor
        double contentWidth = betterScrollPane.getContent().getBoundsInLocal().getWidth() * zoomFactor;
        double contentHeight = betterScrollPane.getContent().getBoundsInLocal().getHeight() * zoomFactor;

        double hValue = betterScrollPane.getHvalue();
        double vValue = betterScrollPane.getVvalue();

        double x = hValue * (contentWidth - viewportWidth) / zoomFactor;
        double y = vValue * (contentHeight - viewportHeight) / zoomFactor;

        if (zoomFactor != 1.0) {
            x = x / zoomFactor;
            y = y / zoomFactor;
        }

        return new Rectangle2D(x, y, viewportWidth, viewportHeight);
    }


    public static Point2D getTopLeft(BetterScrollPane betterScrollPane) {
        Rectangle2D viewport = getViewPortBounds(betterScrollPane);
        return new Point2D(viewport.getMinX(), viewport.getMinY());
    }

    public static Point2D getBottomRight(BetterScrollPane betterScrollPane) {
        Rectangle2D viewport = getViewPortBounds(betterScrollPane);
        return new Point2D(viewport.getMaxX(), viewport.getMaxY());
    }

    public static Rectangle2D scaleRectangle(Rectangle2D original, double zoomFactor) {
        double minX = original.getMinX();
        double minY = original.getMinY();
        double originalWidth = original.getWidth();
        double originalHeight = original.getHeight();

        double scaleFactor = 1.0 / zoomFactor;

        double newWidth = originalWidth * scaleFactor;
        double newHeight = originalHeight * scaleFactor;

        return new Rectangle2D(minX, minY, newWidth, newHeight);
    }
}
