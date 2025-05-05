package library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class LoginController {

    @FXML
    private Button button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogin;

    @FXML
    private void logIn(ActionEvent event) {
        String usernameInput = username.getText();
        String passwordInput = password.getText();

        if(usernameInput.equals("nicagu") && passwordInput.equals("12345")){
            System.out.println("Login done");
        } else{
            System.out.println("Login Error");
        }

        
    }

}

