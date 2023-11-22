import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.EventHandler;

public class GUI extends Application {
    private Button saveSnippetButton, findSnippetButton;
    private VBox startRoot;
    private Stage mainStage;

    @Override
    public void start(Stage mainStage) throws Exception {
        this.mainStage = mainStage;
        initializeComponents();

        VBox root = new VBox();
        root.getChildren().addAll(saveSnippetButton, findSnippetButton);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        startRoot = root;

        Scene mainScene = new Scene(root, 300, 250);
        mainStage.setScene(mainScene);
        mainStage.setMinWidth(300);
        mainStage.setMinHeight(250);

        mainStage.setTitle("CodeLib");
        mainStage.widthProperty().addListener(new StageWidthChangeListener());

        mainStage.show();
        updateFontSizes(mainStage.getWidth());
    }

    private void initializeComponents() {
        saveSnippetButton = new Button("Save");
        findSnippetButton = new Button("Find");

        // Instead of a Lambda, use anonymous inner class
        // saveSnippetButton.setOnAction(e -> switchToCreateNewScreen()); <- THIS IS A LAMBDA

        saveSnippetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent newEvent) {
                switchToCreateNewScreen();
            }
        });

        findSnippetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent newEvent) {
                findGUI();
            }
        });
    }

    private void switchToCreateNewScreen() {
        Button testButton = new Button("some random text lol");
        HBox newRoot = new HBox(testButton);
        newRoot.setAlignment(Pos.TOP_RIGHT);
        newRoot.setSpacing(50);

        mainStage.getScene().setRoot(newRoot);
    }

    private void findGUI() { //TODO: proportions
        HBox top = new HBox();
        AnchorPane left = new AnchorPane();
        AnchorPane right = new AnchorPane();
        
        //HBox.setHgrow(right, Priority.ALWAYS);
        top.getChildren().add(left);
        top.getChildren().add(right);

        TextField keywordsField = new TextField("Keywords");
        TextField languageField = new TextField("Language");

        left.getChildren().add(keywordsField);
        right.getChildren().add(languageField);
        
        mainStage.getScene().setRoot(top);
    }

    private void updateFontSizes(double stageWidth) {
        Font font = new Font("Arial", stageWidth / 12);
        saveSnippetButton.setFont(font);
        findSnippetButton.setFont(font);

        saveSnippetButton.setPrefWidth(stageWidth / 1.5);
        findSnippetButton.setPrefWidth(stageWidth / 1.5);

        startRoot.setSpacing(stageWidth / 15);
    }

    private class StageWidthChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            updateFontSizes((double) newValue);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}