package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import search.BasicSearch;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;

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

    static boolean created = false;

    public void setStage(Stage passedStage) {
        stage = passedStage;
        stage.setMinHeight(600);
        stage.setMinWidth(1080);
    }

    public void setFilePath(String path) {
        this.path.setText(path);
    }

    public void newSnippetButtonClicked() {
        try {
            language.setEditable(false);
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

            if (!AppState.getInstance().getSnippetList().containsKey(pathString)) {
                if (!languageString.trim().isEmpty()) {
                    keywordsString = languageString + " " + keywordsString;
                }
            }

            Snippet snip = new Snippet(keywordsString, languageString, pathString, codeString);

            // If update, need to delete old values. Can't modify path, so this will be our
            // benchmark

            // Ping snippet list to see if it exists
            if (AppState.getInstance().getSnippetList().containsKey(pathString)) {
                // Snippet already exists
                // delete existing snippet
                // delete existing keywords in database
                String keywordsSnippet = AppState.getInstance().getSnippetList().get(pathString).getKeywords();
                AppState.getInstance().getSnippetList().remove(pathString);
                BasicSearch.removeKeywords(keywordsSnippet);
            }

            // Update hashmaps with inputted values
            AppState.getInstance().updateSnippetList(pathString, snip);
            BasicSearch.distributeSnippet(snip);

            // System.out.println("After distributing snippet, updated keys in AppState: ");
            for (String key : AppState.getInstance().getSnippetList().keySet()) {
                // System.out.println("\tKey: " + key);
            }

            // System.out.println("After distributing snippet, updated keys in Database
            // (BasicSearch): ");
            for (Entry<String, ArrayList<String>> entry : BasicSearch.searchArrayList.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                // System.out.println("\t \t Key: " + key + "\n\t\t Value: " + value);
            }

            created = true;

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
                // System.out.println("we entered this block");
                homeController.removeTreeItem(path.getText());
            }

            stage.setScene(new Scene(homeRoot, 1080, 600));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}