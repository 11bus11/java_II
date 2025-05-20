
package library;

import java.io.IOException;
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
    @FXML private TableColumn<CopyForReceiptTable,String>  colBarcode, colDueDate, colTitle;
    @FXML private TableView<CopyForReceiptTable> tvWork;
    @FXML private Label lblLoanDate;

    @FXML
    void goToHome(ActionEvent event) throws IOException{
        App.setRoot("Home");
    }

    @FXML
    private void initialize() {
        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDueDate  .setCellValueFactory(new PropertyValueFactory<>("title"));

        tvWork.setItems(data);
        tvWork.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (Loan.forReturnReceipt.size() == 0) {
            loanReceipt();
        } else {
            returnReceipt();
        }
    }

    private final ObservableList<CopyForReceiptTable> data = FXCollections.observableArrayList();

    //receipt for loan
    private void loanReceipt() {
        data.clear();
        lblLoanDate.setText("Loan date: " + Loan.latestLoan.getLoanDate());
        List<Copy> copies = Loan.latestLoan.getCopies();

        //add all copies to the table
        int i = 0;
        while (copies.size() > i) {
            data.add(createCopyForTable(copies.get(i), copies.get(i).getWork()));
            i++;
        } 

        copies.clear();
        Loan.latestLoan = null;
        

    }

    //receipt for returns
    private void returnReceipt() {
        data.clear();
        lblLoanDate.setText("Return date: " + LocalDateTime.now());
        colDueDate.setText("");

        //add all copies to the table
        int i = 0;
        while (Loan.forReturnReceipt.size() > i) {
            Copy copy = Loan.forReturnReceipt.get(i);
            data.add(createCopyForTable(copy, copy.getWork()));
            i++;
        } 
        Loan.forReturnReceipt.clear();
    }

    //create an opject that can be added to the receipt-table
    private CopyForReceiptTable createCopyForTable(Copy c, Work w) {
        String dueDate = null;
        if (Loan.forReturnReceipt.size() == 0) {
            dueDate = LoanUtil.getDueDate(LocalDate.now(), w.getType()).toString();
        }
        CopyForReceiptTable cft = new CopyForReceiptTable(
                c.getBarcode(),
                w!=null ? w.getTitle() : "",
                dueDate,
                null
                );
        System.out.println(cft.getBarcode() + " barcode");

        return cft;
    }

}
