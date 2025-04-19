module library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;

    opens library to javafx.fxml, java.sql, com.zaxxer.hikari;
   
    exports library;
}
