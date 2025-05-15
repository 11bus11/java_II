package library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MyLibraryAccount {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnLoan;

    @FXML
    private Button btnMyLoans;

    @FXML
    private Button btnReturnLoan;

    @FXML
    void goToHome(MouseEvent event) throws IOException {
        App.setRoot("Home");
    }

    @FXML
    void goToLoan(MouseEvent event) throws IOException {
        App.setRoot("LoanController");
    }

    @FXML
    void goToMyLoans(MouseEvent event) {

    }

    @FXML
    void goToReturnLoan(MouseEvent event) throws IOException {
        App.setRoot("ReturnLoanController");
    }

}