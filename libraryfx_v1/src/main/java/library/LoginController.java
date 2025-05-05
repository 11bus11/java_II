package library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.ListIterator;

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

    public static String getPassword(ArrayList <User> users, String username) {
        String result = "";
        //int i = 0;
        User tester;
        ListIterator<User> it = users.listIterator(users.size());
        while (it.hasPrevious()) {
            System.out.println(it.previous());
            tester = it.previous();
            if (tester.email.equals(username)) {
                result = tester.email;
            }
        }
        System.out.println(result);
        

        return result;
    }

}

