package library;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoanController {

    @FXML private TextField tfBarcode;
    @FXML private TableView<CopyForTable> tvWork;
    @FXML private TableColumn<CopyForTable,String> colBarcode,colTitle,colAuthor,
                                                   colISBN,colWorkType,colStatus;

    private final ObservableList<CopyForTable> data = FXCollections.observableArrayList();

    /* ---------- INIT ---------- */
    @FXML
    private void initialize(){
        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor  .setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN    .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colWorkType.setCellValueFactory(new PropertyValueFactory<>("workType"));
        colStatus  .setCellValueFactory(new PropertyValueFactory<>("status"));

        refresh(Copy.arrayCopiesGlobal);
        tfBarcode.setOnKeyPressed(this::enterSearch);
    }

    /* ---------- SEARCH ---------- */
    @FXML private void handleSearch(ActionEvent e){ doSearch(); }
    private void enterSearch(KeyEvent e){ if(e.getCode()==KeyCode.ENTER) doSearch(); }

    private void doSearch(){
        String f = tfBarcode.getText().trim();
        List<Copy> src = f.isBlank()
                         ? Copy.arrayCopiesGlobal
                         : Copy.arrayCopiesGlobal.stream()
                               .filter(c -> c.getBarcode().startsWith(f))
                               .collect(Collectors.toList());
        refresh(src);
    }

    /* ---------- LOAN / RETURN ---------- */
    @FXML
    private void handleLoan(MouseEvent e){

        CopyForTable sel = tvWork.getSelectionModel().getSelectedItem();
        if(sel == null){ alert("Selecione uma linha."); return; }

        Copy copy = Copy.arrayCopiesGlobal.stream()
                         .filter(c -> c.getBarcode().equals(sel.getBarcode()))
                         .findFirst().orElse(null);
        if(copy == null){ alert("Cópia não encontrada."); return; }

        String newStatus = copy.getCopyStatus().equals("available") ? "loaned" : "available";

        if(!CRUD.updateCopyStatus(copy.getCopyID(), newStatus)){
            alert("Falha ao atualizar status no banco.");
            return;
        }
        copy.setCopyStatus(newStatus);

        if("loaned".equals(newStatus))
            LoanUtil.registerLoan(List.of(copy), App.isLoggedIn);

        refresh(Copy.arrayCopiesGlobal);
    }

    /* ---------- NAVIGATION (empty example) ---------- */
    @FXML public void goToHome      (MouseEvent e){}
    @FXML public void goToReturnLoan(MouseEvent e){}
    @FXML public void goToMyLoans   (MouseEvent e){}

    /* ---------- helpers ---------- */
    private void refresh(List<Copy> src){
        data.clear();
        for(Copy c : src){
            Work   w = c.getWork();
            Author a = w!=null ? w.getAuthor() : null;

            data.add(new CopyForTable(
                    c.getBarcode(),
                    w!=null ? w.getTitle() : "",
                    a!=null ? a.getFirstName()+" "+a.getLastName() : "",
                    w!=null ? w.getIsbn()  : "",
                    w!=null ? w.getType()  : "",
                    c.getCopyStatus()));
        }
        tvWork.setItems(data);
    }
    private void alert(String m){ new Alert(Alert.AlertType.ERROR,m,ButtonType.OK).showAndWait(); }
}
