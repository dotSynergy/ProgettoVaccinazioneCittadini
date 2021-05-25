package cittadini;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;

/**
 * The App class, tasked with initializing our window and application.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue -
 *       @author Andrea Pini - mat.740675
 *
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 570, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets root.
     *
     * @param fxml the fxml
     * @throws IOException the io exception
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }

}