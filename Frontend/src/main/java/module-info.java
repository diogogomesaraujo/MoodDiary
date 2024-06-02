module org.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.net.http;
    requires jetty.util;
    requires java.json;

    opens org.example.frontend to javafx.fxml;
    exports org.example.frontend;
}
