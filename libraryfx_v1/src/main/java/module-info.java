module library {
    requires javafx.controls;
    requires javafx.fxml;

    opens library to javafx.fxml;
    exports library;
}
