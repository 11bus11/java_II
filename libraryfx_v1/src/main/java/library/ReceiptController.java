
package library;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;

public class ReceiptController {

    @FXML
    private Button btnReturn;

    @FXML
    private TableColumn<?, ?> colBarcode;

    @FXML
    private TableColumn<?, ?> colDueDate;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    void goToHome(ActionEvent event) {

    }

    @FXML
    private Label lblLoanDate;

    @FXML
    private void initialize() {
        
    }

    private void loanReceipt(Loan loan) {
        lblLoanDate.setText("Loan date: " );

    }

    private void returnReceipt(ArrayList<Copy> copies) {
        lblLoanDate.setText("Return date: " );
    }

}
