package library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.*;

public class LoginController {


    @FXML
    private Button button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogin;

    
    ArrayList <User> arrayUsers = User.createUsers();

    @FXML
    private void logIn(ActionEvent event) {
        
        System.out.println(arrayUsers + "yeyeye");
        String usernameInput = username.getText();
        String passwordInput = password.getText();

        String correctPassword = getPassword(arrayUsers, usernameInput);

        if(passwordInput.equals(correctPassword)){
            System.out.println("Login done");
        } else{
            System.out.println("Login Error");
        }
        
        
    }

    public static String getPassword(ArrayList <User> users, String username) {
        String result = "nope";
        //int i = 0;
        User tester;
        ListIterator<User> it = users.listIterator(users.size());
        while (it.hasPrevious()) {
            tester = it.previous();
            System.out.println(tester.email + " " + tester.password);
            if (tester.email.equals(username)) {
                result = tester.password;
                break;
            }
        }
        System.out.println(result);
        return result;
    }

}

