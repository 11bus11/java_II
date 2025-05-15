package library;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

/**
 * Flow:
 * 1. TableView starts empty
 * 2. User scans/enters barcode + Search → item is appended
 * 3. Delete removes selected rows from the list only
 * 4. Loan creates ONE Loan for all rows currently displayed
 */
public class LoanController {

    /* ---------- UI ---------- */
    @FXML private TextField tfBarcode;
    @FXML private Button    btnDelete;
    @FXML private TableView<CopyForTable> tvWork;
    @FXML private TableColumn<CopyForTable,String> colBarcode,colTitle,colAuthor,
                                                   colISBN,colWorkType,colStatus;

    /* ---------- in-memory list of copies selected for this loan ---------- */
    private final ObservableList<CopyForTable> data          = FXCollections.observableArrayList();
    private final List<Copy>                   selectedCopies = new ArrayList<>();

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

    /* ---------- SEARCH / ADD ---------- */
    @FXML private void handleSearch(ActionEvent e){ addByBarcode(); }
    private void enterSearch(KeyEvent e){ if(e.getCode()==KeyCode.ENTER) addByBarcode(); }

    public void addByBarcode(){

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
        if(!Objects.equals(copy.getCopyStatus(),"available")){
            alert("Copy is not available.");
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

    /* ---------- DELETE from list (no DB touch) ---------- */
    @FXML public void handleDelete(MouseEvent e){

        Set<String> barcodesToRemove = tvWork.getSelectionModel().getSelectedItems().stream()
                                             .map(CopyForTable::getBarcode)
                                             .collect(Collectors.toSet());
        if(barcodesToRemove.isEmpty()) return;

        data.removeIf(row -> barcodesToRemove.contains(row.getBarcode()));
        selectedCopies.removeIf(c -> barcodesToRemove.contains(c.getBarcode()));
    }

    /* ---------- LOAN ---------- */
    @FXML public void handleLoan(MouseEvent e){

        if (App.isLoggedIn == null) {
            alert("Is logged is false.");
            return;
        }

        if(selectedCopies.isEmpty()){
            alert("No items in the list."); return;
        }

        /* update status in DB first */
        for(Copy c : selectedCopies){
            if(!CRUD.updateCopyStatus(c.getCopyID(),"loaned")){
                alert("Database update failed for "+c.getBarcode());
                return;
            }
        }

        /* update status in memory */
        selectedCopies.forEach(c -> c.setCopyStatus("loaned"));

        /* create ONE Loan */
        LoanUtil.registerLoan(selectedCopies, App.isLoggedIn);

        /* clear view + local lists */
        data.clear();
        selectedCopies.clear();
    }

    /* ---------- NAVIGATION STUBS ---------- */
    @FXML public void goToHome      (MouseEvent e){}
    @FXML public void goToReturnLoan(MouseEvent e)  throws IOException {
        App.setRoot("ReturnLoanController");
    }
    @FXML public void goToMyLoans   (MouseEvent e){}

    /* ---------- helper ---------- */
    private void alert(String m){
        new Alert(Alert.AlertType.ERROR,m,ButtonType.OK).showAndWait();
    }

    private int getLoanDaysByUserType(String userType) {
        if (userType == null) return 4;
        switch (userType.toLowerCase()) {
            case "student":
                return 4;
            case "faculty":
                return 6;
            case "researcher":
                return 7;
            case "public":
                return 8;
            default:
                return 4;
        }
    }

    private LocalDate calculateDueDate(User user) {
        int days = getLoanDaysByUserType(user.getUserType());
        return LocalDate.now().plusDays(days); // Usa local data
    }
}
