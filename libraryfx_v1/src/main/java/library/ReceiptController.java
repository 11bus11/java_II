
package library;

import java.time.LocalDateTime;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

public class ReceiptController {

    @FXML private Button btnReturn;
    @FXML private TableColumn<CopyForTable,String>  colBarcode, colDueDate, colTitle,
                                                    colType;
    @FXML private TableView<CopyForTable> tvWork;
    @FXML private Label lblLoanDate;

    @FXML
    void goToHome(ActionEvent event) {

    }

    @FXML
    private void initialize() {
        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("title"));
        colDueDate  .setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tvWork.setItems(data);

        if (Loan.forReturnReceipt == null) {
            loanReceipt();
        } else {
            returnReceipt();
        }
    }

    private final ObservableList<CopyForTable> data = FXCollections.observableArrayList();

    private void loanReceipt() {
        lblLoanDate.setText("Loan date: " );

    }

    private void returnReceipt() {
        lblLoanDate.setText("Return date: " + LocalDateTime.now());
        System.out.println(Loan.forReturnReceipt);

        int i = 0;
        while (Loan.forReturnReceipt.size() > i) {
            data.add(createCopyForTable(Loan.forReturnReceipt.get(i), Loan.forReturnReceipt.get(i).getWork()));
            i++;
        } 

        Loan.forReturnReceipt = null;
        
    }

    private CopyForTable createCopyForTable(Copy c, Work w) {
        CopyForTable cft = new CopyForTable(
                c.getBarcode(),
                w!=null ? w.getTitle() : "",
                null,
                w!=null ? w.getIsbn()  : "",
                w!=null ? w.getType()  : "",
                c.getCopyStatus());

        return cft;
    }

}
