module com.example.mangatool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.example.mangatool to javafx.fxml;
    exports com.example.mangatool;
    exports com.example.mangatool.ui;
    opens com.example.mangatool.ui to javafx.fxml;
    exports com.example.mangatool.ui.component;
    opens com.example.mangatool.ui.component to javafx.fxml;
    exports com.example.mangatool.common;
    opens com.example.mangatool.common to javafx.fxml;
}