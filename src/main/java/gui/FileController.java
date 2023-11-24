import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javafx.collections.FXCollections;

public class FileController {
    private static Stage stage;

    public static void setStage(Stage stageParam) {
        stage = stageParam;
    }

    @FXML
    private ListView<String> directoryList;

    public void initialize() {
        loadDefaultDirectories();
    }

    private void loadDefaultDirectories() {
        directoryList.getItems().addAll(Arrays.asList("Java", "Python", "C"));
        FXCollections.sort(directoryList.getItems());
    }

    public void homeButtonClicked() {
        try {
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/HomeScreenGUI.fxml";

            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent homeRoot = loader.load();

            stage.setScene(new Scene(homeRoot, 1080, 600));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
