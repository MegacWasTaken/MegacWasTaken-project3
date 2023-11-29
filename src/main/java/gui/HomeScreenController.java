import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;

// TO DO: Figure out if we are allowed to use file paths like we are doing now (potential issues with this)
// AND REPLACE THIS WITH .getResource() option if we can, although VSCode doesn't like this right now and may not be portable
public class HomeScreenController {
    private static Stage stage;
    public static TreeView<String> tree;
    public static Parent fileRoot;

    public static void setStage(Stage stageParam) {
        stage = stageParam;
    }

    public static void setRoot(Parent root) {
        if (root == null)
            System.out.println("ah man");
        System.out.println("yes man!");
        fileRoot = root;
    }

    // Obtain the TreeView
    public static void setTree(TreeView<String> input) {
        tree = input;
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

    public void findButtonClicked() {
        // here, instead of the previous stuff, we just set up the stage to
        // be using a new fxml file that we make with Scene Builder for the "find"
        // screen
        try {
            // Create the path with current dir + specific
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/FindGUI.fxml";

            // Create file with path, take URL from this file
            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            // Create an FXMLLoader from this location, set new root as result of .load() on
            // this
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            Parent findRoot = loader.load();
            NewController controllerInstance = loader.getController();
            controllerInstance.setStage(stage);
            controllerInstance.setTreeView(tree);

            // Update scene with this new element as root
            stage.getScene().setRoot(findRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newButtonClicked() {
        try {
            String basePath = System.getProperty("user.dir");
            String filePath = basePath + "/src/main/resources/gui/NewGUI.fxml";

            File fxmlFile = new File(filePath);
            URL fxmlLocation = fxmlFile.toURI().toURL();

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent newRoot = loader.load(); // Load the FXML first

            NewController controller = loader.getController(); // Then get the controller
            if (controller != null) {
                controller.setStage(stage);
                controller.setTreeView(tree);
            } else {
                System.out.println("Controller is null");
            }

            stage.getScene().setRoot(newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOrganizationButtonClicked() {
        // now should already hvae the instance created in GUI callee, so sends the tree
        // easily
        // both roots point to the same place
        // but how to access methods? well for that, copy code over
        FileController.setStage(stage);
        stage.getScene().setRoot(fileRoot);
    }
}