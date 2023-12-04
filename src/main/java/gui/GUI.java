package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class GUI extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Load AppState before loading FXML
        String basePathLoad = System.getProperty("user.dir") + "/src/AppState.ser";
        System.out.println("Loading AppState at startup from: " + basePathLoad);
        AppState.loadStateFromFile(basePathLoad);

        System.out.println("Done loading");

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
