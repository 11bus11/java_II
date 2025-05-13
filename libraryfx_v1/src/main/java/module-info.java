module library {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    requires com.zaxxer.hikari;

    opens library to javafx.fxml, java.sql, com.zaxxer.hikari, javafx.graphics;
   
    exports library;
}
