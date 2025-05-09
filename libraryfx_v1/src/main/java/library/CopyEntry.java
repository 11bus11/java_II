package library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CopyEntry {
    
    private final StringProperty  barcode     = new SimpleStringProperty();
    private final StringProperty  title       = new SimpleStringProperty();
    private final StringProperty  author      = new SimpleStringProperty();
    private final StringProperty  isbn       = new SimpleStringProperty();
    private final StringProperty  workType    = new SimpleStringProperty();
    private final StringProperty  placement   = new SimpleStringProperty();

    public CopyEntry(
                     String barcode,
                     String title,
                     String author,
                     String isbn,
                     String workType,
                     String placement)
    {
       
        this.barcode  .set(barcode);
        this.title    .set(title);
        this.author   .set(author);
        this.isbn     .set(isbn);
        this.workType .set(workType);
        this.placement.set(placement);
    }

    
    public String getBarcode()  { return barcode.get();  }
    public String getTitle()    { return title.get();    }
    public String getAuthor()   { return author.get();   }
    public String getIsbn()     { return isbn.get();     }
    public String getWorkType() { return workType.get(); }
    public String getPlacement(){ return placement.get();}


// **add this** so PropertyValueFactory can bind to the property???????????????????????????????????????????????????????
     public StringProperty isbnProperty() {
        return isbn;
    }
}
