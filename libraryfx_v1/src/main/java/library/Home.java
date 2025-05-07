package library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Home {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnMyLibraryAccount;

    @FXML
    private Button btnSearch;

    @FXML
    void goToHome(MouseEvent event) throws IOException {
        App.setRoot("Home");

    }

    @FXML
    void goToMyLibraryAccount(MouseEvent event) throws IOException {
        if (App.isLoggedIn != null) {
            App.setRoot("MyLibraryAccount");
        } else {
            App.setRoot("LoginController");
        }
    }

    @FXML
    void goToSearch(MouseEvent event) throws IOException {
        App.setRoot("SearchController");

    }

}
