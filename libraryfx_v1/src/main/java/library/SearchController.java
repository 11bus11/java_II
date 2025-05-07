package library;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SearchController {

    // TableView och kolumner
    @FXML private TableView<CopyView>           tvSearch;
    @FXML private TableColumn<CopyView,String>  colBarcode;
    @FXML private TableColumn<CopyView,String>  colTitle;
    @FXML private TableColumn<CopyView,Integer> colYear;
    @FXML private TableColumn<CopyView,String>  colISBN;
    @FXML private TableColumn<CopyView,String>  colStatus;
    @FXML private TableColumn<CopyView,String>  colDescription;
    @FXML private TableColumn<CopyView,String>  colType;

    // Sökfält och Home-knapp
    @FXML private TextField tfSearch;
    @FXML private Button    btnHome;

    // JDBC-konfiguration
    private static final String URL  =
        "jdbc:mysql://mysql-eebfafa-library1.j.aivencloud.com:27035/defaultdb?sslMode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASS = Secret.Password();

    
    @FXML
    private void initialize() {
        // A) Binda kolumner mot CopyView-egenskaper
        colBarcode   .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle     .setCellValueFactory(new PropertyValueFactory<>("title"));
        colYear      .setCellValueFactory(new PropertyValueFactory<>("year"));
        colISBN      .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStatus    .setCellValueFactory(new PropertyValueFactory<>("status"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType      .setCellValueFactory(new PropertyValueFactory<>("type"));

        // B) Ladda initialt alla rader utan filter
        loadData("");

        // C) Lyssna på tangenttryck i sökrutan för live-filter???????????
        tfSearch.addEventHandler(KeyEvent.KEY_RELEASED, e ->
            loadData(tfSearch.getText())
        );
    }

    /**
     * Hämtar rader från databasen och fyller tableview tvSearch.
     */
    private void loadData(String filter) {
        ObservableList<CopyView> list = FXCollections.observableArrayList();
        String sql =
        "SELECT c.CopyBarcode   AS barcode, " +
        "       w.WorkTitle     AS title, " +
        //"       w.year          AS year, " +
        "       w.ISBN          AS isbn, " +
        "       c.CopyStatus    AS status, " +
        "       w.WorkDesc      AS description, " +
        "       w.WorkType      AS type " +
        "FROM Copy c " +
        "JOIN Work w ON c.WorkID = w.WorkID " +
        "WHERE c.CopyBarcode LIKE ? " +
        "   OR w.WorkTitle   LIKE ? " +
        "   OR w.ISBN        LIKE ? " +
        "   OR c.CopyStatus  LIKE ? " +
        "   OR w.WorkDesc    LIKE ? " +
        "   OR w.WorkType    LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filter + "%";
            for (int i = 1; i <= 6; i++) {
                ps.setString(i, wildcard);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CopyView(
                    rs.getString("barcode"),
                    rs.getString("title"),
                    //rs.getInt   ("year"),
                    rs.getString("isbn"),
                    rs.getString("status"),
                    rs.getString("description"),
                    rs.getString("type")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tvSearch.setItems(list);
    }

    
    @FXML
    private void goToHome(MouseEvent event) throws IOException {
        App.setRoot("Home");
    }
}
