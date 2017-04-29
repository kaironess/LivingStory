package toolUI;

import sharedClasses.*;
import creationToolClasses.*;
import createdGameClasses.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javafx.embed.swing.SwingFXUtils;

import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
//import java.awt.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class ToolController implements Initializable {

    WIP wip;
    Label dialogLabel;
    DisplayChar currChar = null;
    double currX = 0, currY = 0;
    int currImgIdx;
    ObservableList<String> allChars;
    ObservableList<String> allCharImgs;
    ObservableList<String> allSongs;
    ImageView currImg;
    
    // MAIN/BASE WINDOW INJECTIONS
    @FXML
    private AnchorPane basePane, framePane, propPane;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private MenuBar menuBar;    
    
    @FXML
    private Menu fileMenu, editMenu, viewMenu, statMenu, charMenu;
    
    @FXML
    private MenuItem openFile, statMenuCommand, charMenuCommand, saveProject, newDFrame, statList, newBFrame;
    
    @FXML
    private SplitPane toolPane;
    
    @FXML
    private TitledPane frameSetup, frameChara, frameEffects;
    
    @FXML
    private Accordion accord;
    
    @FXML
    private GridPane setupGrid, effectsGrid, charaGrid;
    
    // FRAME SETUP INJECTIONS
    
    @FXML
    private Button openFrameBG;
    
    @FXML
    private ColorPicker dialogColor;
    
    @FXML
    private TextArea dialogText;
    
    @FXML
    private Button dialogTextButton;
    
    // FRAME CHARACTER INJECTIONS
    
    @FXML
    private ChoiceBox<String> charaChoice, charImgChoice;
    
    @FXML
    private TextField charXOffset, charYOffset;
    
    @FXML
    private Label charNameDisplay, charSetImgLabel, charXLabel, charYLabel;
    
    @FXML
    private Button charSaveButton, removeCharButton;
    
    // FRAME EFFECTS INJECTIONS
    
    @FXML
    private ChoiceBox<String> songChoice;
    
    @FXML
    private ToggleButton loopOnToggle, loopOffToggle;
    
    @FXML
    private Button frameDecButton, frameStatButton;

    // --------------------------------------------------------------------------
    //                              UI INITIALIZATIONS
    // --------------------------------------------------------------------------
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        if (wip == null) {
            wip = WIP.getWIP();  
            Frame startFrame = new Frame(null);
            FrameManager.setCurFrame(startFrame);
            wip.frames.add(startFrame);
            
            // Display the current frame's dialog label
            dialogInit();
            
            // Initialize frame character items
            charDefaultInit();
            charListenerInit();
            
            // Initialize frame effect items
            effectInit();
            
        }
    }
    
    /**
     * Initialize current frame character list
     */
    private void charDefaultInit() {
        allChars = FXCollections.observableArrayList();
        int char_num = wip.chars.size() - 1;
        while (char_num >= 0) {
            allChars.add(wip.chars.get(char_num).getName());
            char_num--;
        }
        charaChoice.setItems(allChars);
        
        // Hide specific character properties
        charNameDisplay.setVisible(false);
        
        charSetImgLabel.setVisible(false);
        charImgChoice.setVisible(false);
        
        charXLabel.setVisible(false);
        charYLabel.setVisible(false);
        charXOffset.setVisible(false);
        charYOffset.setVisible(false);
        charSaveButton.setVisible(false);
        
        //charListenerInit();
    }
    
    /**
     * Initialize all character property listeners
     */
    private void charListenerInit() {
        // Listener to grab character selection
        charaChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       
                       // CHANGE PROPERTY FIELDS TO MATCH CHOSEN CHARACTER
                       int index = new_value.intValue();
                       if (index >= 0) {
                           Frame currFrame = FrameManager.getCurFrame();
                           System.out.println(allChars.get(index));
                           
                           // If char not already in frame, ADD new display char
                           if (currFrame.getChar(allChars.get(index)) == null) {
                               currChar = new DisplayChar(wip.chars.get(index), 0);
                               System.out.println("ADDING DISPLAY CHAR");
                           }
                           // If char already in frame, EDIT a display char
                           else {
                               currChar = currFrame.getChar(allChars.get(index));
                               System.out.println("EDITING DISPLAY CHAR");
                           }
                       }
                       
                       charPropInit();
                   }
                });
        
        // Listener to grab character image selections
        charImgChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       currImgIdx = new_value.intValue();
                       if (currImgIdx >= 0) {                           
                           // If chosen image is a diff image
                           Image charImg = imgConverter(currChar.getCharImg());
                           if (currChar.getCharImgIndex() != currImgIdx) {
                               currChar.setCharImg(currImgIdx);
                               currImg = new ImageView(charImg);
                               currImg.setId(currChar.getCharName() + "_img");
                               framePane.getChildren().add(currImg);
                           }
                           // If chosen image doesn't change
                           else {
                               ImageView im = (ImageView) framePane.lookup("#" + currChar.getCharName() + "_img");
                               currImg = im;
                           }
                       }
                   }
        });
        
        // Listener to grab character img x translations
        charXOffset.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    String x = charXOffset.getText();
                    try {
                        ImageView im = (ImageView) framePane.lookup("#" + currChar.getCharName() + "_img");
                        currX = Double.parseDouble(x);
                        im.setTranslateX(currX);
                    } catch (Exception e) { /* bad input */ };
                }
            }
        });
        
        // Listener to grab character img y translations
        charYOffset.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    String y = charYOffset.getText();
                    try {
                        ImageView im = (ImageView) framePane.lookup("#" + currChar.getCharName() + "_img");
                        currY = Double.parseDouble(y);
                        im.setTranslateY(currY);
                    } catch (Exception e) { /* bad input */ };
                }
            }
        });
    }
    
    /**
     * Initialize current selected character properties
     * Assumes currChar is initialized
     */
    private void charPropInit() {             
        
        // Initialize character image list
        int img_num = currChar.getStoryChar().getImgNum(), i = 0;
        allCharImgs = FXCollections.observableArrayList();
        while (i < img_num) {
            allCharImgs.add("IMG " + i);
            i++;
        }
        charImgChoice.setItems(allCharImgs);
        
        // Set character properties visible
        charNameDisplay.setText(currChar.getCharName());
        charNameDisplay.setVisible(true);
        
        charSetImgLabel.setVisible(true);
        charImgChoice.setVisible(true);
        
        charXLabel.setVisible(true);
        charYLabel.setVisible(true);
        charXOffset.setVisible(true);
        charYOffset.setVisible(true);
        charSaveButton.setVisible(true);
    }
    
    /**
     * Initialize frame dialog box
     */
    private void dialogInit() {
        dialogLabel = new Label();
        dialogLabel.setMinHeight(this.basePane.getPrefHeight() / 8);
        dialogLabel.setPadding(new Insets(10, 10, 10, 10));
        dialogLabel.setWrapText(true);
        dialogLabel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, null)));
        this.framePane.setLeftAnchor(dialogLabel, 10.0);
        this.framePane.setRightAnchor(dialogLabel, 10.0);
        this.framePane.setBottomAnchor(dialogLabel, 10.0);
        this.framePane.getChildren().addAll(dialogLabel);
    }
    
    /**
     * Open file explorer and set bg image
     * @throws IOException
     */
    //      Just remember that we need to edit our linked list
    //      when adding the menu bgs to the front in the correct order
    //      That functionality will be done later
    @FXML
    private void openBGFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.toURI().toString();
            
            // Save as the frame bg
            Image frameBG = new Image(path);
            wip.bgs.add(SwingFXUtils.fromFXImage(frameBG, null)); // add after menu bgs
            FrameManager.editBG(wip.bgs.size() - 1);
            
            // Display new frame bg
            // Doubles as a visual test
            Frame currFrame = FrameManager.getCurFrame();
            //ImageView bgView = new ImageView(wip.bgs.get(currFrame.getBG()));
            ImageView bgView = new ImageView();
            bgView.setId("CURR_BG");
            bgView.setImage(imgConverter(wip.bgs.get(currFrame.getBG())));
            this.framePane.getChildren().add(bgView);
        }
    }

    /**
     * Save all character property changes
     */
    @FXML
    private void setCharProp() {
        
        currChar.setLeftMargin((int)currX);
        currChar.setTopMargin((int)currY);
        FrameManager.addCharacter(currChar);
        
        // Tool updates
        int index = allChars.indexOf(currChar.getCharName());
        
        if (index >= 0)
        allChars.add(currChar.getCharName());
        charaChoice.setItems(allChars);
        
    }
    
    /**
     * Remove character from current frame
     * Consecutive removes don't work
     */
    @FXML
    private void removeChar() {
        
        if (currChar != null) {
            FrameManager.removeCharacter(currChar.getCharName());
            
            // Tool updates
            allChars.remove(currChar.getCharName());
            charaChoice.setItems(allChars);

            // Hide specific character properties
            charNameDisplay.setVisible(false);
            
            charSetImgLabel.setVisible(false);
            charImgChoice.setVisible(false);
            
            charXLabel.setVisible(false);
            charYLabel.setVisible(false);
            charXOffset.setVisible(false);
            charYOffset.setVisible(false);
            charSaveButton.setVisible(false);
            
            framePane.getChildren().remove((ImageView) framePane.lookup("#" + currChar.getCharName() + "_img"));
            this.framePane.getChildren().remove(currImg);
        }
    }
    
    /**
     * Set color of the current frame's dialog box
     */
    @FXML
    private void setDialogColor() {
        Color c = dialogColor.getValue();
        int red = (int) Math.round(c.getRed() * 255);
        int green = (int) Math.round(c.getGreen() * 255);
        int blue = (int) Math.round(c.getBlue() * 255);
        FrameManager.editDialogRGB(red, green, blue);
        
        // Display new dialog color
        // Doubles as visual test
        Frame currFrame = FrameManager.getCurFrame();
        int val[] = currFrame.getDialogRGB();
        dialogLabel.setStyle("-fx-background-color: rgba(" + val[0] + ", " + 
                val[1] + ", " + val[2] + ", 0.8)");
        dialogLabel.toFront();

    }
    
    /**
     * Set dialog text in dialog box of current frame
     */
    @FXML
    private void setDialogText() {
        String text = dialogText.getText();
        FrameManager.editDialog(text);
        
        // Display new frame dialog
        // Doubles as visual test
        Frame currFrame = FrameManager.getCurFrame();
        dialogLabel.setText(currFrame.getDialog());
        dialogLabel.toFront();
    }
    
    /**
     * Initialize frame effect listeners and lists
     */
    private void effectInit() {
        allSongs = FXCollections.observableArrayList();
        int song_num = wip.musics.size() - 1;
        while (song_num >= 0) {
            allSongs.add(wip.musics.get(song_num).getName());
            song_num--;
        }
        songChoice.setItems(allSongs);
        
        // Listener to grab frame music selections
        songChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       int currSongIdx = new_value.intValue();
                       if (currSongIdx >= 0) {
                           Music chosen = wip.getMusicByName(allSongs.get(currSongIdx));
                           FrameManager.addMusicTrigger(); //new MusicTrigger(chosen, PLAY);
                       }
                   }
        });
        
        final ToggleGroup group = new ToggleGroup();
        loopOnToggle.setToggleGroup(group);
        loopOffToggle.setToggleGroup(group);
        loopOnToggle.setUserData(true);
        loopOffToggle.setUserData(false);
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                    if (new_toggle == null) {
                        // set no loop by default
                    }
                    else {
                        // Gotta get MusicTrigger figured out
                        // setLoop(group.getSelectedToggle().getUserData());
                    }
                 }
        });
        
    }
    
    @FXML
    private void openDecDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
             "decisionsMenu" + File.separator + "DecsWindow.fxml"));
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
    private void openFrameStatDialog() {
        // not sure how to handle this
        // make stat changes by decision or frame?
    }
    
    // --------------------------------------------------------------------------
    //                              TOP MENU CONTROLS
    // --------------------------------------------------------------------------
    
    @FXML
    private void switchDFrame() {
        // By default sets current frame as previous frame of the new frame
        Frame nextFrame = new Frame(FrameManager.getCurFrame());
        FrameManager.setCurFrame(nextFrame);
        wip.frames.add(nextFrame);
        
        // Save curr frame properties
        Color c = dialogColor.getValue();
        int red = (int) Math.round(c.getRed() * 255);
        int green = (int) Math.round(c.getGreen() * 255);
        int blue = (int) Math.round(c.getBlue() * 255);
        FrameManager.editDialogRGB(red, green, blue);
        
        dialogLabel.setText(null);
        
        FrameManager.editBG(wip.bgs.size() - 1);
        
        charXOffset.clear();
        charYOffset.clear();
        
        // Display the current frame's dialog label
        dialogInit();
        
        // Initialize frame character items
        charDefaultInit();
        
        // Initialize frame effect items
        effectInit();
    }
    @FXML
    private void switchBFrame() {
        // By default sets current frame as previous frame of the new frame
        Frame nextFrame = new Frame(FrameManager.getCurFrame());
        FrameManager.setCurFrame(nextFrame);
        wip.frames.add(nextFrame);
        
        // Clear frame related properties
        this.framePane.getChildren().clear();
        
        dialogLabel.setText(null);
        dialogLabel.setStyle(null);
        dialogLabel.toFront();
        dialogColor.setValue(Color.WHITE);
        dialogText.clear();
        this.framePane.getChildren().remove((ImageView) framePane.lookup("#" + "CURR_BG"));
        
        charXOffset.clear();
        charYOffset.clear();
        
        // Display the current frame's dialog label
        dialogInit();
        
        // Initialize frame character items
        charDefaultInit();
        
        // Initialize frame effect items
        effectInit();
    }
    
    @FXML
    void openStatsDialog() {
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                 "statMenu" + File.separator + "StatsWindow.fxml"));
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
    void openCharDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
             "charMenu" + File.separator + "CharsWindow.fxml"));
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
    
    // Converts a regular Java Image to a JavaFX Image
    private javafx.scene.image.Image imgConverter(java.awt.Image img) {
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