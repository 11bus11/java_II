package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;


public class Author {

    
    private int    authorID;
    private String firstName;
    private String lastName;

    public Author(int authorID, String firstName, String lastName) {
        this.authorID  = authorID;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    /* ---------- Getters ---------- */
    public int    getAuthorID() { return authorID; }
    public String getFirstName(){ return firstName; }
    public String getLastName (){ return lastName;  }

    /* ---------- Setters ---------- */
    public void setFirstName(String v){ this.firstName = v; }
    public void setLastName (String v){ this.lastName  = v; }

    
    public static ArrayList<Author> createAuthors() {
        ArrayList<Author> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();
        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Author")) {
            while (rs.next()) {
                list.add(new Author(
                    rs.getInt   ("AuthorID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    
    public static final ArrayList<Author> arrayAuthorsGlobal = createAuthors();
}