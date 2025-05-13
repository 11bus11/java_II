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
    private ArrayList<Author> author;
    private int year;

    public Work(int workID, String title, String isbn, String type, String description, ArrayList<Author> author, int year) {
        this.workID = workID;
        this.title = title;
        this.isbn = isbn;
        this.type = type;
        this.description = description;
        this.author = author;
        this.year = year;
    }
    
    public static int getWorkID(Work work)       { return work.workID; }
    public static String getTitle(Work work)       { return work.title; }
    public static String getISBN(Work work)        { return work.isbn; }
    public static String getType()        { return type; }
    public static String getDescription() { return description; }

    //creating the works from database
    public static ArrayList<Work> createWorks() {
        DataSource dataSource = DbUtil.createDataSource();
        ArrayList <Work> arrayWorks = new ArrayList<Work>();
            
        try (Connection connection = dataSource.getConnection()) {
            
            ResultSet resultSetWork= connection.createStatement().executeQuery("select * from Work");
            ResultSet resultSetWorkAuthor= connection.createStatement().executeQuery("select * from WorkAuthor");
            System.out.println(resultSetWork.getString("WorkTitle") + " result");
            while(resultSetWork.next()){
                int workID = resultSetWork.getInt("WorkID");
                String title = resultSetWork.getString("WorkTitle");
                String isbn = resultSetWork.getString("ISBN");
                String type = resultSetWork.getString("WorkType");
                String description = resultSetWork.getString("WorkDesc");
                ArrayList<Author> author = findAuthors(resultSetWork.getInt("AuthorID"), resultSetWorkAuthor);
                int year = resultSetWork.getInt("Year");
                Work work = new Work(workID, title, isbn, type, description, author, year);
                arrayWorks.add(work);
                System.out.println(work.workID + " hdhhd");
            }


                
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return arrayWorks;
    }

     public static ArrayList<Author> findAuthors(int id, ResultSet resultSetAuthor) {
        ArrayList<Author> arrayAuthor = new ArrayList<Author>();
        DataSource dataSource = DbUtil.createDataSource();
   
        try (Connection connection = dataSource.getConnection()) {
            
            while(resultSetAuthor.next()){
                int workID = resultSetAuthor.getInt("WorkID");
                if (workID == id) {
                    int authorID = resultSetAuthor.getInt("AuthorID");
                    int index = 0;
                    while (Author.arrayAuthorsGlobal.size() > index) {
                        if (Author.getAuthorID(Author.arrayAuthorsGlobal.get(index)) == authorID) {
                            arrayAuthor.add(Author.arrayAuthorsGlobal.get(index));
                        }
                       
                        index++;
                    }
                }
            
            } 
        } catch (Exception e) {
            System.out.println("error findAuthor");
        }
                       
        return arrayAuthor;
    }

    //Global variable containing all works
    static ArrayList <Work> arrayWorksGlobal = createWorks();
}

