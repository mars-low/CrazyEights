module CrazyEights {
    requires javafx.fxml;
    requires javafx.controls;
    opens CrazyEights to javafx.graphics;
    exports CrazyEights;
}
