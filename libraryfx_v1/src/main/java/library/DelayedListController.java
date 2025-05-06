package library;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class DelayedListController {

    @FXML
    private Button btnReturn;

    @FXML
    private TableColumn<?, ?> colBarcode;

    @FXML
    private TableColumn<?, ?> colDaysDelayed;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colYear;

    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<?> tvDelayedList;

    @FXML
    void goToStaff(MouseEvent event) {

    }

}

