package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;


public class Work {

    
    private int    workID;
    private String title;
    private String isbn;
    private String type;
    private String description;
    private Author author;   
    private int    year;

    /* ---------- Construtor ---------- */
    public Work(int workID, String title, String isbn, String type,
                String description, Author author, int year) {
        this.workID      = workID;
        this.title       = title;
        this.isbn        = isbn;
        this.type        = type;
        this.description = description;
        this.author      = author;
        this.year        = year;
    }

    /* ---------- Getters ---------- */
    public int    getWorkID()      { return workID; }
    public String getTitle()       { return title; }
    public String getIsbn()        { return isbn; }
    public String getType()        { return type; }
    public String getDescription() { return description; }
    public Author getAuthor()      { return author; }
    public int    getYear()        { return year; }

    /* ---------- Setters ---------- */
    public void setTitle      (String v){ this.title       = v; }
    public void setIsbn       (String v){ this.isbn        = v; }
    public void setType       (String v){ this.type        = v; }
    public void setDescription(String v){ this.description = v; }
    public void setYear       (int    y){ this.year        = y; }
    public void setAuthor     (Author a){ this.author      = a; }

    
    public static ArrayList<Work> createWorks() {
        ArrayList<Work> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();      
        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Work")) {
            while (rs.next()) {
                list.add(new Work(
                    rs.getInt   ("WorkID"),
                    rs.getString("WorkTitle"),
                    rs.getString("ISBN"),
                    rs.getString("WorkType"),
                    rs.getString("WorkDesc"),
                    null,                                 
                    rs.getInt   ("Year")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /* ---------- global cache ---------- */
    public static final ArrayList<Work> arrayWorksGlobal = createWorks();
}