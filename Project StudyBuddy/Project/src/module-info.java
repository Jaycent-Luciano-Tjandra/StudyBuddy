module Project {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	requires javafx.media;

    opens application to javafx.fxml;
    exports application;
}