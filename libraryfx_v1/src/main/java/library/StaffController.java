
package library;

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

}

