package library;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

public class CRUD {                      // <- Must match your filename CRUD.java

    

    // TableView + columns
    @FXML private TableView<CopyEntry>           tvCRUD;
   
    @FXML private TableColumn<CopyEntry,String>  colBarcode;
    @FXML private TableColumn<CopyEntry,String>  colTitle;
    @FXML private TableColumn<CopyEntry,String>  colAuthor;
    @FXML private TableColumn<CopyEntry,String>  colISBN;
    @FXML private TableColumn<CopyEntry,String>  colWorkType;
    @FXML private TableColumn<CopyEntry,String>  colPlacement;

    // Form fields
    @FXML private TextField tfBarcode;
    @FXML private TextField tfTitle;
    @FXML private TextField tfISBN;
    @FXML private ComboBox<String> cbWorkType;
    @FXML private TextField tfPlacement;
    
    @FXML private TextField  tfFirstName;
    @FXML private TextField  tfLastName;
    @FXML private TextField  tfYear;
    @FXML private TextField  tfDescription;
    @FXML private CheckBox   cbIsReference;


    // Buttons
    @FXML private Button btnInsert;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnReturn;

    private final ObservableList<CopyEntry> data =
      FXCollections.observableArrayList();


      // Store currently selected IDs so update can reference them
private int currentWorkId = -1;
private int currentAuthorId = -1;


