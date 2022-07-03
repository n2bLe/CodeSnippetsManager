module com.nobble.codesnippets {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.datatransfer;
    requires java.desktop;
    exports com.nobble.codesnippets;
    opens com.nobble.codesnippets to javafx.fxml;
}