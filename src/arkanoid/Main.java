package arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    
    public static void main(String[] args) {
        
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Scene scene = FXMLLoader.load(getClass().getResource("..\\res\\Main.fxml"));
        stage.setScene(scene);
        stage.setTitle("Arkanoid");
        stage.show();
    }
}
