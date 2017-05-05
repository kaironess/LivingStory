package toolUI.musicMenu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import creationToolClasses.MusicManager;
import creationToolClasses.StatManager;
import creationToolClasses.WIP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sharedClasses.Frame;
import sharedClasses.Music;
import sharedClasses.MusicTrigger;
import sharedClasses.Stat;

public class MusicController implements Initializable {
    private WIP wip = WIP.getWIP();
    
    @FXML
    private AnchorPane musicMenuAnchor, musicDisplayPane;
    @FXML
    private ListView musicList;
    @FXML
    private Label newMusic;
    @FXML
    private TextField curMusicName;
    @FXML
    private VBox musicInfoBox, musicControlBox;
    
    private Music curMusic;
    private boolean addedHandler = false;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        musicInfoBox.setVisible(false);
        
        newMusic.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addMusic();
            }
        });
        
        musicList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newMusic.setPrefWidth(getWidth);
            }
        });
        
        displayMusicList();
    }
    
    private void addMusic() {
        if (curMusic != null)
            curMusic.stop();
        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {   
            Music newMusic = new Music(file.getName(), file.getAbsolutePath());
            wip.musics.add(newMusic);
            displayMusicList();
        }
    }
    
    private void displayMusicList() {
        musicList.getItems().clear();
        musicList.getItems().add(newMusic);
        
        for (Music music : wip.musics) {
            Label musicLabel = new Label(music.getName());
            
            musicLabel.setPrefWidth(musicList.getWidth());
            newMusic.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    musicLabel.setPrefWidth(getWidth);
                }
            });
            
            addDisplayTrigger(musicLabel, music);
            
            musicList.getItems().add(musicLabel);
        }
    }
    
    // Adds functionality to each label to display their music's info
    private void addDisplayTrigger(Label musicLabel, Music music) {
        musicLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (curMusic != null)
                    curMusic.stop();
                
                musicInfoBox.setVisible(true);
                curMusic = music;
                curMusicName.setText(curMusic.getName());
                //setupMusicControls();
            }
        });
    }
    
    /**
     * Unimplemented for now.
    private void setupMusicControls() {
        musicControlBox.getChildren().clear();
        
        Label musicLabel = new Label("Music Control");
        
        // Create the volume slider
        Slider posSlider = new Slider(0, 1.0, 1.0);
        posSlider.setMajorTickUnit(.1);
        posSlider.setMinorTickCount(0);
        
        musicControlBox.getChildren().add(musicLabel);
        musicControlBox.getChildren().add(posSlider);
    }
    */
    
    @FXML
    private void changeMusicInfo() {
        curMusic.setName(curMusicName.getText());
        displayMusicList();
    }
    
    @FXML
    private void playCurMusic() {
        // Add the handler to stop the music if it hasnt been added yet
        if (!addedHandler) {
            musicMenuAnchor.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    if (curMusic != null)
                        curMusic.stop();
                }
            });
        }
        
        curMusic.play();
    }
    
    @FXML 
    private void pauseCurMusic() {
        curMusic.pause();
    }
    
    @FXML
    private void stopCurMusic() {
        curMusic.stop();
    }
    
    @FXML
    private void deleteMusic() {
        String curMusicName = curMusic.getName();
        
        // Show a dialog asking if the player really wants to delete the stat
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Delete Music");
        confirm.setHeaderText(null);
        confirm.setContentText("You are about to delete " + curMusicName + ". Is this okay?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK){
            curMusic.stop();
            
            wip.deleteMusic(curMusic);
            
            curMusic = null;
            musicInfoBox.setVisible(false);
            displayMusicList();
        }
    }
}
