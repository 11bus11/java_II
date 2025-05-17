
package library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class StaffController {

    @FXML
    private Button btnCRUD;

    @FXML
    private Button btnDelayedLoans;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnMyLoans;

    @FXML
    private Button btnLogout;

    @FXML
    void goToCRUD(MouseEvent event) {
        try {
            App.setRoot("CRUD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToDelayedLoans(MouseEvent event) {
        try {
            App.setRoot("DelayedListController");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToHome(MouseEvent event) {
        try {
            App.setRoot("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToMyLoans(MouseEvent event) {
        try {
            App.setRoot("MyLibraryAccount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToHomeLogout(MouseEvent event) throws IOException {
        App.isLoggedIn = null;
        App.setRoot("Home");
    }

}

