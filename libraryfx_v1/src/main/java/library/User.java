package library;

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

    public void createUser() {
        
    }
}
