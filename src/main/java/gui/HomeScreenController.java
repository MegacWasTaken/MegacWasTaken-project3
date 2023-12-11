package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import search.BasicSearch;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class HomeScreenController {
    private Stage stage;
    public Parent fileRoot;
    @FXML
    public TreeView<String> tree;
    @FXML
    public Button searchButton;
    @FXML
    public ComboBox<String> searchBar;
    @FXML
    public ListView<String> searchResultListView;

    public void setStage(Stage stageParam) {
        stage = stageParam;
        stage.setMinHeight(600);
        stage.setMinWidth(1080);
    }

    private class FolderCreationHandler implements Consumer<String> {
        private TreeItem<String> parent;

        public FolderCreationHandler(TreeItem<String> parent) {
            this.parent = parent;
        }

        public void accept(String newFolderName) {
            String childPath = parent.getValue() + "/" + newFolderName;
            // Handles parsing name from whole path in the CellFactory

            // Check if the existing name already exists before adding
            boolean exists = false;
            for (TreeItem<String> child : parent.getChildren()) {
                if (child.getValue().endsWith("/" + newFolderName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                TreeItem<String> newFolder = new TreeItem<String>(childPath);
                parent.getChildren().add(newFolder);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("A folder with name " + newFolderName + " already exists in this directory");
            }
        }
    }

    private class SnippetCreationHandler implements Consumer<String> {
        private TreeItem<String> parent;

        public SnippetCreationHandler(TreeItem<String> parent) {
            this.parent = parent;
        }

        public void accept(String newSnippetName) {
            String childPath = parent.getValue() + "/" + newSnippetName;

            // update hashmap here
            // Handles parsing name from whole path in the CellFactory

            // Check if the existing name already exists before adding
            boolean exists = false;
            for (TreeItem<String> child : parent.getChildren()) {
                if (child.getValue().endsWith("/" + newSnippetName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                TreeItem<String> newFolder = new TreeItem<String>(childPath);
                parent.getChildren().add(newFolder);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("A folder with name " + newSnippetName + " already exists in this directory");
            }
        }
    }

    public class SearchResultCell extends ListCell<String> {
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String name = item;
                int whereSplit = name.lastIndexOf("/");
                if (whereSplit != -1) {
                    name = item.substring(whereSplit + 1);
                }
                setText(name);

                this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        handleSnippetDoubleClick(item);
                    }
                });
            }
        }
    }

    private void performSearch(final String searchText) {
        Task<ArrayList<String>> searchTask = new Task<ArrayList<String>>() {
            @Override
            protected ArrayList<String> call() throws Exception {
                String[] keywordsArray = searchText.split(" ");

                ArrayList<String> searchResult = BasicSearch.search(keywordsArray);
                Collections.sort(searchResult);
                return searchResult;
            }
        };

        searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                // System.out.println("entered succeeded");

                ArrayList<String> searchResult = searchTask.getValue();
                String valueSearch = searchBar.getEditor().getText();
                searchBar.getItems().clear();
                if (!searchResult.isEmpty()) {
                    searchBar.getItems().addAll(searchResult);
                    searchBar.show();
                } else {
                    searchBar.hide(); // Hide the dropdown if no results
                }

                if (!searchResult.isEmpty()) {
                    searchBar.setValue(valueSearch);
                }
            }
        });

        Thread thread = new Thread(searchTask);
        thread.start();
    }

    public void initialize() {
        // load directory from appstate
        tree.setRoot(AppState.getInstance().getTreeRoot());

        // set root of TreeView with AppState singleton
        // Create base example folder
        loadDirectoryStructure();

        // Dynamic way for users to add additional folders
        setupContextMenu();

        // For root interaction (right clicking on empty inside root directory)
        setupRootContextMenu();

        tree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
                    if (item != null) {
                        String itemPath = item.getValue();
                        if (AppState.getInstance().getSnippetList().containsKey(itemPath)) {
                            handleSnippetDoubleClick(itemPath);
                        }
                    }
                }
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String keywordInput = searchBar.getEditor().getText().trim();
                // System.out.println("Now searching for key:" + keywordInput);

                // search all snippets to see if their full keywords list matches this
                searchResultListView.getItems().clear();
                for (Entry<String, Snippet> entry : AppState.getInstance().getSnippetList().entrySet()) {
                    String key = entry.getKey();
                    Snippet value = entry.getValue();
                    System.out.println("Current keywords is<" + value.getKeywords() + ">");
                    System.out.println("Comparing against ours which is<" + keywordInput + ">");
                    if (value.getKeywords().equals(keywordInput)) {
                        searchResultListView.getItems().add(key);
                    }
                }
            }
        });

        TextField editor = searchBar.getEditor();
        editor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.trim().isEmpty() && !searchBar.getItems().contains(newValue)) {
                    // System.out.println("option b");
                    performSearch(newValue);
                }
            }
        });

        // set up ListView for displaying the results
        searchResultListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> listView) {
                return new SearchResultCell();
            }
        });

        tree.setVisible(true);
    }

    public void loadDirectoryStructure() {
        TreeItem<String> root = AppState.getInstance().getTreeRoot();

        if (root.getChildren().isEmpty()) {
            TreeItem<String> exampleFolder = new TreeItem<>("Example Folder");
            root.getChildren().add(exampleFolder);
            AppState.getInstance().setTreeRoot(root);
        }

        tree.setRoot(root);
        tree.setShowRoot(false);
    }

    public void handleSnippetDoubleClick(String path) {
        try {
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/NewGUI.fxml";

            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent newRoot = loader.load(); // Load the FXML first

            NewController controller = loader.getController(); // Then get the
                                                               // controller
            if (controller != null) {
                controller.setStage(stage);
                controller.setFilePath(path);
                Snippet currentSnippet = AppState.getInstance().getSnippetList().get(path);
                if (currentSnippet == null) {
                    // System.out.println("The following path was null:" + path);
                    // System.out.println("Here is the list of valid paths: ");
                    for (String a : AppState.getInstance().getSnippetList().keySet()) {
                        // System.out.println(a);
                    }
                }
                controller.code.setText(currentSnippet.getCode());
                controller.keywords.setText(currentSnippet.getKeywords());
                controller.language.setText(currentSnippet.getLanguage());
                // controller.save.setText("Update");
            } else {
                // System.out.println("Controller is null");
            }

            stage.getScene().setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Switch to new scene with fields from object displayed in boxes
    }

    public void setupContextMenu() {
        tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> tv) {
                final TreeCell<String> cell = new TreeCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setContextMenu(null);
                        } else {
                            String folderName = item.substring(item.lastIndexOf("/") + 1);
                            setText("S: " + folderName);

                            ContextMenu contextMenu = new ContextMenu();
                            // for (String key : AppState.getInstance().getSnippetList().keySet()) {
                            // System.out.println(key);
                            // }

                            if (AppState.getInstance().getSnippetList().get(item) == null) {
                                setText(folderName);
                                MenuItem newFolderItem = new MenuItem("New Folder");
                                newFolderItem.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        TreeItem<String> newFolder = getTreeItem();
                                        createNewFolder(newFolder);
                                    }
                                });

                                MenuItem newSnippet = new MenuItem("New Snippet");
                                // New snippet button: collect name with createNewSnippet, then handle rest in
                                // NewController
                                newSnippet.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            TreeItem<String> newSnippet = getTreeItem();
                                            String snippetName = createNewSnippet(newSnippet);

                                            // null indicates a name wasn't provided, or it didn't meet criteria
                                            // abort
                                            if (snippetName == null)
                                                return;

                                            String basePath = System.getProperty("user.dir");
                                            String filePath = basePath + "/src/main/resources/gui/NewGUI.fxml";

                                            File fxmlFile = new File(filePath);
                                            URL fxmlLocation = fxmlFile.toURI().toURL();

                                            FXMLLoader loader = new FXMLLoader(fxmlLocation);
                                            Parent newRoot = loader.load(); // Load the FXML first

                                            NewController controller = loader.getController(); // Then get the
                                                                                               // controller
                                            if (controller != null) {
                                                controller.setStage(stage);
                                                controller.setFilePath(item + snippetName);
                                            } else {
                                                // System.out.println("Controller is null");
                                            }

                                            stage.getScene().setRoot(newRoot);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                contextMenu.getItems().addAll(newFolderItem, newSnippet);
                            }

                            MenuItem delete = new MenuItem("Delete");
                            delete.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    String path = getTreeItem().getValue();
                                    System.out.println("Path is: " + path);
                                    delete(getTreeItem());
                                    // for each key in the database, remove keywords if exist
                                    if (AppState.getInstance().getSnippetList().get(item) != null) {
                                        System.out.println("Item is " + item);
                                        String keywords = AppState.getInstance().getSnippetList().get(item)
                                                .getKeywords();

                                        // System.out.println("Removing full keywords:" + keywords);
                                        BasicSearch.removeKeywords(keywords);
                                        AppState.getInstance().getSnippetList().remove(item);

                                        String basePath = System.getProperty("user.dir");
                                        basePath = basePath + "/src/AppState.ser";
                                        AppState.getInstance().saveStateToFile(basePath);
                                    }
                                }
                            });

                            contextMenu.getItems().addAll(delete);
                            // sets for current TreeCell "cell"
                            setContextMenu(contextMenu);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void setupRootContextMenu() {
        ContextMenu rootMenu = new ContextMenu();

        // Seamless experience for the user
        MenuItem addRootFolder = new MenuItem("New Folder");

        addRootFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewFolder(tree.getRoot());
            }
        });

        MenuItem addRootSnippet = new MenuItem("New Snippet");

        addRootSnippet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String snippetName = createNewSnippet(tree.getRoot());
                    if (snippetName == null)
                        return;

                    String basePath = System.getProperty("user.dir");
                    String filePath = basePath + "/src/main/resources/gui/NewGUI.fxml";

                    File fxmlFile = new File(filePath);
                    URL fxmlLocation = fxmlFile.toURI().toURL();

                    FXMLLoader loader = new FXMLLoader(fxmlLocation);
                    Parent newRoot = loader.load();

                    NewController controller = loader.getController();

                    if (controller != null) {
                        controller.setStage(stage);
                        // This will be added to the HashMap as just the name, since no previous
                        controller.setFilePath(snippetName);
                    } else {
                        // System.out.println("Null controller");
                    }

                    stage.getScene().setRoot(newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rootMenu.getItems().addAll(addRootFolder, addRootSnippet);
        tree.setContextMenu(rootMenu);
    }

    public void delete(TreeItem<String> itemToDelete) {
        if (itemToDelete != null) {
            itemToDelete.getParent().getChildren().remove(itemToDelete);
            AppState.getInstance().setTreeRoot(tree.getRoot());
        }
    }

    public void createNewFolder(TreeItem<String> parentFolder) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Folder");
        dialog.setGraphic(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Name");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String folderName = result.get();
            folderName = folderName.trim();
            FolderCreationHandler handler = new FolderCreationHandler(parentFolder);
            handler.accept(folderName);
            AppState.getInstance().setTreeRoot(tree.getRoot());
        }
    }

    public void removeTreeItem(String path) {
        removeTreeItemRecursive(tree.getRoot(), path);
    }

    private void removeTreeItemRecursive(TreeItem<String> current, String path) {
        for (TreeItem<String> child : new ArrayList<>(current.getChildren())) {
            if (child.getValue().equals(path)) {
                current.getChildren().remove(child);
                return;
            }
            removeTreeItemRecursive(child, path);
        }
    }

    public String createNewSnippet(TreeItem<String> parentFolder) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Snippet");
        dialog.setGraphic(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Name");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String userInput = result.get();
            userInput = userInput.trim();
            SnippetCreationHandler handler = new SnippetCreationHandler(parentFolder);
            handler.accept(userInput);
            AppState.getInstance().setTreeRoot(tree.getRoot());
            return "/" + userInput;
        }
        return null;
    }
}