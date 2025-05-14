package library;

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

/**
 * CRUD – handles database operations, maintains memory caches,
 * and validates ISBN for “course literature” and “other literature”.
 */
public class CRUD {

    //Table
    @FXML private TableView<CopyEntry> tvCRUD;
    @FXML private TableColumn<CopyEntry,String> colBarcode,colTitle,colAuthor,
                                               colISBN,colWorkType,colPlacement;

    //Textfield 
    @FXML private TextField tfBarcode,tfTitle,tfISBN,tfPlacement,
                            tfFirstName,tfLastName,tfYear,tfDescription;
    @FXML private ComboBox<String> cbWorkType;
    @FXML private CheckBox cbIsReference;

    @FXML private Button btnInsert,btnUpdate,btnDelete,btnReturn;

    private final ObservableList<CopyEntry> data = FXCollections.observableArrayList();


    //INITIALIZATION 
    @FXML
    private void initialize() {
        colBarcode .setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colTitle   .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor  .setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN    .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colWorkType.setCellValueFactory(new PropertyValueFactory<>("workType"));
        colPlacement.setCellValueFactory(new PropertyValueFactory<>("placement"));

        //add options to Combobox
        cbWorkType.setItems(FXCollections.observableArrayList(
            "movie","magazine","course literature","other literature"));
        cbWorkType.setPromptText("Select Type");

        //Set table data
        tvCRUD.setItems(data);
        loadData();

        //Fill the form when a row is selected
        tvCRUD.getSelectionModel().selectedItemProperty().addListener((o,oldSel,sel)->{
            if(sel==null)return;
            Copy c = Copy.arrayCopiesGlobal.stream()
                         .filter(cp->cp.getBarcode().equals(sel.getBarcode()))
                         .findFirst().orElse(null);
            if(c==null)return;

            Work w=c.getWork(); Author a=(w!=null)?w.getAuthor():null;

            tfBarcode.setText(c.getBarcode());
            tfPlacement.setText(c.getCopyPlacement());
            cbIsReference.setSelected(c.isReference());

            if(w!=null){
                tfTitle.setText(w.getTitle());
                tfISBN .setText(w.getIsbn());
                cbWorkType.setValue(w.getType());
                tfDescription.setText(w.getDescription());
                tfYear.setText(String.valueOf(w.getYear()));
            }else{
                tfTitle.clear();tfISBN.clear();cbWorkType.setValue(null);
                tfDescription.clear();tfYear.clear();
            }
            if(a!=null){
                tfFirstName.setText(a.getFirstName());
                tfLastName .setText(a.getLastName());
            }else{
                tfFirstName.clear();tfLastName.clear();
            }
        });
    }

    
    //Refresh the table
    private void loadData(){
        data.clear();
        for(Copy c:Copy.arrayCopiesGlobal){
            Work w=c.getWork();
            String author = (w!=null && w.getAuthor()!=null)
                ? w.getAuthor().getFirstName()+" "+w.getAuthor().getLastName()
                : "";
            data.add(new CopyEntry(
                c.getBarcode(),
                w!=null?w.getTitle():"",
                author,
                w!=null?w.getIsbn():"",
                w!=null?w.getType():"",
                c.getCopyPlacement()));
        }
    }

