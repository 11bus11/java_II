package library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LoginController"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        DataSource dataSource = createDataSource();

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
                System.out.println(user.firstName);
		    }
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch();
    }


    private static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }
}
    
//Hejj
