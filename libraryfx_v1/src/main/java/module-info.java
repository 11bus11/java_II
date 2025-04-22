module library {
    requires javafx.controls; //drar in graphics för modulen
    requires javafx.fxml;

    requires transitive javafx.graphics; //exporterar modul vidare

    opens library to javafx.fxml;
    exports library;
}
