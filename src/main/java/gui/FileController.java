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

public class FileController {
    private static Stage stage;

    @FXML
    public TreeView<String> directoryTree; // Left-hand Side
    // @FXML
    // private TreeView<String> folderContents; //Right-hand Side
    @FXML
    public TableView<File> fileTable;

    // Replica of the selected Folder's contents (from the left-hand side)
    // to the right-hand side
    // public void folderContents() { //TODO
    // TreeItem<String> selectedFolder = selectedFolder.getSelectedItem();;
    // folderContents.add(selectedFolder.getChildren());

    // }

    // Consumer : Interface : we implement accept() to perform our action
    // We pass the current node, to which we are adding the new item with result
    // from the prompt

    private class FolderCreationHandler implements Consumer<String> {
        private FolderWithPath parentItem;

        public FolderCreationHandler(FolderWithPath parentItem) {
            this.parentItem = parentItem;
        }

        public void accept(String newFolderName) {
            String childPath = parentItem.getPath() + "/" + newFolderName;
            FolderWithPath newFolderItem = new FolderWithPath(newFolderName, childPath);
            parentItem.getChildren().add(newFolderItem);
            System.out.println("Created new path for child of: " + childPath);
            // Expands after creating
            parentItem.setExpanded(true);
        }
    }

    private class SnippetCreationHandler implements Consumer<String> {
        private TreeItem<String> parentItem;

        public SnippetCreationHandler(TreeItem<String> parentItem) {
            this.parentItem = parentItem;
        }

        public void accept(String newSnippetName) {
            TreeItem<String> newSnippet = new TreeItem<>(newSnippetName);
            parentItem.getChildren().add(newSnippet);
            // Expands after creating
            parentItem.setExpanded(true);
        }
    }

    public static void setStage(Stage passedStage) {
        stage = passedStage;
    }

    // Once loaded, always calls initialize
    public void initialize() {
        // Create base folders
        loadDirectoryStructure();
        // Dynamic way for the user to add additional folders
        setupContextMenu();
        // For base folders
        setupTreeViewContextMenu();
    }

    public void loadDirectoryStructure() {
        FolderWithPath rootItem = new FolderWithPath("Root", "");
        directoryTree.setRoot(rootItem);
        directoryTree.setShowRoot(false);

        FolderWithPath exampleFolder = new FolderWithPath("Example Folder", "/");
        rootItem.getChildren().add(exampleFolder);
    }

    // sets it so all initial elements have the right click functionality, which
    // allows new folders to be brought into existence
    public void setupContextMenu() {
        // Cell Factory: creates new nodes on the tree
        // At INIT just sets this, user can also create new
        directoryTree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            // this sets what is called when an element is right clicked
            @Override
            public TreeCell<String> call(TreeView<String> tv) {
                final TreeCell<String> cell = new TreeCell<String>() {

                    // updateItem takes care of JavaFX updating stuff (setting up the folder as we
                    // want it, as specified by user)
                    // then, we set additional properties (make it so if new folder right clicked,
                    // still works)

                    // item String is passed by the right click, of parent of what we are right
                    // clicking
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
                                    FolderWithPath newFolder = (FolderWithPath) getTreeItem();
                                    createNewFolder(newFolder);
                                }
                            });

                            MenuItem newSnippet = new MenuItem("New Snippet");
                            newSnippet.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    createNewSnippet(getTreeItem());
                                }
                            });

                            MenuItem delete = new MenuItem("Delete");
                            delete.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    delete(getTreeItem());
                                }
                            });

                            contextMenu.getItems().addAll(newFolderItem, newSnippet, delete);
                            // sets for current TreeCell "cell"
                            setContextMenu(contextMenu);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void delete(TreeItem<String> toDelete) {
        if (toDelete != null) {
            toDelete.getParent().getChildren().remove(toDelete);
        }
    }

    public void createNewFolder(FolderWithPath parentFolder) {
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
            FolderCreationHandler handler = new FolderCreationHandler(parentFolder);
            handler.accept(result.get());
        }
    }

    public void createNewSnippet(TreeItem<String> parentItem) {
        // TextInputDialog: tool that opens a window
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Snippet");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Name");

        // Use TextInputDialog's .showAndWait() to take input from the window, then
        // close when submitted (this is done already by the class)
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            SnippetCreationHandler handler = new SnippetCreationHandler(parentItem);
            handler.accept(result.get());
        }
    }

    public void setupTreeViewContextMenu() {
        ContextMenu treeViewContextMenu = new ContextMenu();

        MenuItem addMainFolder = new MenuItem("New Folder");
        addMainFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewMainFolder();
            }
        });

        treeViewContextMenu.getItems().add(addMainFolder);
        directoryTree.setContextMenu(treeViewContextMenu);
    }

    public void createNewMainFolder() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Main Folder");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Name");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            TreeItem<String> newFolderItem = new TreeItem<>(result.get());
            directoryTree.getRoot().getChildren().add(newFolderItem);
        }
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