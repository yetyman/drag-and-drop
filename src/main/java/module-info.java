module com.kadenfrisk.draganddrop {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.slf4j;
    exports com.kadenfrisk.draganddrop;
    exports com.kadenfrisk.draganddrop.custom;
    exports com.kadenfrisk.draganddrop.controllers;
}