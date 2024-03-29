package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Load AppState before loading FXML
        String basePathLoad = System.getProperty("user.dir") + "/src/AppState.ser";
        AppState.loadStateFromFile(basePathLoad);

        // Load database for search results:
        String basePath2 = System.getProperty("user.dir");
        basePath2 = basePath2 + "/src/Database.ser";

        File file = new File(basePath2);
        if (file.exists() && !file.isDirectory()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(basePath2))) {
                HashMap<String, ArrayList<String>> database = (HashMap<String, ArrayList<String>>) in.readObject();
                search.BasicSearch.searchArrayList = database;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            AppState.setInstance(new AppState());
        }

        // Load FXML
        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "/src/main/resources/gui/HomeScreenGUI.fxml";
        File fxmlFile = new File(filePath);
        URL fxmlLocation = fxmlFile.toURI().toURL();

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // Set up the controller
        HomeScreenController controllerInstance = loader.getController();
        controllerInstance.setStage(primaryStage);
        primaryStage.setTitle("CodeLib");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
