package library;

import java.sql.Connection;                     // (remains even if not used)
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

public class User {

    /* ---------- fields (kept as in the original) ---------- */
    int userID;
    String firstName;
    String lastName;
    String email;
    String password;
    String userType;

    public User(int userID, String firstName, String lastName,
                String email, String password, String userType) {

        this.userID    = userID;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.password  = password;
        this.userType  = userType;
    }

    public static ArrayList<User> createUsers() {

        DataSource dataSource = DbUtil.createDataSource();
        ArrayList<User> arrayUsers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement  stmt       = connection.createStatement();
             ResultSet  rs         = stmt.executeQuery("SELECT * FROM User")) {

            while (rs.next()) {
                int    userID    = rs.getInt   ("UserID");
                String firstName = rs.getString("FirstName");
                String lastName  = rs.getString("LastName");
                String email     = rs.getString("Email");
                String password  = rs.getString("Password");
                String userType  = rs.getString("UserType");

                arrayUsers.add(new User(userID, firstName, lastName,
                                        email, password, userType));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayUsers;
    }

    /* ---------- global cache (as in the original) ---------- */
    static ArrayList<User> arrayUsersGlobal = createUsers();

    /* ======================================================
     *  GETTERS added — solve compilation errors
     * ====================================================== */
    public int    getUserID()   { return userID;   }
    public String getFirstName(){ return firstName;}
    public String getLastName() { return lastName; }
    public String getEmail()    { return email;    }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
}
