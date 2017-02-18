package gameUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;

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
    private MenuItem closeWindowButton;

    @FXML
    private Menu saveMenu;
    
    // AUDIO WINDOW INJECTIONS
    @FXML
    private Label audioTitle;
    
    @FXML
    private Slider volumeSlider;

    @FXML
    private Label musicVol;

    @FXML
    private Slider sfxvolumeSlider;

    @FXML
    private AnchorPane audioWindowBasePane;

    @FXML
    private Label sfxVol;

    @FXML
    private GridPane audioGrid;
    
    @FXML
    private Button closeAudioButton;

    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        
    }
    
    @FXML
    private void saveGameState() {
        System.out.println("SAVE CURR FRAME / GAME STATE");
    }

    @FXML
    private void exitGame() {
        System.out.println("EXIT GAME? BACK TO MAIN MENU?");
    }
    
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) myMenu.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void openAudioDialog() {            
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AudioWindow.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();        
                    Scene scene = new Scene(root1);
                    stage.setScene(scene);  
                    stage.show();
                    
            } 
            catch(Exception e) {
               e.printStackTrace();
            }
    }
    
    @FXML
    private void openDisplayDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DisplayWindow.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();        
                    Scene scene = new Scene(root1);
                    stage.setScene(scene);  
                    stage.show();
                    
            } 
            catch(Exception e) {
               e.printStackTrace();
            }
    }
    
    @FXML
    private void openAboutDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutWindow.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();        
                    Scene scene = new Scene(root1);
                    stage.setScene(scene);  
                    stage.show();
                    
            } 
            catch(Exception e) {
               e.printStackTrace();
            }
    }
    
    @FXML
    private void setAudioVolume() {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (! volumeSlider.isValueChanging()) {
                System.out.println("Vol Slider Value Changed (newValue: " + newValue.intValue() + ")");
            }
        });
        
    }
    
    @FXML
    private void setSFXVolume() {
        sfxvolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (! sfxvolumeSlider.isValueChanging()) {
                System.out.println("SFX Slider Value Changed (newValue: " + newValue.intValue() + ")");
            }
        });

    }
    
    @FXML
    private void closeAudioWindow() {
        Stage stage = (Stage) closeAudioButton.getScene().getWindow();
        stage.close();
    }

}
