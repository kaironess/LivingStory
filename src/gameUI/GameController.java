package gameUI;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class GameController implements Initializable {

    @FXML
    private Menu settingsMenu;

    @FXML
    private BorderPane menuPane;

    @FXML
    private StackPane displayPane;

    @FXML
    private MenuBar myMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private AnchorPane basePane;

    @FXML
    private MenuItem audioSetButton;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private MenuItem displaySetButton;

    @FXML
    private MenuItem saveGameButton;

    @FXML
    private MenuItem quitGameButton;

    @FXML
    private Menu saveMenu;
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }
    
    @FXML
    private void openAudioDialog() {            
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AudioWindow.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));  
                    stage.show();
            } 
            catch(Exception e) {
               e.printStackTrace();
            }
    }

}
