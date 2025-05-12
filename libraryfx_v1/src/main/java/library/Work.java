package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.sql.DataSource;

public class Work {
    private int workID;
    private String title;
    private String isbn;
    private String type;
    private String description;
    private Author author;
    private int year;

    public Work(int workID, String title, String isbn, String type, String description, Author author, int year) {
        this.workID = workID;
        this.title = title;
        this.isbn = isbn;
        this.type = type;
        this.description = description;
        this.author = author;
        this.year = year;
    }
    
    public static int getWorkID(Work work)       { return work.workID; }
    public String getTitle()       { return title; }
    public String getIsbn()        { return isbn; }
    public String getType()        { return type; }
    public String getDescription() { return description; }

    //creating the works from database
    public static ArrayList<Work> createWorks() {
        DataSource dataSource = DbUtil.createDataSource();
        ArrayList <Work> arrayWorks = new ArrayList<Work>();
            
        try (Connection connection = dataSource.getConnection()) {
            
            ResultSet resultSetWork= connection.createStatement().executeQuery("select * from Work");
            while(resultSetWork.next()){
                int workID = resultSetWork.getInt("WorkID");
                String title = resultSetWork.getString("WorkTitle");
                String isbn = resultSetWork.getString("ISBN");
                String type = resultSetWork.getString("WorkType");
                String description = resultSetWork.getString("WorkDesc");
                int year = resultSetWork.getInt("Year");
                Work work = new Work(workID, title, isbn, type, description, author, year);
                arrayWorks.add(work);
                System.out.println(work.workID);
            }


                
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return arrayWorks;
    }

    //Global variable containing all works
    static ArrayList <Work> arrayWorksGlobal = createWorks();
}

