package gameUI;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import createdGameClasses.GameController;
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
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Button;

import javafx.scene.layout.*;
import javafx.scene.layout.BackgroundImage;

import javafx.stage.Stage;
import sharedClasses.Frame;


public class UIControl implements Initializable {
    private GameController gc;

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
        // Testing: Load in a Game
        try {
            FileInputStream fis = new FileInputStream("testGame.save");
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            this.gc = (GameController)ois.readObject();
            displayCurFrame();
            
            ois.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void displayCurFrame() {
        Frame curFrame = this.gc.getCurFrame();
        Image bg = imgConverter(this.gc.getCurBG());
        BackgroundImage img = new BackgroundImage((Image)bg, BackgroundRepeat.NO_REPEAT, 
         BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        this.displayPane.setBackground(new Background(img));
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

    private Image imgConverter(java.awt.Image img) {
        BufferedImage bi = (BufferedImage)img;
        
        WritableImage wr = null;
        if (img != null) {
            wr = new WritableImage(bi.getWidth(), bi.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    pw.setArgb(x, y, bi.getRGB(x, y));
                }
            }
        }
        
        return wr;
    }
}
