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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    // Consumer : Interface : we implement accept() to perform our action
    // We pass the current node, to which we are adding the new item with result
    // from the prompt
    private class FolderCreationHandler implements Consumer<String> {
        private TreeItem<String> parentItem;

        public FolderCreationHandler(TreeItem<String> parentItem) {
            this.parentItem = parentItem;
        }

        public void accept(String newFolderName) {
            TreeItem<String> newFolderItem = new TreeItem<>(newFolderName);
            parentItem.getChildren().add(newFolderItem);
            // Expands after creating
            parentItem.setExpanded(true);
        }
    }

    // Once loaded, always calls initialize
    public void initialize() {
        // Create base folders
        loadDirectoryStructure();
        // Dynamic way for the user to add additional folders
        setupContextMenu();
    }

    private void loadDirectoryStructure() {
        // our TreeView -- directoryTree -- requires a root TreeItem<String>. All other
        // TreeItems spawn from this
        // We hide it, but still exists for hierarchy purposes
        TreeItem<String> rootItem = new TreeItem<>("Root");
        directoryTree.setRoot(rootItem);
        directoryTree.setShowRoot(false);

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

    // sets it so all initial elements have the right click functionality, which
    // allows new folders to be brought into existence
    private void setupContextMenu() {
        // Cell Factory: creates new nodes on the tree
        // At INIT just sets this, but then
        directoryTree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            // JavaFX calls this when a cell is updated / created
            @Override
            public TreeCell<String> call(TreeView<String> tv) {
                final TreeCell<String> cell = new TreeCell<String>() {

                    // updateItem takes care of JavaFX updating stuff (setting up the folder as we
                    // want it, as specified by user)
                    // then, we set additional properties (make it so if new folder right clicked,
                    // still works)
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setContextMenu(null);
                        } else {
                            setText(item);

                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem newFolderItem = new MenuItem("New Folder");
                            newFolderItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    createNewFolder(getTreeItem());
                                }
                            });
                            contextMenu.getItems().add(newFolderItem);
                            // Now, when we right click on a folder, this is called
                            // "New Folder" Pops up, if clicked, then createNewFolder is called
                            setContextMenu(contextMenu);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void createNewFolder(TreeItem<String> parentItem) {
        // TextInputDialog: tool that opens a window
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Folder");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Name");

        // Use TextInputDialog's .showAndWait() to take input from the window, then
        // close when submitted (this is done already by the class)
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            FolderCreationHandler handler = new FolderCreationHandler(parentItem);
            handler.accept(result.get());
        }
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