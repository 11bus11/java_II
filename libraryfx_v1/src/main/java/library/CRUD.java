package library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CRUD {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> colAuthor;

    @FXML
    private TableColumn<?, ?> colBarcode;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colPlacement;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colWorkType;

    @FXML
    private TextField tfAuthor;

    @FXML
    private TextField tfBarcode;

    @FXML
    private TextField tfISBN;

    @FXML
    private TextField tfPlacement;

    @FXML
    private TextField tfTitle;

    @FXML
    private Label tfType;

    @FXML
    private Label tfType1;

    @FXML
    private TextField tfWorkType;

    @FXML
    private TableView<?> tvWork;

    @FXML
    void handleDelete(MouseEvent event) {

    }

    @FXML
    void handleInsert(ActionEvent event) {

    }

    @FXML
    void handleUpdate(MouseEvent event) {

    }

}


