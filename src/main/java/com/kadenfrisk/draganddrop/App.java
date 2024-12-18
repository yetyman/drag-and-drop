package com.kadenfrisk.draganddrop;

import static com.kadenfrisk.draganddrop.util.Geometry.*;

import com.kadenfrisk.draganddrop.controllers.SettingsManager;
import com.kadenfrisk.draganddrop.custom.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final double ZOOM_IN_BOUND = 2.0;
    private static final double ZOOM_OUT_BOUND = 0.5;
    private static final double ZOOM_AMOUNT = 0.05;
    private static Grid grid;
    private static BetterScrollPane scrollPane;
    private static Scene scene;

    public static void main(String[] args) {
        logger.info("Starting application...");
        launch();
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        VBox items = ComponentPanel.getComponentPanel();
        SplitPane splitPane = new SplitPane();

        ScrollPane itemsScroller = new ScrollPane(items);
        BorderPane borderPane = new BorderPane();

        scrollPane = new BetterScrollPane();

        Group group = new Group();

        grid = new Grid(6400, 4800);
        group.getChildren().add(grid);

        scrollPane.setContent(group);

        scrollPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            logger.info(
                "Dragging inside ScrollPane at: {}, {}",
                mouseX,
                mouseY
            );
        });

        ChangeListener<Number> scrollListener = (
            observable,
            oldValue,
            newValue
        ) -> {
            double hValue = scrollPane.getHvalue();
            double vValue = scrollPane.getVvalue();
            double hDistance = hValue * grid.getHeight();
            double vDistance = vValue * grid.getWidth();
            logger.info(
                "Scrolled distance - Horizontal: {}, Vertical: {}",
                hDistance,
                vDistance
            );
        };

        scrollPane.hvalueProperty().addListener(scrollListener);
        scrollPane.vvalueProperty().addListener(scrollListener);

        root.getChildren().add(scrollPane);
        splitPane.getItems().addAll(itemsScroller, root);
        splitPane.setDividerPosition(0, 0.2);

        borderPane.setLeft(itemsScroller);
        borderPane.setCenter(splitPane);

        scene = new Scene(borderPane, 960.0, 540.0);

        stage.setTitle("Zoom-able Content");
        stage.setScene(scene);

        SettingsManager settingsManager = new SettingsManager();
        settingsManager.loadSettings();

        Button settings = new Button("Settings");
        settings.setOnAction(event -> settingsManager.showSettingsDialog());

        VBox tools = new VBox();
        tools
            .getChildren()
            .addAll(new MainMenuBar(settingsManager), new MainToolBar(grid));
        borderPane.setTop(tools);

        stage.show();
    }

    public static void resetZoom() {
        setZoom(1.0);
    }

    public static Scene getScene() {
        if (scene == null) {
            throw new IllegalStateException(
                "Scene has not been initialized yet."
            );
        }
        return scene;
    }

    private static boolean withinZoomBounds(double newZoom) {
        return newZoom > ZOOM_IN_BOUND || newZoom < ZOOM_OUT_BOUND;
    }

    public static void zoomIn() {
        setZoom(getZoomFactor() + ZOOM_AMOUNT);
    }

    public static void zoomOut() {
        setZoom(getZoomFactor() - ZOOM_AMOUNT);
    }

    private static void setZoom(double newZoom) {
        if (!withinZoomBounds(newZoom)) {
            setZoomFactor(newZoom);
            Scale scale = new Scale(newZoom, newZoom);
            grid.getTransforms().setAll(scale);
            grid.drawGrid();
        }
    }

    public static Rectangle2D getViewportBounds() {
        return scaleRectangle(getViewPortBounds(scrollPane), getZoomFactor());
    }

    public static Grid getGrid() {
        return grid;
    }

    public static BetterScrollPane getWorkspaceScroll() {
        return scrollPane;
    }

    public static void postZoom() {
        fixScrollPosition();
        grid.drawGrid();
    }

    private static void fixScrollPosition() {
        Point2D topLeft = getTopLeft(scrollPane);
        Point2D bottomRight = getBottomRight(scrollPane);

        if (topLeft.getX() < 0) {
            scrollPane.setHvalue(0);
        }
        if (topLeft.getY() < 0) {
            scrollPane.setVvalue(0);
        }
        if (bottomRight.getX() > grid.getWidth()) {
            scrollPane.setHvalue(1);
        }
        if (bottomRight.getY() > grid.getHeight()) {
            scrollPane.setVvalue(1);
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
