
package library;

import java.time.LocalDate;
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
    @FXML private TableColumn<CopyForReceiptTable,String>  colBarcode, colDueDate, colTitle,
                                                    colType;
    @FXML private TableView<CopyForReceiptTable> tvWork;
    @FXML private Label lblLoanDate;

    @FXML
    void goToHome(ActionEvent event) {

    }

    @FXML
    private void initialize() {
        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDueDate  .setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tvWork.setItems(data);
        tvWork.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (Loan.forReturnReceipt.size() == 0) {
            loanReceipt();
        } else {
            returnReceipt();
        }
    }

    private final ObservableList<CopyForReceiptTable> data = FXCollections.observableArrayList();

    private void loanReceipt() {
        data.clear();
        lblLoanDate.setText("Loan date: " + Loan.latestLoan.getLoanDate());
        List<Copy> copies = Loan.latestLoan.getCopies();
        System.out.println(copies);

        int i = 0;
        while (copies.size() > i) {
            data.add(createCopyForTable(copies.get(i), copies.get(i).getWork()));
            i++;
        } 
        System.out.println(data + " data");
        copies.clear();
        Loan.latestLoan = null;
        

    }

    private void returnReceipt() {
        data.clear();
        lblLoanDate.setText("Return date: " + LocalDateTime.now());
        System.out.println(Loan.forReturnReceipt);

        int i = 0;
        while (Loan.forReturnReceipt.size() > i) {
            Copy copy = Loan.forReturnReceipt.get(i);
            data.add(createCopyForTable(copy, copy.getWork()));
            i++;
        } 
        System.out.println(data + " data");

        Loan.forReturnReceipt.clear();
    }

    private CopyForReceiptTable createCopyForTable(Copy c, Work w) {
        String dueDate = null;
        if (Loan.forReturnReceipt.size() == 0) {
            dueDate = LoanUtil.getDueDate(LocalDate.now(), w.getType()).toString();
        }
        System.out.println(w.getType());
        CopyForReceiptTable cft = new CopyForReceiptTable(
                c.getBarcode(),
                w!=null ? w.getTitle() : "",
                dueDate,
                w.getType()
                );
        System.out.println(cft.getBarcode() + " barcode");

        return cft;
    }

}
