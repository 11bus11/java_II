package library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private Button        button;
    @FXML private PasswordField password;
    @FXML private TextField     username;
    @FXML private Label         wrongLogin;

    /* ---------- LOGIN ---------- */
    @FXML
    public void logIn(ActionEvent event) {

        String userInput  = username.getText().trim();
        String passInput  = password.getText();

        if (userInput.isBlank() || passInput.isBlank()) {
            wrongLogin.setText("Please fill in username and password.");
            return;
        }

        User candidate = findUserByEmail(User.arrayUsersGlobal, userInput);

        if (candidate == null) {
            wrongLogin.setText("User not found.");
            return;
        }

        if (Objects.equals(passInput, candidate.getPassword())) {
            actionLoggedIn(candidate);          // navigates to Home
        } else {
            wrongLogin.setText("Incorrect password.");
        }
    }

    /* ---------- search User by e-mail ---------- */
    private static User findUserByEmail(ArrayList<User> users, String email) {
        ListIterator<User> it = users.listIterator(users.size());
        while (it.hasPrevious()) {
            User u = it.previous();
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    /* ---------- post-login ---------- */
    private static void actionLoggedIn(User loggedIn) {
        try {
            App.isLoggedIn = loggedIn;
            App.setRoot("home");                // loads Home.fxml
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ---------- manual navigation via button (optional) ---------- */
    @FXML public void switchToHome(ActionEvent e) {
        if (App.isLoggedIn != null) {
            actionLoggedIn(App.isLoggedIn);
        } else {
            wrongLogin.setText("Please log in first.");
        }
    }
}
