module com.nobble.codesnippets {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    exports com.nobble.codesnippets;
    opens com.nobble.codesnippets to javafx.fxml;
}