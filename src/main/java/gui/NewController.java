package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;
import search.BasicSearch;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField path;
    @FXML
    public TextField keywords;
    @FXML
    public TextField language;
    @FXML
    public TextArea code;
    @FXML
    public Button save;

    private Stage stage;

    public void setStage(Stage passedStage) {
        stage = passedStage;
    }

    public void setFilePath(String path) {
        this.path.setText(path);
    }

    public void newSnippetButtonClicked() {
        try {
            save.setText("Save");
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/HomeScreenGUI.fxml";

            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            Parent homeRoot = loader.load();
            HomeScreenController homeController = loader.getController();

            // Collect snippet values provided
            String keywordsString = keywords.getText();
            String languageString = language.getText();
            String pathString = path.getText();
            String codeString = code.getText();

            Snippet snip = new Snippet(keywordsString, languageString, pathString, codeString);

            // Update hashmap with inputted values
            System.out.println("UPDATING NEW SNIPPET IN NEWCONTROLLER");
            AppState.getInstance().updateSnippetList(pathString, snip);

            BasicSearch.distributeSnippet(snip);

            for (String key : AppState.getInstance().getSnippetList().keySet()) {
                System.out.println("Key: " + key);
            }

            homeController.setStage(stage);

            stage.setScene(new Scene(homeRoot, 1080, 600));

        } catch (IOException e) {
            e.printStackTrace();
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
            HomeScreenController homeController = loader.getController();
            homeController.setStage(stage);
            if (!AppState.getInstance().getSnippetList().containsKey(path.getText()) && code.getText().equals("")) {
                System.out.println("we entered this block");
                homeController.removeTreeItem(path.getText());
            }

            stage.setScene(new Scene(homeRoot, 1080, 600));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}