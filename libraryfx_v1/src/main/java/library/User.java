package library;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class User {
    int userID;
    String firstName;
    String lastName;
    String email;
    String password;
    String userType;
    
    public User(int userID, String firstName, String lastName, String email, String password, String userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
    //creating the users from database
    public static ArrayList<User> createUsers() {
        DataSource dataSource = createDataSource();
            ArrayList <User> arrayUsers = new ArrayList<User>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from User");
                while(resultSet.next()){
                    int userID = resultSet.getInt("UserID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    String password = resultSet.getString("Password");
                    String userType = resultSet.getString("UserType");
                    User user = new User(userID, firstName, lastName, email, password, userType);
                    arrayUsers.add(user);
                    System.out.println(user.lastName);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }

            
            return arrayUsers;
        }


    private static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }

    
}

