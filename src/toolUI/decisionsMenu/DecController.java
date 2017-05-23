package toolUI.decisionsMenu;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import creationToolClasses.*;
import sharedClasses.*;

import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DecController implements Initializable {
    // --------------------------------------------------------------------------
    //                              UI INJECTIONS
    // --------------------------------------------------------------------------
    
    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane optionPane, actionPane, newDecAnchor;

    @FXML
    private SplitPane toolPane;
    
    @FXML
    private GridPane decInfoGrid;
    
    @FXML
    private ListView decisionList;
    
    @FXML
    private Label newDecCmd, dialogLabel, branchLabel, nextFrameLabel, dReqLabel;
    
    @FXML
    private Button saveDecButton, addDReqButton;
    
    @FXML
    private TextArea decDialog;
    
    @FXML
    private ChoiceBox<String> nextFrameChoice;
    
    
    // --------------------------------------------------------------------------
    //                              UI INITIALIZATIONS
    // --------------------------------------------------------------------------
    
    private WIP wip = WIP.getWIP();
    private Decision currDec;
    private int currId;
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        newDecCmd = new Label("NEW DECISION");
        setupDecList();
        
        // Hide char option stuff
        actionPane.setVisible(false);
        decListenerInit();
    }
    
    private void setupDecList() {
        
        // Functionality for the New decision option
        newDecCmd.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FrameManager.addDialogOption("NEW DIALOG OPT");
                currDec = FrameManager.fromText("NEW DIALOG OPT");
                currId = currDec.getId();
                updateDecList();
            }
        });
        
        decisionList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newDecCmd.setPrefWidth(getWidth);
            }
        });
        
        updateDecList();
        
    }
    
    private void updateDecList() {
        decisionList.getItems().clear();
        decisionList.getItems().add(newDecCmd);
        
        List<Decision> dialogs = FrameManager.getDialogOptions();
        
        for (Decision d : dialogs) {
            Label newDec = new Label("Decision " + d.getId());
            newDec.setPrefWidth(newDecCmd.getPrefWidth());
            newDecCmd.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    newDec.setPrefWidth(getWidth);
                }
            });
            addDecDisplay(newDec);
            decisionList.getItems().add(newDec);
        }
    }
    
    private void addDecDisplay(Label decLabel) {
        // Functionality for each character in the list
        decLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Reset fields                
                branchLabel.setText(decLabel.getText());
                nextFrameChoice.getItems().clear();
                currId = Integer.parseInt(decLabel.getText().replaceAll("[\\D]", ""));
                currDec = FrameManager.fromID(currId);
                FrameManager.setCurDec(currDec);
                
                // Update the right side's info
                // frame list
                int i = 0, frameNum = wip.frames.size();
                ArrayList<String> allFrames = new ArrayList<String>();
                while (i < frameNum) {
                    if (!wip.frames.get(i).equals(FrameManager.getCurFrame())) {
                        allFrames.add("FRAME " + i);
                    }
                    i++;
                }
                nextFrameChoice.setItems(FXCollections.observableArrayList(allFrames));
                
                // dialog text area
                decDialog.setText(currDec.getDialog());
                                
                actionPane.setVisible(true);
            }
        });
    }
    
    private void decListenerInit() {
        // Listener to grab next frame selection
        nextFrameChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       int index = new_value.intValue();
                       if (index >= 0) {
                           Frame next = wip.frames.get(index);
                           FrameManager.updateDialogOption(next, currDec.getDialog());
                       }
                   }
                });
    }
    
    @FXML
    private void saveDecDialog() throws IOException {
        String text = decDialog.getText();
        currDec.setDialog(text);
        
        // visual test
        Decision check = FrameManager.fromText(text);
        decDialog.setText(check.getDialog());
    }
    
    @FXML
    private void openDReq() {
        try {
            String fxmlPath = "DecReqsWindow.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    // ???
                }
            });
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    
}
