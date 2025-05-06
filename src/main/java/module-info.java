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

    opens com.example.mangatool to javafx.fxml;
    exports com.example.mangatool;
    exports com.example.mangatool.UI;
    opens com.example.mangatool.UI to javafx.fxml;
}