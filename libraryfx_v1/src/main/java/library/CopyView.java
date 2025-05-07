package library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CopyView {
    private final StringProperty barcode      = new SimpleStringProperty();
    private final StringProperty title        = new SimpleStringProperty();
    //private final IntegerProperty year        = new SimpleIntegerProperty();
    private final StringProperty isbn         = new SimpleStringProperty();
    private final StringProperty status       = new SimpleStringProperty();
    private final StringProperty description  = new SimpleStringProperty();
    private final StringProperty type         = new SimpleStringProperty();
  
    // ← NEW constructor:
    public CopyView(String barcode, String title, String isbn, String status, String description, String type)
    {
      this.barcode     .set(barcode);
      this.title       .set(title);
      //this.year        .set(year);
      this.isbn        .set(isbn);
      this.status      .set(status);
      this.description .set(description);
      this.type        .set(type);
    }
  
    
  
    // getters used by your TableColumn<PropertyValueFactory>…
    public String getBarcode()     { return barcode.get(); }
    public String getTitle()       { return title.get(); }
    //public int    getYear()        { return year.get(); }
    public String getIsbn()        { return isbn.get(); }
    public String getStatus()      { return status.get(); }
    public String getDescription() { return description.get(); }
    public String getType()        { return type.get(); }
  }
