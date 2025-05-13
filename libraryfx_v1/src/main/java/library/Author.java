package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class Author {
    private int authorID;
    private String firstName;
    private String lastName;

    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static int getAuthorID(Author author)       { return author.authorID; }

    //creating the authors from database
    public static ArrayList<Author> createAuthors() {
        DataSource dataSource = DbUtil.createDataSource();
            ArrayList <Author> arrayAuthors = new ArrayList<Author>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from Author");
                while(resultSet.next()){
                    int authorID = resultSet.getInt("AuthorID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    Author author = new Author(authorID, firstName, lastName);
                    arrayAuthors.add(author);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
        }

            
        return arrayAuthors;
    }


    //Global variable containing all users
    static ArrayList <Author> arrayAuthorsGlobal = createAuthors();
}
