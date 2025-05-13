package library;

import java.io.IOException;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static User isLoggedIn = null;

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;     
        Parent root = loadFXML("Home");
        scene = new Scene(root);  
        stage.setScene(scene);
        stage.setTitle("Library");
        stage.sizeToScene();      
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        stage.sizeToScene();      
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        ArrayList <Copy> arrayCopies = Copy.createCopies();
        //System.out.println(arrayUsers);
        // tar bort rad för att funka 
        isLoggedIn = User.arrayUsersGlobal.get(0); 
        System.out.println(Loan.arrayLoansGlobal.get(0).copiesLoaned + " " + Loan.arrayLoansGlobal.get(0).borrowDate);
        System.out.println(arrayCopies);

        launch();
    }

    

    public static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }

    
}