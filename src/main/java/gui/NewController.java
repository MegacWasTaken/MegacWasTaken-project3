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

public class NewController {

    public static Stage stage;
    public static TreeView<String> tree;

    public static void setStage(Stage stage1) {
        stage = stage1;
    }

    public static void setTreeView(TreeView<String> treeView) {
        tree = treeView;
    }

    // to establish the panel, that we can set here, take it from the controller
    // (pass GUI or something?)
    // pass in the constructor
    // public String getPath() {
    // takes a property of the root defining in the panel, returns current route
    // }

    // TO DO: make this create data from

    // Language: make this the first keyword
    // Keywords field: limit the amount (maybe 50?) -> store in string field of
    // snippet object
    // filepath: take the filepath (make sure to make this read only, handle null
    // errors)
    // code snippet text: just take as a big string? or organize another way?

    public void newSnippetButtonClicked() {
        System.out.println("hi");
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