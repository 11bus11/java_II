package library;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        scene.setRoot(loadFXML(fxml));
        stage.sizeToScene();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fl = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fl.load();
    }

    public static void main(String[] args) {
        
        User  .createUsers();              
        Author.createAuthors();            
        Work  .createWorks();             

        
        Copy.arrayCopiesGlobal.size();     

        if (!User.arrayUsersGlobal.isEmpty())
        //Gör till kommentar om Log in ska fungera
            isLoggedIn = User.arrayUsersGlobal.get(0);   

        launch();
    }
}
