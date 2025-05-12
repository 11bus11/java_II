
package library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoanController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnLoan;

    @FXML
    private Button btnMyLoans;

    @FXML
    private Button btnReturnLoan;

    @FXML
    private TableColumn<?, ?> colAuthor;

    @FXML
    private TableColumn<?, ?> colBarcode;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colWorkType;

    @FXML
    private TextField tfBarcode;

    @FXML
    private TableView<?> tvWork;

    @FXML
    void goToHome(MouseEvent event)throws IOException {
        App.setRoot("Home");
    }

    @FXML
    void goToReturnLoan(MouseEvent event) {

    }

    @FXML
    void goToMyLoans(MouseEvent event)  throws IOException {
        App.setRoot("MyLoansController");
    }

    @FXML
    void handleDelete(MouseEvent event) {

    }

    @FXML
    void handleInsert(MouseEvent event) {
        addTableElement();
    }

    @FXML
    void handleLoan(MouseEvent event) {

    }

    public void addTableElement(String barcode) {
        //find the correct thing
    }

}
