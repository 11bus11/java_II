package library;

import javafx.beans.property.SimpleStringProperty;

public class CopyForReceiptTable {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty dueDate;
    private final SimpleStringProperty title;
    private final SimpleStringProperty workType;

    public CopyForReceiptTable(String barcode,
                        String dueDate,
                        String title,
                        String workType) {

        this.barcode  = new SimpleStringProperty(barcode);
        this.dueDate  = new SimpleStringProperty(dueDate);
        this.title    = new SimpleStringProperty(title);
        this.workType = new SimpleStringProperty(workType);
    }

    public String getBarcode()  { return barcode.get();  }
    public String getTitle()    { return title.get();    }
    public String getDueDate()   { return dueDate.get();   }
    public String getWorkType() { return workType.get(); }
}