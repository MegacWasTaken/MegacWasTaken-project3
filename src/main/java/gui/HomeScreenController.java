import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeScreenController {
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void findButtonClicked() { //TODO: proportions
        // here, instead of the previous stuff, we just set up the stage to
        // be using a new fxml file that we make with Scene Builder for the "find" screen
        try{
            Parent findRoot = FXMLLoader.load(getClass().getResource("/gui/FindGUI.fxml"));
            stage.getScene().setRoot(findRoot);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void newButtonClicked(){

    }
}
