
package library;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private final ObservableList<Copy> data =
      FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // bind columns
       
        colBarcode  .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle    .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor   .setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN     .setCellValueFactory(new PropertyValueFactory<>("isbn"));

        tvWork.setItems(data);
        tvWork.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
        if (sel == null) {
            return;}

        })
     }

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
        String inputBarcode   = tfBarcode.getText().trim();
        int index = 0;
        Copy currCopy = null;
        while (Copy.arrayCopiesGlobal.size() <= index) {
            if (inputBarcode.equals(Copy.getBarcode(Copy.arrayCopiesGlobal.get(index)))) {
                if (Copy.getIsReference(Copy.arrayCopiesGlobal.get(index))) {
                //showError("ISBN is required for course literature and other literature.");
                System.out.println("reference literature");
                } else {
                    currCopy = Copy.arrayCopiesGlobal.get(index);
                }
              
            }
            return;
        }
        if (currCopy != null) {
            
            cbIsReference.setSelected(rs.getBoolean("IsReference"));
            tfFirstName  .setText(rs.getString("FirstName"));
            tfLastName   .setText(rs.getString("LastName"));
        }

}
        //find the correct thing
    }

}

//@FXML private TableView<CopyEntry>           tvCRUD;
//   
//    @FXML private TableColumn<CopyEntry,String>  colBarcode;
//    @FXML private TableColumn<CopyEntry,String>  colTitle;
//    @FXML private TableColumn<CopyEntry,String>  colAuthor;
//    @FXML private TableColumn<CopyEntry,String>  colISBN;
//    @FXML private TableColumn<CopyEntry,String>  colWorkType;
//    @FXML private TableColumn<CopyEntry,String>  colPlacement;