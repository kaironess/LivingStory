package toolUI;

import sharedClasses.*;
import creationToolClasses.*;
import creationToolClasses.WIP.BadWIPException;
import createdGameClasses.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser;

public class ToolController implements Initializable {

    WIP wip;
    Label dialogLabel;
    DisplayChar currChar = null;
    double currX = 0, currY = 0;
    int currImgIdx;
    String currStat;
    ObservableList<String> allChars;
    ObservableList<String> allCharImgs;
    ObservableList<String> allSongs;
    ObservableList<String> allStats;
    ObservableList<String> allBGs;
    ImageView currImg;
    
    private String curWIPPath = null;
    
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
    private MenuItem openFile, statMenuCommand, charMenuCommand, saveCurProject, saveNewProject,
     newDFrame, statList, newBFrame;
    
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
    private ChoiceBox<String> bgChoice;
    
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
    private ChoiceBox<String> songChoice, statChoice;
    
    @FXML
    private ToggleButton loopOnToggle, loopOffToggle;
    
    @FXML
    private Button frameDecButton, statChangeButton;
    
    @FXML
    private TextField statChangeText;
    
    @FXML
    private Label changeValLabel, loopLabel;

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
        int char_num = wip.chars.size() - 1, i = 0;
        while (i <= char_num) {
            allChars.add(wip.chars.get(i).getName());
            i++;
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
                               currChar = new DisplayChar(wip.chars.get(index), -1);
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
                           if (currChar.getCharImgIndex() != currImgIdx) {
                               currChar.setCharImg(currImgIdx);
                               Image charImg = imgConverter(currChar.getCharImg());
                               currImg = new ImageView(charImg);
                               currImg.setId(currChar.getCharName() + "_img");
                               framePane.getChildren().add(currImg);
                               dialogLabel.toFront();
                           }
                           // If chosen image doesn't change
                           else {
                               ImageView im = (ImageView) framePane.lookup("#" + currChar.getCharName() + "_img");
                               currImg = im;
                               dialogLabel.toFront();
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
     * Initialize frame dialog box and frame background
     */
    private void dialogInit() {
        allBGs = FXCollections.observableArrayList();
        int bg_num = wip.bgs.size() - 6, i = 0;
        while (i < bg_num) {
            String imgName = wip.bg_paths.get(i);
            allBGs.add(imgName);
            i++;
        }
        bgChoice.setItems(allBGs);
        
        // Listener to grab frame bg selections
        bgChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       currImgIdx = new_value.intValue();
                       if (currImgIdx >= 0) {           
                           FrameManager.editBG(currImgIdx + 6);
                           
                           Frame currFrame = FrameManager.getCurFrame();
                           ImageView bgView = new ImageView();
                           bgView.setId("CURR_BG");
                           bgView.setImage(imgConverter(wip.bgs.get(currFrame.getBG())));
                           framePane.getChildren().add(bgView);
                       }
                   }
        });        
        
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
    /**
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
    } */

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
        statChangeButton.setVisible(false);
        statChangeText.setVisible(false);
        changeValLabel.setVisible(false);
        loopLabel.setVisible(false);
        
        allSongs = FXCollections.observableArrayList();
        int song_num = wip.musics.size() - 1, i = 0;
        while (i <= song_num) {
            allSongs.add(wip.musics.get(song_num).getName());
            i++;
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
                           FrameManager.addMusicTrigger(chosen); // auto PLAY set
                           
                           loopLabel.setVisible(true);
                           loopOnToggle.setVisible(true);
                           loopOffToggle.setVisible(true);
                       }
                   }
        });
        
        final ToggleGroup group = new ToggleGroup();
        loopOnToggle.setToggleGroup(group);
        loopOffToggle.setToggleGroup(group);
        loopOnToggle.setUserData(true);
        loopOffToggle.setUserData(false);
        loopOnToggle.setVisible(false);
        loopOffToggle.setVisible(false);
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                    MusicTrigger mt = FrameManager.getTriggers().get(0); 
                    if (new_toggle == null) {
                        // set no loop by default
                        mt.setLoop(false);
                    }
                    else {
                        mt.setLoop((boolean) group.getSelectedToggle().getUserData());
                    }
                 }
        });
        
        // STAT CHANGE LISTENERS
        allStats = FXCollections.observableArrayList();
        int stat_num = wip.stats.size() - 1;
        i = 0;
        while (i <= stat_num) {
            allStats.add(wip.stats.get(i).getName());
            i++;
        }
        statChoice.setItems(allStats);
        
        // Listener to grab frame music selections
        statChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       int currStatIdx = new_value.intValue();
                       if (currStatIdx >= 0) {
                           String stat = allStats.get(currStatIdx);
                           currStat = stat;
                           StatChange sc = FrameManager.hasStatChange(stat);
                           if (sc != null) {
                               statChangeText.setText("" + sc.getChange());
                           }
                           else {
                               FrameManager.addStatChange(allStats.get(currStatIdx));
                               statChangeText.setText("0");
                           }
                       }
                       statChangeText.setVisible(true);
                       statChangeButton.setVisible(true);
                       changeValLabel.setVisible(true);
                   }
        });
    }
    
    @FXML
    private void openDecDialog() {
        openDialog("decisionsMenu" + File.separator + "DecsWindow.fxml");
    }

    @FXML
    private void saveStatChange() {
        // stat changes are applied at the frame level
        try {
            int change = Integer.parseInt(statChangeText.getText());
            FrameManager.updateStatChange(currStat, change);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
        statChangeText.clear();
        
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
        statChangeText.clear();
        
        // Display the current frame's dialog label
        dialogInit();
        
        // Initialize frame character items
        charDefaultInit();
        
        // Initialize frame effect items
        effectInit();
    }
    
    // OPENING NEW WINDOWS ---------------------------------------------------
    
    
    private void openDialog(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    dialogInit();
                    charDefaultInit();
                    effectInit();
                }
            });  
            stage.show();
        } 
        catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    @FXML
    private void openStatsDialog() {
        openDialog("statMenu" + File.separator + "StatsWindow.fxml");
    }
    
    @FXML
    private void openCharDialog() {
        openDialog("charMenu" + File.separator + "CharsWindow.fxml");
    }
    
    @FXML
    private void openBGDialog() {
        openDialog("backgroundMenu" + File.separator + "BGWindow.fxml");
    }
    
    @FXML
    private void openMusicDialog() {
        openDialog("musicMenu" + File.separator + "MusicWindow.fxml");
    }
    
    @FXML
    private void openWIP() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {    
            try {
                WIP.loadWIP(file.getAbsolutePath());
                curWIPPath = file.getAbsolutePath();
            }
            catch (BadWIPException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Loading Error");
                alert.setHeaderText(null);
                alert.setContentText("This save is not compatible and could not be loaded!");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void saveCurWIP() {
        if (curWIPPath != null) {
            File file = new File(this.curWIPPath);
            if (file != null) {    
                WIP.saveWIP(file.getAbsolutePath());
                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Save Success");
                alert.setHeaderText(null);
                alert.setContentText("Save Successful!");

                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Project");
            alert.setHeaderText(null);
            alert.setContentText("No project is currently loaded that can be saved!");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void saveNewWIP() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {    
            curWIPPath = file.getAbsolutePath();
            WIP.saveWIP(file.getAbsolutePath());
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Success");
            alert.setHeaderText(null);
            alert.setContentText("Save Successful!");

            alert.showAndWait();
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