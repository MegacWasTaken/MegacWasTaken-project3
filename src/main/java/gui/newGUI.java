import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class newGUI extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        newGUI.primaryStage = primaryStage;

        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "/src/main/resources/gui/HomeScreenGUI.fxml";
        File fxmlFile = new File(filePath);
        URL fxmlLocation = fxmlFile.toURI().toURL();

        // new FXML file from URL from file with full path (assuming user starts at
        // root)
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        HomeScreenController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Code Lib");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}