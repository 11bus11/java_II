package library;

public class Work {
    private int workID;
    private String title;
    private String isbn;
    private String type;
    private String description;
    private int year;

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

