package library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CopyForTable {
    private final StringProperty barcode      = new SimpleStringProperty();
    private final StringProperty title        = new SimpleStringProperty();
    private final StringProperty isbn         = new SimpleStringProperty();

    public CopyForTable(String barcode, String title, String isbn)
    {
      this.barcode     .set(barcode);
      this.title       .set(title);
      //this.year        .set(year);
      this.isbn        .set(isbn);
    }
    // getters used by your TableColumn<PropertyValueFactory>…
    public String getBarcode()     { return barcode.get(); }
    public String getTitle()       { return title.get(); }
    //public int    getYear()        { return year.get(); }
    public String getIsbn()        { return isbn.get(); }
}
