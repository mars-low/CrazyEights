import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A basic JavaFX app starting class, responsible for setting up a new scene and connecting it to the fxml file
 */
public class CrazyEights extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CrazyEights.fxml"));
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}
