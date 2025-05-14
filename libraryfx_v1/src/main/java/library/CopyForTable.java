package library;

import javafx.beans.property.SimpleStringProperty;

public class CopyForTable {

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty workType;
    private final SimpleStringProperty status;

    public CopyForTable(String barcode,
                        String title,
                        String author,
                        String isbn,
                        String workType,
                        String status) {

        this.barcode  = new SimpleStringProperty(barcode);
        this.title    = new SimpleStringProperty(title);
        this.author   = new SimpleStringProperty(author);
        this.isbn     = new SimpleStringProperty(isbn);
        this.workType = new SimpleStringProperty(workType);
        this.status   = new SimpleStringProperty(status);
    }

    public String getBarcode()  { return barcode.get();  }
    public String getTitle()    { return title.get();    }
    public String getAuthor()   { return author.get();   }
    public String getIsbn()     { return isbn.get();     }
    public String getWorkType() { return workType.get(); }
    public String getStatus()   { return status.get();   }
}
