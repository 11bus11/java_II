package library;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * DelayedListController – lists all overdue loans.
 * <p>
 * Rules:
 * • "magazine" cannot be loaned (ignore).<br>
 * • Copies with <i>IsReference = true</i> are also ignored.<br>
 * • Due date by work type:<br>
 *   – other literature : 30 days<br>
 *   – course literature : 14 days<br>
 *   – movie            : 7  days<br>
 * • If <b>current date</b> &gt; <b>BorrowDate + Due</b> ⇒ overdue loan.
 */
public class DelayedListController implements Initializable {

    /* ---------- UI ---------- */
    @FXML private TableView<OverdueEntry>           tvDelayed;
    @FXML private TableColumn<OverdueEntry,String>  colUser;
    @FXML private TableColumn<OverdueEntry,String>  colCopy;
    @FXML private TableColumn<OverdueEntry,String>  colWorkType;
    @FXML private TableColumn<OverdueEntry,String>  colBorrowDate;
    @FXML private TableColumn<OverdueEntry,String>  colDueDate;
    @FXML private TableColumn<OverdueEntry,String>  colEmail;
    @FXML private TableColumn<OverdueEntry,String>  colDaysDelayed;
    @FXML public void goToStaff(MouseEvent event) {
        // implemente ou deixe vazio por enquanto
    }

    private final ObservableList<OverdueEntry> data = FXCollections.observableArrayList();

    /* ---------- INIT ---------- */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUser      .setCellValueFactory(new PropertyValueFactory<>("userName"));
        colCopy      .setCellValueFactory(new PropertyValueFactory<>("copyBarcode"));
        colWorkType  .setCellValueFactory(new PropertyValueFactory<>("workType"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate   .setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDaysDelayed.setCellValueFactory(new PropertyValueFactory<>("daysDelayed"));
        colEmail     .setCellValueFactory(new PropertyValueFactory<>("email"));

        // Automatic column resize
        tvDelayed.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadOverdues();
        tvDelayed.setItems(data);
    }

    /* ---------- CORE ---------- */
    private void loadOverdues() {
        data.clear();

        for (Loan loan : Loan.arrayLoansGlobal) {
            // LoanDate is Timestamp – convert to LocalDate
            LocalDate borrowDate = loan.getLoanDate()
                                        .toLocalDateTime()
                                        .toLocalDate();

            // LoanCopies of this loan that have not yet been returned
            List<LoanCopy> openCopies = LoanCopy.arrayLoanCopiesGlobal.stream()
                    .filter(lc -> lc.getLoanID() == loan.getLoanID() && !lc.isReturned())
                    .collect(Collectors.toList());

            for (LoanCopy lc : openCopies) {
                Copy copy = findCopyByID(lc.getCopyID());
                if (copy == null) continue;

                // Ignore loan restrictions
                if (copy.isReference()) continue;
                String workType = copy.getWork() != null ? copy.getWork().getType() : "";
                if ("magazine".equalsIgnoreCase(workType)) continue;

                int allowedDays = getAllowedDaysByWorkType(workType);
                LocalDate dueDate = borrowDate.plusDays(allowedDays);

                int daysDelayed = LoanUtil.getDaysOverdue(borrowDate, workType);
                if (daysDelayed > 0) {
                    // Its delayed
                }

                if (LocalDate.now().isAfter(dueDate)) {
                    User u = loan.getUser();
                    String userName = u != null ? u.getFirstName() + " " + u.getLastName()
                                                : String.valueOf(loan.getUser().getUserID());

                    data.add(new OverdueEntry(userName,
                                               copy.getBarcode(),
                                               workType,
                                               borrowDate.toString(),
                                               dueDate.toString(),
                                               u.getEmail(),
                                               String.valueOf(LocalDate.now().toEpochDay() - dueDate.toEpochDay())));
                }
            }
        }
    }

    /** Finds a copy in the global cache by ID. */
    private Copy findCopyByID(int copyID){
        for (Copy c : Copy.arrayCopiesGlobal)
            if (c.getCopyID() == copyID) return c;
        return null;
    }

    /** Number of allowed days by work type. */
    private int getAllowedDaysByWorkType(String workType) {
        switch (workType.toLowerCase()) {
            case "other literature":  return 30;
            case "course literature": return 14;
            case "movie":             return 7;
            default:                   return 30;
        }
    }

    /* ---------- DTO ---------- */
    public static class OverdueEntry {
        private final String userName;
        private final String copyBarcode;
        private final String workType;
        private final String borrowDate;
        private final String dueDate;
        private final String email;
        private final String daysDelayed;

        public OverdueEntry(String userName, String copyBarcode, String workType,
                             String borrowDate, String dueDate, String email, String daysDelayed) {
            this.userName    = userName;
            this.copyBarcode = copyBarcode;
            this.workType    = workType;
            this.borrowDate  = borrowDate;
            this.dueDate     = dueDate;
            this.email       = email;
            this.daysDelayed = daysDelayed;
        }

        public String getUserName()    { return userName; }
        public String getCopyBarcode() { return copyBarcode; }
        public String getWorkType()    { return workType; }
        public String getBorrowDate()  { return borrowDate; }
        public String getDueDate()     { return dueDate; }
        public String getEmail()       { return email; }
        public String getDaysDelayed() { return daysDelayed; }
    }

    // No controller associado ao DelayedListcontroller.fxml
    
}
