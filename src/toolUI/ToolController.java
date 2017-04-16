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
    StoryChar currChar = null;
    double currX = 0, currY = 0;
    int currImgIdx;
    ObservableList<String> allChars;
    ObservableList<String> allCharImgs;
    ImageView currImg;
    
    // MAIN/BASE WINDOW INJECTIONS
    @FXML
    private AnchorPane basePane, framePane, propPane;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private MenuBar menuBar;    
    
    @FXML
    private Menu fileMenu, editMenu, viewMenu, helpMenu;
    
    @FXML
    private MenuItem openFile, newStatCommand, saveProject, newDFrame, statList, newBFrame;
    
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
    private TextField charNameField, charXOffset, charYOffset;
    
    @FXML
    private Label charNameDisplay, charNameChangeLabel, charSetImgLabel, charXLabel, charYLabel;
    
    @FXML
    private Button openCharImg, charSaveButton, removeCharButton;

    
    // CREATE STAT WINDOW INJECTIONS
    
    @FXML
    private TextField newStatNameField;
    
    @FXML
    private Label newStatLabel, newStatInit, newStatName;
    
    @FXML
    private Button newStatButton;
    
    @FXML
    private GridPane newStatGrid;
    
    @FXML
    private TextField newStatInitField;
    
    @FXML
    private AnchorPane newStatAnchor;
    
    @FXML
    private VBox newStatVbox;
    
    // --------------------------------------------------------------------------
    //                              UI INITIALIZATIONS
    // --------------------------------------------------------------------------
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        wip = WIP.getWIP();  
        Frame startFrame = new Frame(null);
        FrameManager.setCurFrame(startFrame);
        wip.frames.add(startFrame);
        
        // Display the current frame's dialog label
        dialogInit();
        
        // Initialize frame character items
        charDefaultInit();
    }
    
    /**
     * Initialize current frame character list
     */
    private void charDefaultInit() {
        allChars = FXCollections.observableArrayList("NEW CHAR");
        int char_num = wip.chars.size() - 1;
        while (char_num >= 0) {
            allChars.add(wip.chars.get(char_num).getName());
            char_num--;
        }
        charaChoice.setItems(allChars);
        
        // Hide specific character properties
        charNameDisplay.setVisible(false);
        charNameField.setVisible(false);
        charNameChangeLabel.setVisible(false);
        
        charSetImgLabel.setVisible(false);
        charImgChoice.setVisible(false);
        openCharImg.setVisible(false);
        
        charXLabel.setVisible(false);
        charYLabel.setVisible(false);
        charXOffset.setVisible(false);
        charYOffset.setVisible(false);
        charSaveButton.setVisible(false);
        
        charListenerInit();
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
                       if (index > 0) {
                           // Must check to make sure this character is not already
                           // in the frame
                           Frame currFrame = FrameManager.getCurFrame();
                           if (currFrame.getChar(allChars.get(index)) == null) {
                               currChar = wip.chars.get(new_value.intValue() - 1);
                           }
                           else {
                               currChar = currFrame.getChar(allChars.get(index)).getStoryChar();
                           }
                       } 
                       else { 
                           CharManager.createChar("DEFAULT_NAME", null);
                           currChar = wip.getCharByName("DEFAULT_NAME");
                       }
                       
                       charPropInit();
                   }
                });
        
        // Listener to grab character name changes
        charNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    String newName = charNameField.getText();
                    CharManager.renameChar(currChar.getName(), newName);
                    charNameDisplay.setText(currChar.getName());
                }
            }
        });
        
        // Listener to grab character image selections
        charImgChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       currImgIdx = new_value.intValue();
                       if (currImgIdx > 0) {
                           ImageView im = (ImageView) framePane.lookup("#" + currChar.getName() + "_img");
                           Image charImg = imgConverter(wip.getCharByName(currChar.getName()).getImage(currImgIdx));
                           if (im == null) {
                               currImg.setImage(charImg);
                           }
                           else {
                               currImg = new ImageView(charImg);
                               currImg.setId(currChar.getName() + "_img");
                               framePane.getChildren().add(currImg);
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
                        ImageView im = (ImageView) framePane.lookup("#" + currChar.getName() + "_img");
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
                        ImageView im = (ImageView) framePane.lookup("#" + currChar.getName() + "_img");
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
        int img_num = currChar.getImgNum(), i = 0;
        allCharImgs = FXCollections.observableArrayList();
        while (i < img_num) {
            allCharImgs.add("IMG " + i);
            i++;
        }
        charImgChoice.setItems(allCharImgs);
        
        // Set character properties visible
        charNameDisplay.setText(currChar.getName());
        charNameDisplay.setVisible(true);
        charNameField.setVisible(true);
        charNameChangeLabel.setVisible(true);
        
        charSetImgLabel.setVisible(true);
        charImgChoice.setVisible(true);
        openCharImg.setVisible(true);
        
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
            bgView.setImage(imgConverter(wip.bgs.get(currFrame.getBG())));
            this.framePane.getChildren().add(bgView);
        }
    }
    
    /**
     * Open file explorer and set char image
     * @throws IOException
     */
    @FXML
    private void openImgFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.toURI().toString();
            
            // Save newly chosen char img
            Image charImg = new Image(path);
            CharManager.addImgToChar(currChar.getName(), SwingFXUtils.fromFXImage(charImg, null));
            currX = 0;
            currY = 0;
            currImgIdx = 0;
            
            // Display new char img
            // Doubles as a visual test
            int last = currChar.getImgNum() - 1;
            Image charImgTest = imgConverter(wip.getCharByName(currChar.getName()).getImage(last));
            currImg = new ImageView(charImgTest);
            currImg.setId(currChar.getName() + "_img");
            this.framePane.getChildren().add(currImg);
        }
    }

    /**
     * Save all character property changes
     */
    @FXML
    private void setCharProp() {
        
        FrameManager.removeCharacter(currChar.getName());
        DisplayChar dc = new DisplayChar(currChar, currImgIdx, (int)currX, (int)currY, 1, 1);
        FrameManager.addCharacter(dc);
        
        // Tool updates
        allChars.add(currChar.getName());
        charaChoice.setItems(allChars);
        
    }
    
    /**
     * Remove character from current frame
     * Consecutive removes don't work
     */
    @FXML
    private void removeChar() {
        
        if (currChar != null) {
            FrameManager.removeCharacter(currChar.getName());
            
            // Tool updates
            allChars.remove(currChar.getName());
            charaChoice.setItems(allChars);

            // Hide specific character properties
            charNameDisplay.setVisible(false);
            charNameField.setVisible(false);
            charNameChangeLabel.setVisible(false);
            
            charSetImgLabel.setVisible(false);
            charImgChoice.setVisible(false);
            openCharImg.setVisible(false);
            
            charXLabel.setVisible(false);
            charYLabel.setVisible(false);
            charXOffset.setVisible(false);
            charYOffset.setVisible(false);
            charSaveButton.setVisible(false);
            
            framePane.getChildren().remove((ImageView) framePane.lookup("#" + currChar.getName() + "_img"));
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
    
    // --------------------------------------------------------------------------
    //                              STAT CONTROLS
    // --------------------------------------------------------------------------
    
    @FXML
    void openNewStatDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StatCreateWindow.fxml"));
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
    void enterStatName() {}
    
    @FXML
    void enterStatVal() {}
    
    @FXML
    void saveNewStat() {
        String statname = "";
        int statvalue = 0;
        
        if ((newStatNameField.getText() != null && !newStatNameField.getText().isEmpty())) {
             statname = newStatNameField.getText();
        }
        
        if ((newStatInitField.getText() != null && !newStatInitField.getText().isEmpty())) {
            String input = newStatInitField.getText();
            if (input.matches("\\d*")) {
                statvalue = Integer.parseInt(input);
            }
            
            Stat myStat = new Stat(statname, statvalue);
            System.out.println("Saved Stat Name: " + myStat.getName());
            System.out.println("Saved Stat Initial Val: " + myStat.getCount());
        }
    }

    @FXML
    void openStatList() {}

    
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