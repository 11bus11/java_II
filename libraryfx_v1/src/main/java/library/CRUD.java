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
    @FXML private TextField tfAuthor;
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
            if (sel != null) {
                tfBarcode .setText(sel.getBarcode());
                tfTitle   .setText(sel.getTitle());
                tfAuthor  .setText(sel.getAuthor());
                tfISBN    .setText(sel.getIsbn());
                cbWorkType.setValue(sel.getWorkType());
                tfPlacement.setText(sel.getPlacement());
                

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
    ps.executeUpdate();
  } catch (SQLException ex) {
    ex.printStackTrace();
  }

  // 4) refresh the table
  loadData();
}


    @FXML
    private void handleUpdate(MouseEvent ev) {
        // … your update logic …
    }

    @FXML
    private void handleDelete(MouseEvent ev) {
        // … your delete logic …
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

}
