package library;

public class Work {
    private final int    workID;
    private final String title;
    private final String isbn;
    private final String type;
    private final String description;
    //private final int    year;
    

    public Work(int workID, String title, String isbn, String type, String description) {
        this.workID = workID;
        this.title = title;
        this.isbn = isbn;
        this.type = type;
        this.description = description;
        //this.year = year;
    }

    

public String getTitle()       { return title; }
public String getIsbn()        { return isbn; }
public String getType()        { return type; }
public String getDescription() { return description; }


}

