package library;

import java.io.IOException;

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

    /* ---------- Table ---------- */
    @FXML private TableView<CopyView>           tvSearch;
    @FXML private TableColumn<CopyView,String>  colBarcode;
    @FXML private TableColumn<CopyView,String>  colTitle;
    @FXML private TableColumn<CopyView,String>  colISBN;
    @FXML private TableColumn<CopyView,String>  colStatus;
    @FXML private TableColumn<CopyView,String>  colDescription;
    @FXML private TableColumn<CopyView,String>  colType;

    /* ---------- UI ---------- */
    @FXML private TextField tfSearch;
    @FXML private Button    btnHome;


    //------INITIALIZE-------
    @FXML
    private void initialize() {
        colBarcode   .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle     .setCellValueFactory(new PropertyValueFactory<>("title"));
        colISBN      .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStatus    .setCellValueFactory(new PropertyValueFactory<>("status"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType      .setCellValueFactory(new PropertyValueFactory<>("type"));

        loadData("");  // Load all entries when the screen opens

        // Add listener to the search field: triggers whenever the user types
        tfSearch.addEventHandler(KeyEvent.KEY_RELEASED,
            e -> loadData(tfSearch.getText())  // Reload data based on current search term
        );
    }

    
     /**
     * Loads data from the global list of copies, filtering based on the search term.
     * Updates the table with matching results.
     * 
     * @param filter The search term entered by the user
     */
    private void loadData(String filter) {
        String term = filter.toLowerCase();
        ObservableList<CopyView> list = FXCollections.observableArrayList();

        for (Copy c : Copy.arrayCopiesGlobal) {
            Work w = c.getWork();
            if (w == null) continue;   
             
            // Check if any relevant field contains the search term
            boolean match =
                term.isBlank() ||
                c.getBarcode()            .toLowerCase().contains(term) ||
                w.getTitle()              .toLowerCase().contains(term) ||
                w.getIsbn()               .toLowerCase().contains(term) ||
                c.getCopyStatus()         .toLowerCase().contains(term) ||
                w.getDescription()        .toLowerCase().contains(term) ||
                w.getType()               .toLowerCase().contains(term);
            
                // If matched, add a new CopyView to the result list
            if (match) {
                list.add(new CopyView(
                    c.getBarcode(),
                    w.getTitle(),
                    w.getIsbn(),
                    c.getCopyStatus(),
                    w.getDescription(),
                    w.getType()
                ));
            }
        }
           // Display results in the table
        tvSearch.setItems(list);
    }

    //Back to home 
    @FXML
    private void goToHome(MouseEvent e) throws IOException {
        App.setRoot("Home");
    }
}