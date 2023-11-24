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

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TO DO :(Later on, more theoretical for now): Figure out how we are going to store previously made files for the specific user
// TO DO: (Later on, more theoretical for now): Figure out how we are going to search efficiently, using this TreeView structure

// TO DO: (Next implementation): make it so users can create folders and maybe drag already existing files between (but not create new here)
// since arent just text files but include other data that they input in the find window
public class FileController {
    private static Stage stage;

    @FXML
    private TreeView<String> directoryTree;
    @FXML
    private TableView<File> fileTable;

    // Once loaded, always calls initialize
    public void initialize() {
        loadDirectoryStructure();
    }

    private void loadDirectoryStructure() {
        // our TreeView -- directoryTree -- requires a root TreeItem<String>. All other
        // TreeItems spawn from this
        TreeItem<String> rootItem = new TreeItem<>("Root");
        directoryTree.setRoot(rootItem);

        TreeItem<String> javaBaseDirectory = new TreeItem<>("Java");
        TreeItem<String> cBaseDirectory = new TreeItem<>("C");
        TreeItem<String> pythonBaseDirectory = new TreeItem<>("Python");
        TreeItem<String> otherBaseDirectory = new TreeItem<>("Other Languages");

        List<TreeItem<String>> items = new ArrayList<>();
        items.add(javaBaseDirectory);
        items.add(cBaseDirectory);
        items.add(pythonBaseDirectory);
        items.add(otherBaseDirectory);
        rootItem.getChildren().addAll(items);

        TreeItem<String> javaExample1 = new TreeItem<>("Java Example 1");
        TreeItem<String> javaExample2 = new TreeItem<>("Java Example 2");

        List<TreeItem<String>> javaItems = new ArrayList<>();
        javaItems.add(javaExample1);
        javaItems.add(javaExample2);
        javaBaseDirectory.getChildren().addAll(javaItems);

    }

    public static void setStage(Stage passedStage) {
        stage = passedStage;
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
