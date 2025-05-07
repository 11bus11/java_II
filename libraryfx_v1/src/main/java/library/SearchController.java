
package library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SearchController {

    @FXML
    private Button btnHome;

    @FXML
    private TableColumn<?, ?> colBarcode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colYear;

    @FXML
    private TextField tfSearch;

    @FXML
  void goToHome(MouseEvent event) throws IOException {
        App.setRoot("Home");

    }

}