    //-------INSERT--------
    @FXML
    private void handleInsert(ActionEvent ev){
        String bc  = tfBarcode.getText().trim();
        String ttl = tfTitle  .getText().trim();
        if(bc.isBlank()||ttl.isBlank()){err("Barcode & Title required");return;}
        if(Copy.arrayCopiesGlobal.stream().anyMatch(c->c.getBarcode().equals(bc))){
            err("Barcode already exists");return;}

        int yr=parseYear(); if(yr<0)return;
        String type = cbWorkType.getValue();
        if(type==null){err("Select Work Type");return;}
        String isbn=tfISBN.getText().trim();
        if( requiresISBN(type) && isbn.isEmpty()){
            err("ISBN is required for "+type+".");return;}

        String fn=tfFirstName.getText().trim(), ln=tfLastName.getText().trim();
        String desc=tfDescription.getText().trim();
        boolean isRef=cbIsReference.isSelected();
        String place=tfPlacement.getText().trim();

        try(Connection conn=DbUtil.getConnection()){
            conn.setAutoCommit(false);

            
            int authorId;
            try(PreparedStatement ps=conn.prepareStatement(
                "SELECT AuthorID FROM Author WHERE FirstName=? AND LastName=?")){
                ps.setString(1,fn); ps.setString(2,ln);
                ResultSet rs=ps.executeQuery();
                if(rs.next()) authorId=rs.getInt(1);
                else{
                    try(PreparedStatement ins=conn.prepareStatement(
                        "INSERT INTO Author (FirstName,LastName) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS)){
                        ins.setString(1,fn);ins.setString(2,ln);ins.executeUpdate();
                        ResultSet k=ins.getGeneratedKeys();k.next();
                        authorId=k.getInt(1);
                    }
                }
            }

            // work insert
            int workId;
            try(PreparedStatement ins=conn.prepareStatement(
                "INSERT INTO Work (WorkTitle,ISBN,WorkType,WorkDesc,Year) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)){
                ins.setString(1,ttl);ins.setString(2,isbn);ins.setString(3,type);
                ins.setString(4,desc);ins.setInt(5,yr);
                ins.executeUpdate();
                ResultSet k=ins.getGeneratedKeys();k.next();
                workId=k.getInt(1);
            }
            
            
            try(PreparedStatement link=conn.prepareStatement(
                "INSERT INTO WorkAuthor (WorkID,AuthorID) VALUES (?,?)")){
                link.setInt(1,workId);link.setInt(2,authorId);link.executeUpdate();
            }
            
            try(PreparedStatement ins=conn.prepareStatement(
                "INSERT INTO Copy (CopyBarcode,WorkID,IsReference,CopyStatus,CopyPlacement) "
              +"VALUES (?,?,?,?,?)")){
                ins.setString(1,bc);ins.setInt(2,workId);ins.setBoolean(3,isRef);
                ins.setString(4,"available");ins.setString(5,place);
                ins.executeUpdate();
            }
            conn.commit();

            
            Author aObj=new Author(authorId,fn,ln);
            Work   wObj=new Work(workId,ttl,isbn,type,desc,aObj,yr);
            Copy   cObj=new Copy(-1,bc,wObj,isRef,"available",place);
            Author.arrayAuthorsGlobal.add(aObj);
            Work  .arrayWorksGlobal  .add(wObj);
            Copy  .arrayCopiesGlobal .add(cObj);
            loadData(); clear();
        }catch(SQLException e){e.printStackTrace();err(e.getMessage());}
    }

    /* ---------- UPDATE ---------- */
    @FXML
    private void handleUpdate(MouseEvent ev){
        CopyEntry sel=tvCRUD.getSelectionModel().getSelectedItem();
        if(sel==null){err("Select a copy to update");return;}

        Copy target=Copy.arrayCopiesGlobal.stream()
            .filter(c->c.getBarcode().equals(sel.getBarcode()))
            .findFirst().orElse(null);
        if(target==null){err("Copy not found");return;}

        String newBC  = tfBarcode.getText().trim();
        String newTTL = tfTitle  .getText().trim();
        if(newBC.isBlank()||newTTL.isBlank()){err("Barcode & Title required");return;}
        if(!newBC.equals(sel.getBarcode()) &&
           Copy.arrayCopiesGlobal.stream().anyMatch(c->c.getBarcode().equals(newBC))){
            err("Barcode already exists");return;}

        int newYr=parseYear(); if(newYr<0)return;
        String newType=cbWorkType.getValue();
        if(newType==null){err("Select Work Type");return;}
        String newISBN=tfISBN.getText().trim();
        if( requiresISBN(newType) && newISBN.isEmpty()){
            err("ISBN is required for "+newType+".");return;}

        String newFN=tfFirstName.getText().trim(), newLN=tfLastName.getText().trim();
        String newDesc=tfDescription.getText().trim();
        boolean newIsRef=cbIsReference.isSelected();
        String newPlace =tfPlacement.getText().trim();

        Work w=target.getWork(); Author oldA=w.getAuthor();

        try(Connection conn=DbUtil.getConnection()){
            conn.setAutoCommit(false);

            
            int authorId;
            if(oldA!=null && oldA.getFirstName().equals(newFN) && oldA.getLastName().equals(newLN)){
                authorId=oldA.getAuthorID();
                try(PreparedStatement ps=conn.prepareStatement(
                    "UPDATE Author SET FirstName=?,LastName=? WHERE AuthorID=?")){
                    ps.setString(1,newFN);ps.setString(2,newLN);ps.setInt(3,authorId);
                    ps.executeUpdate();
                }
            }else{
                
                try(PreparedStatement ps=conn.prepareStatement(
                    "SELECT AuthorID FROM Author WHERE FirstName=? AND LastName=?")){
                    ps.setString(1,newFN);ps.setString(2,newLN);
                    ResultSet rs=ps.executeQuery();
                    if(rs.next()) authorId=rs.getInt(1);
                    else{
                        try(PreparedStatement ins=conn.prepareStatement(
                            "INSERT INTO Author (FirstName,LastName) VALUES (?,?)",
                            Statement.RETURN_GENERATED_KEYS)){
                            ins.setString(1,newFN);ins.setString(2,newLN);
                            ins.executeUpdate(); ResultSet k=ins.getGeneratedKeys();k.next();
                            authorId=k.getInt(1);
                        }
                    }
                }
                
                try(PreparedStatement ps=conn.prepareStatement(
                    "UPDATE WorkAuthor SET AuthorID=? WHERE WorkID=?")){
                    ps.setInt(1,authorId);ps.setInt(2,w.getWorkID());ps.executeUpdate();
                }
            }

            //Update work
            try(PreparedStatement ps=conn.prepareStatement(
                "UPDATE Work SET WorkTitle=?,ISBN=?,WorkType=?,WorkDesc=?,Year=? WHERE WorkID=?")){
                ps.setString(1,newTTL);ps.setString(2,newISBN);ps.setString(3,newType);
                ps.setString(4,newDesc);ps.setInt(5,newYr);ps.setInt(6,w.getWorkID());
                ps.executeUpdate();
            }

            //Update Copy
            try(PreparedStatement ps=conn.prepareStatement(
                "UPDATE Copy SET CopyBarcode=?,IsReference=?,CopyPlacement=? WHERE CopyBarcode=?")){
                ps.setString(1,newBC);ps.setBoolean(2,newIsRef);
                ps.setString(3,newPlace);ps.setString(4,sel.getBarcode());
                ps.executeUpdate();
            }

            conn.commit();

            //Update cache
            if(oldA==null || oldA.getAuthorID()!=authorId){
                Author newA=new Author(authorId,newFN,newLN);
                Author.arrayAuthorsGlobal.add(newA);
                w.setAuthor(newA);
            }else{
                oldA.setFirstName(newFN); oldA.setLastName(newLN);
            }

            w.setTitle      (newTTL);
            w.setIsbn       (newISBN);
            w.setType       (newType);
            w.setDescription(newDesc);
            w.setYear       (newYr);

            target.setBarcode      (newBC);
            target.setIsReference  (newIsRef);
            target.setCopyPlacement(newPlace);

            loadData(); clear();
        }catch(SQLException e){e.printStackTrace();err(e.getMessage());}
    }

    /* ---------- DELETE ---------- */
    @FXML
    private void handleDelete(MouseEvent ev){
        CopyEntry sel=tvCRUD.getSelectionModel().getSelectedItem();
        if(sel==null){err("Select a copy to delete");return;}
        Alert conf=new Alert(Alert.AlertType.CONFIRMATION,
            "Delete copy \""+sel.getBarcode()+"\" ?",ButtonType.YES,ButtonType.NO);
        conf.setHeaderText(null); conf.showAndWait();
        if(conf.getResult()!=ButtonType.YES)return;

        try(Connection conn=DbUtil.getConnection();
            PreparedStatement ps=conn.prepareStatement(
                "DELETE FROM Copy WHERE CopyBarcode=?")){
            ps.setString(1,sel.getBarcode()); ps.executeUpdate();
        }catch(SQLException e){e.printStackTrace();err(e.getMessage());return;}

        Copy.arrayCopiesGlobal.removeIf(c->c.getBarcode().equals(sel.getBarcode()));
        loadData();
    }

    //Return
    @FXML private void handleReturn(MouseEvent ev){
        ((Button)ev.getSource()).getScene().getWindow().hide();
    }

    //Helpers
    private static boolean requiresISBN(String type){
        return "course literature".equals(type)||"other literature".equals(type);
    }
    private int parseYear(){
        if(tfYear.getText().isBlank())return 0;
        try{return Integer.parseInt(tfYear.getText().trim());}
        catch(NumberFormatException e){err("Year must be a number");return -1;}
    }


    private void err(String m){
        new Alert(Alert.AlertType.ERROR,m,ButtonType.OK).showAndWait();
    }
    
    
    private void clear(){
        tfBarcode.clear();tfTitle.clear();tfISBN.clear();tfPlacement.clear();
        tfFirstName.clear();tfLastName.clear();tfYear.clear();tfDescription.clear();
        cbWorkType.setValue(null);cbIsReference.setSelected(false);
    }

    /**
     * Updates the status of a copy (used by LoanController and others).
     * @param copyID    ID of the copy to be updated
     * @param newStatus New status ("available", "loaned", etc)
     * @return true if update was successful, false otherwise
     */
    public static boolean updateCopyStatus(int copyID, String newStatus){
        final String SQL = "UPDATE Copy SET CopyStatus = ? WHERE CopyID = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, newStatus);
            ps.setInt   (2, copyID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
