
package library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ReturnLoanController {
    /* ---------- INIT ---------- */
    @FXML
    private void initialize(){

        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor  .setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN    .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colWorkType.setCellValueFactory(new PropertyValueFactory<>("workType"));
        colStatus  .setCellValueFactory(new PropertyValueFactory<>("status"));

        tvWork.setItems(data);
        tvWork.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tfBarcode.setOnKeyPressed(this::enterSearch);
    }

    /* ---------- in-memory list of copies selected for this loan ---------- */
    private final ObservableList<CopyForTable> data          = FXCollections.observableArrayList();
    private final List<Copy>                   selectedCopies = new ArrayList<>();

    /* ---------- UI ---------- */
    @FXML private TextField tfBarcode;
    @FXML private Button    btnDelete;
    @FXML private TableView<CopyForTable> tvWork;
    @FXML private TableColumn<CopyForTable,String> colBarcode,colTitle,colAuthor,
                                                   colISBN,colWorkType,colStatus;


    @FXML
    private Button btnHome;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnMyLoans;

    @FXML
    private Button btnReturnLoan;

    @FXML
    void goToHome(MouseEvent event) throws IOException{
        App.setRoot("Home");
    }

    @FXML
    void goToLoan(MouseEvent event) throws IOException{
        App.setRoot("LoanController");
    }

    @FXML
    void goToReceiptController(MouseEvent event) throws IOException{
        App.setRoot("ReceiptController");
    }

    @FXML
    void handleInsert(MouseEvent event) {
        addByBarcode(); }
        
    private void enterSearch(KeyEvent e){ if(e.getCode()==KeyCode.ENTER) addByBarcode(); }

    private void addByBarcode(){

        String bc = tfBarcode.getText().trim();
        if(bc.isBlank()) return;

        /* find copy in global cache */
        Copy copy = Copy.arrayCopiesGlobal.stream()
                        .filter(c -> c.getBarcode().equals(bc))
                        .findFirst().orElse(null);

        if(copy == null){
            alert("Barcode not found.");
            return;
        }
        if(!Objects.equals(copy.getCopyStatus(),"loaned")){
            alert("Copy is not loaned out.");
            return;
        }
        if(selectedCopies.contains(copy)){
            alert("Copy already in the list.");
            return;
        }

        /* add to local lists */
        selectedCopies.add(copy);
        Work   w = copy.getWork();
        Author a = w!=null ? w.getAuthor() : null;

        data.add(new CopyForTable(
                copy.getBarcode(),
                w!=null ? w.getTitle() : "",
                a!=null ? a.getFirstName()+" "+a.getLastName() : "",
                w!=null ? w.getIsbn()  : "",
                w!=null ? w.getType()  : "",
                copy.getCopyStatus()));

        tfBarcode.clear();
    }
    


    @FXML
    void handleReturnLoan(MouseEvent event)  throws IOException{
        if(selectedCopies.isEmpty()){
            alert("No items in the list."); return;
        }

        /* update status in DB first */
        for(Copy c : selectedCopies){
            //Copy
            if(!CRUD.updateCopyStatus(c.getCopyID(),"available")){
                alert("Database update failed for "+c.getBarcode());
                return;
            }
            //LoanCopy
            if (!CRUD.updateIsReturned(c.getCopyID())) {
                alert("Database update failed for "+c.getBarcode());
                return;
            }
        }

        /* update status in memory */
        selectedCopies.forEach(c -> c.setCopyStatus("available"));

        /* update the list for receipt */
        selectedCopies.forEach(c -> Loan.forReturnReceipt.add(c));
        

        /* clear view + local lists */
        data.clear();
        selectedCopies.clear();
        
        App.setRoot("ReceiptController");
    }
    

    /* ---------- helper ---------- */
    private void alert(String m){
        new Alert(Alert.AlertType.ERROR,m,ButtonType.OK).showAndWait();
    }

    

}
