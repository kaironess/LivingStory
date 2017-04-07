package toolUI;

import sharedClasses.*;
import creationToolClasses.*;
import createdGameClasses.*;
import java.util.*;
import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private GridPane setupGrid, effectsGrid; //charaGrid;
    
    // FRAME SETUP INJECTIONS
    
    @FXML
    private Button openFrameBG;
    
    @FXML
    private ColorPicker dialogColor;
    
    @FXML
    private TextArea dialogText;
    
    @FXML
    private Button dialogTextButton;

    
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
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        wip = WIP.getWIP();  
        Frame startFrame = new Frame(null);
        FrameManager.setCurFrame(startFrame);
        wip.frames.add(startFrame);
        
        // Display the current frame's dialog label
        dialogLabel = new Label();
        dialogLabel.setMinHeight(this.framePane.getPrefWidth() / 8);
        dialogLabel.setPadding(new Insets(10, 10, 10, 10));
        dialogLabel.setWrapText(true);
        dialogLabel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, null)));
        this.framePane.setLeftAnchor(dialogLabel, 10.0);
        this.framePane.setRightAnchor(dialogLabel, 10.0);
        this.framePane.setBottomAnchor(dialogLabel, 10.0);
        this.framePane.getChildren().addAll(dialogLabel);
    }
    
    // Open file explorer and set bg image
    @FXML
    private void openBGFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.toURI().toString();
            
            // Save as the frame bg
            Image frameBG = new Image(path);
            wip.bgs.add(frameBG); // add after menu bgs
            FrameManager.editBG(wip.bgs.size() - 1);
            
            // Display new frame bg
            Frame currFrame = FrameManager.getCurFrame();
            ImageView bgView = new ImageView(wip.bgs.get(currFrame.getBG()));
            this.framePane.getChildren().add(bgView);
        }
    }
    
    @FXML
    private void setDialogColor() {
        Color c = dialogColor.getValue();
        System.out.println("New Color's RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());
        int red = (int) Math.round(c.getRed() * 255);
        int green = (int) Math.round(c.getGreen() * 255);
        int blue = (int) Math.round(c.getBlue() * 255);
        
        dialogLabel.setStyle("-fx-background-color: rgba(" + red + ", " + 
                green + ", " + blue + ", 0.8)");
        dialogLabel.toFront();

    }
    
    @FXML
    private void setDialogText() {
        String text = dialogText.getText();
        FrameManager.editDialog(text);
        
        // Display new frame dialog
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

}