import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HomeScreenController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void findButtonClicked() {
        // here, instead of the previous stuff, we just set up the stage to
        // be using a new fxml file that we make with Scene Builder for the "find"
        // screen
        try {
            // Create the path with current dir + specific
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/FindGUI.fxml";

            // Create file with path, take URL from this file
            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            // Create an FXMLLoader from this location, set new root as result of .load() on
            // this
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent findRoot = loader.load();

            // Update scene with this new element as root
            stage.getScene().setRoot(findRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newButtonClicked() {

    }
}