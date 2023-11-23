import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class newGUI extends Application{

    private static Stage primaryStage;

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        newGUI.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/HomeScreenGUI.fxml"));
        Parent root = loader.load();

        HomeScreenController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Code Lib");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }
}
