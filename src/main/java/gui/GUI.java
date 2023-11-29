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

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // HomeScreenController is initialized when the fxml is declared because it is
        // defined there as the controller
        GUI.primaryStage = primaryStage;

        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "/src/main/resources/gui/HomeScreenGUI.fxml";
        File fxmlFile = new File(filePath);
        URL fxmlLocation = fxmlFile.toURI().toURL();

        // new FXML file from URL from file with full path (assuming user starts at
        // root)
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        HomeScreenController.setStage(primaryStage);
        primaryStage.setTitle("Code Lib");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();

        FileController instance = new FileController();
        TreeView<String> tree = instance.directoryTree;
        HomeScreenController.setTree(tree);
    }

    public static void main(String[] args) {
        launch(args);
    }
}