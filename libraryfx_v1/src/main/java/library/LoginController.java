
package library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;



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
        
        System.out.println(arrayUsers);
        String usernameInput = username.getText();
        String passwordInput = password.getText();

        User loginTry = getPassword(arrayUsers, usernameInput);

        if(passwordInput.equals(loginTry.password)){
            System.out.println("Login done");
            App.isLoggedIn = loginTry;
            actionLoggedIn();
        } else{
            System.out.println("Login Error");
        }
        
        
    }

    public static User getPassword(ArrayList <User> users, String username) {
        User result = null;
        //int i = 0;
        User tester;
        ListIterator<User> it = users.listIterator(users.size());
        while (it.hasPrevious()) {
            tester = it.previous();
            System.out.println(tester.email + " " + tester.password);
            if (tester.email.equals(username)) {
                result = tester;
                break;
            }
        }
        System.out.println(result);
        return result;
    }

    @FXML
    private static void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    private static void actionLoggedIn() {
        try {
            switchToSecondary();
        } catch (Exception e) {
            System.out.println("error dude");
        }
        
    }

}

