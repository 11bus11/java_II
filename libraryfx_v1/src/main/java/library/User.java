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

    
}