    @FXML
    private void initialize() {
        // bind columns
       
        colBarcode  .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle    .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor   .setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN     .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colWorkType .setCellValueFactory(new PropertyValueFactory<>("workType"));
        colPlacement.setCellValueFactory(new PropertyValueFactory<>("placement"));

        tvCRUD.setItems(data);
        tvCRUD.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
    if (sel == null) return;

    // 1) the fields you already do:
    tfBarcode .setText(sel.getBarcode());
    tfTitle   .setText(sel.getTitle());
    tfISBN    .setText(sel.getIsbn());
    cbWorkType.setValue(sel.getWorkType());
    tfPlacement.setText(sel.getPlacement());

    // 2) now grab the rest
    String sql =
      "SELECT w.Year, w.WorkDesc, c.IsReference, " +
      "       a.FirstName, a.LastName, w.WorkID, a.AuthorID " +
      "FROM Copy c " +
      " JOIN Work w       ON c.WorkID   = w.WorkID " +
      " JOIN WorkAuthor wa ON w.WorkID   = wa.WorkID " +
      " JOIN Author a     ON wa.AuthorID = a.AuthorID " +
      "WHERE c.CopyBarcode = ?";
    try (var conn = DbUtil.getConnection();
         var ps   = conn.prepareStatement(sql)) {
      ps.setString(1, sel.getBarcode());
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          tfYear       .setText(rs.getString("Year"));
          tfDescription.setText(rs.getString("WorkDesc"));
          cbIsReference.setSelected(rs.getBoolean("IsReference"));
          tfFirstName  .setText(rs.getString("FirstName"));
          tfLastName   .setText(rs.getString("LastName"));

          // stash these IDs in hidden fields so update can find them:
          currentWorkId   = rs.getInt("WorkID");
          currentAuthorId = rs.getInt("AuthorID");
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      showError("Could not load details: " + ex.getMessage());
    }
});

        cbWorkType.setItems(FXCollections.observableArrayList(
    "movie",
    "magazine",
    "course literature",
    "other literature"
));
cbWorkType.setPromptText("Select Type");

        loadData();
    }

    private void loadData() {
        data.clear();
        String sql =
  "SELECT c.CopyBarcode, w.WorkTitle, " +
  "       CONCAT(a.FirstName,' ',a.LastName) AS author, " +
  "       w.ISBN, w.WorkType, c.CopyPlacement " +
  "FROM Copy c " +
  "JOIN Work w       ON c.WorkID   = w.WorkID " +
  "JOIN WorkAuthor wa ON w.WorkID   = wa.WorkID " +
  "JOIN Author a     ON wa.AuthorID = a.AuthorID";

try (var conn = DbUtil.getConnection();
     var ps   = conn.prepareStatement(sql);
     var rs   = ps.executeQuery())
{
    while (rs.next()) {
        data.add(new CopyEntry(
            rs.getString("CopyBarcode"),
            rs.getString("WorkTitle"),
            rs.getString("author"),
            rs.getString("ISBN"),
            rs.getString("WorkType"),
            rs.getString("CopyPlacement")
        ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
private void handleInsert(ActionEvent ev) {
  // 1) pull every field
  String firstName = tfFirstName.getText().trim();
  String lastName  = tfLastName.getText().trim();
  String barcode   = tfBarcode.getText().trim();
  String title     = tfTitle.getText().trim();
   String yearText  = tfYear      .getText().trim();
  String isbn      = tfISBN.getText().trim();
  String type      = cbWorkType.getValue();
  String placement = tfPlacement.getText().trim();
  String desc      = tfDescription.getText().trim();
  boolean isRef    = cbIsReference.isSelected();
  
  // 2) parse & validate Year
  int year;
  try {
    year = Integer.parseInt(yearText);
  } catch (NumberFormatException ex) {
    showError("Year must be a number.");
    return;
  }
    

  // --- after you read String isbn and String type ---
if (("course literature".equals(type) || "other literature".equals(type))
    && isbn.isEmpty()) {
    showError("ISBN is required for course literature and other literature.");
    return;
}



   // 3) upsert the Work & Author (new signature!)
  int workId = findOrCreateWork(
     title, firstName, lastName,
     isbn, type, desc, year
  );
  if (workId < 0) return;   // user fixed whatever error you showed



  // 4) insert the Copy row (with isReference and placement)
  String sql =
    "INSERT INTO Copy " +
    "  (CopyBarcode, WorkID, IsReference, CopyStatus, CopyPlacement) " +
    "VALUES (?, ?, ?, 'available', ?)";
  try (Connection conn = DbUtil.getConnection();
       PreparedStatement ps = conn.prepareStatement(sql))
  {
  ps.setString(1, barcode);
    ps.setInt   (2, workId);
    ps.setBoolean(3, isRef);
    ps.setString(4, placement);
    
    // 2) attempt insert, catching duplicate‐barcode
    try {
      ps.executeUpdate();
    } catch (SQLException ex) {
      if (ex.getErrorCode() == 1062) {            // MySQL duplicate-key
        showError("That barcode already exists.");
        return;
      } else {
        throw ex;  // re-throw anything else
      }
    }

} catch (SQLException ex) {
    ex.printStackTrace();
    showError("Error inserting copy: " + ex.getMessage());
    return;
}

  // 4) refresh the table
  loadData();
}


   @FXML
private void handleUpdate(MouseEvent ev) {
    
    CopyEntry sel = tvCRUD.getSelectionModel().getSelectedItem();
    if (sel == null) {
        showError("Please select a copy to update.");
        return;
    }

    
    String oldBarcode   = sel.getBarcode();                 
    String newBarcode   = tfBarcode.getText().trim();
    boolean isRef       = cbIsReference.isSelected();
    String placement    = tfPlacement.getText().trim();

    String newTitle     = tfTitle.getText().trim();
    String newISBN      = tfISBN.getText().trim();
    String newType      = cbWorkType.getValue();
    String newDesc      = tfDescription.getText().trim();
    String yearText     = tfYear.getText().trim();
    String newFirstName = tfFirstName.getText().trim();
    String newLastName  = tfLastName.getText().trim();

    int newYear;
    try {
        newYear = Integer.parseInt(yearText);
    } catch (NumberFormatException ex) {
        showError("Year must be a number.");
        return;
    }

    int workId   = currentWorkId;
    int authorId = currentAuthorId;

    
    try (Connection conn = DbUtil.getConnection()) {
        conn.setAutoCommit(false);

        
        String sqlAuthor = 
            "UPDATE Author SET FirstName=?, LastName=? WHERE AuthorID=?";
        try (PreparedStatement ps = conn.prepareStatement(sqlAuthor)) {
            ps.setString(1, newFirstName);
            ps.setString(2, newLastName);
            ps.setInt   (3, authorId);
            ps.executeUpdate();
        }

        
        String sqlWork =
            "UPDATE Work SET WorkTitle=?, ISBN=?, WorkDesc=?, WorkType=?, Year=? " +
            "WHERE WorkID=?";
        try (PreparedStatement ps = conn.prepareStatement(sqlWork)) {
            ps.setString(1, newTitle);
            ps.setString(2, newISBN);
            ps.setString(3, newDesc);
            ps.setString(4, newType);
            ps.setInt   (5, newYear);
            ps.setInt   (6, workId);
            ps.executeUpdate();
        }

        
        String sqlCopy =
            "UPDATE Copy SET CopyBarcode=?, IsReference=?, CopyPlacement=? " +
            "WHERE CopyBarcode=?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCopy)) {
            ps.setString (1, newBarcode);
            ps.setBoolean(2, isRef);
            ps.setString (3, placement);
            ps.setString (4, oldBarcode);
            ps.executeUpdate();
        }

        conn.commit();  

    } catch (SQLException ex) {
        ex.printStackTrace();
        showError("Error updating record: " + ex.getMessage());
    }

    
    loadData();
    clearForm();
}

   @FXML
private void handleDelete(MouseEvent ev) {
    CopyEntry sel = tvCRUD.getSelectionModel().getSelectedItem();
    if (sel == null) {
        showError("Please select a copy to delete.");
        return;
    }
    // optional confirmation
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
        "Are you sure you want to delete copy “" + sel.getBarcode() + "”?",
        ButtonType.YES, ButtonType.NO);
    confirm.setHeaderText(null);
    confirm.showAndWait();
    if (confirm.getResult() != ButtonType.YES) return;

    String sql = "DELETE FROM Copy WHERE CopyBarcode = ?";
    try (Connection conn = DbUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sel.getBarcode());
        ps.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
        showError("Error deleting copy: " + ex.getMessage());
        return;
    }

    loadData();
    clearForm();
}


    @FXML
    private void handleReturn(MouseEvent ev) throws IOException {
        App.setRoot("StaffController");
    }

   

   private int findOrCreateWork(String title,
                             String firstName,
                             String lastName,
                             String isbn,
                             String type,
                             String description, 
                             int year)
{
  try (Connection conn = DbUtil.getConnection()) {
    // 1) try select existing
    String lookup =
      "SELECT w.WorkID " +
      "FROM Work w " +
      "JOIN WorkAuthor wa ON w.WorkID=wa.WorkID " +
      "JOIN Author a     ON wa.AuthorID=a.AuthorID " +
      "WHERE w.WorkTitle=? AND w.ISBN=? " +
      "  AND a.FirstName=? AND a.LastName=?";
    try (PreparedStatement ps = conn.prepareStatement(lookup)) {
      ps.setString(1, title);
      ps.setString(2, isbn);
      ps.setString(3, firstName);
      ps.setString(4, lastName);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("WorkID");
      }
    }

    // 2) insert Work (including WorkDesc + Year)
    String insW =
      "INSERT INTO Work (WorkTitle, ISBN, WorkDesc, WorkType, Year) " +
      "VALUES (?, ?, ?, ?, ?)";
    int workId;
    try (PreparedStatement ps = conn.prepareStatement(
             insW, Statement.RETURN_GENERATED_KEYS))
    {
      ps.setString (1, title);
      ps.setString (2, isbn);
      ps.setString (3, description);
      ps.setString (4, type);
      ps.setInt    (5, year);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        keys.next();
        workId = keys.getInt(1);
      }
    } catch (SQLException e) {
      showError("Invalid work-type or data overflow.\n"
              + "Please check your Type, Description length, and Year.");
      return -1;
    }

    // 3) author upsert
    int authorId;
    String findA = "SELECT AuthorID FROM Author WHERE FirstName=? AND LastName=?";
    try (PreparedStatement ps = conn.prepareStatement(findA)) {
      ps.setString(1, firstName);
      ps.setString(2, lastName);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          authorId = rs.getInt("AuthorID");
        } else {
          try (PreparedStatement insA = conn.prepareStatement(
                   "INSERT INTO Author (FirstName, LastName) VALUES (?, ?)",
                   Statement.RETURN_GENERATED_KEYS))
          {
            insA.setString(1, firstName);
            insA.setString(2, lastName);
            insA.executeUpdate();
            try (ResultSet k2 = insA.getGeneratedKeys()) {
              k2.next();
              authorId = k2.getInt(1);
            }
          }
        }
      }
    }

    // 4) link WorkAuthor
    try (PreparedStatement link = conn.prepareStatement(
           "INSERT INTO WorkAuthor (WorkID, AuthorID) VALUES (?, ?)"))
    {
      link.setInt(1, workId);
      link.setInt(2, authorId);
      link.executeUpdate();
    }

    return workId;

  } catch (SQLException ex) {
    throw new RuntimeException(ex);
  }
}

/** display a simple error dialog */
private void showError(String message) {
  Alert alert = new Alert(Alert.AlertType.ERROR);
  alert.setHeaderText(null);
  alert.setContentText(message);
  alert.showAndWait();
}

/** clear every field back to its “empty” state */
private void clearForm() {
    tfBarcode.clear();
    tfTitle.clear();
    tfISBN.clear();
    tfPlacement.clear();
    tfYear.clear();
    cbWorkType.setValue(null);
    tfFirstName.clear();
    tfLastName.clear();
    tfDescription.clear();
    cbIsReference.setSelected(false);
    tfFirstName.clear();
    tfLastName.clear();
}

}
