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
    @FXML
    private TreeView<String> tree;

    public void setStage(Stage stage1) {
        stage = stage1;
    }

    public void setTreeView(TreeView<String> treeView) {
        this.tree = treeView;
    }

    @FXML
    private void initialize() {
        loadDirectoryStructure();
    }

    // ALL OF THE LOADING:

    public void loadDirectoryStructure() {
        FolderWithPath rootItem = new FolderWithPath("Root", "");
        tree.setRoot(rootItem);
        // tree.setShowRoot(false);

        FolderWithPath exampleFolder = new FolderWithPath("Example Folder", "/");
        rootItem.getChildren().add(exampleFolder);
        System.out.println("finished loading");
    }

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