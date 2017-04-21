package toolUI.charMenu;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import creationToolClasses.CharManager;
import creationToolClasses.FrameManager;
import creationToolClasses.WIP;
import sharedClasses.DisplayChar;
import sharedClasses.Frame;
import sharedClasses.StoryChar;

import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.SplitPane;
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

public class CharController implements Initializable {
    // --------------------------------------------------------------------------
    //                              UI INJECTIONS
    // --------------------------------------------------------------------------
    
    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane optionPane, actionPane, newCharAnchor;

    @FXML
    private SplitPane toolPane;
    
    @FXML
    private GridPane charOptionGrid;
    
    @FXML
    private ListView charList;
    
    @FXML
    private Label newCharCmd, charNameLabel, charEditNameLabel, charAddImgLabel, charDelImgLabel;
    
    @FXML
    private Button delCharButton, saveCharButton, openImgButton;
    
    @FXML
    private TextField charNameField;
    
    @FXML
    private ChoiceBox<String> delImgChoice;
    
    
    // --------------------------------------------------------------------------
    //                              UI INITIALIZATIONS
    // --------------------------------------------------------------------------
    
    private WIP wip = WIP.getWIP();
    private StoryChar activeChar;
    private ArrayList<String> allCharImgs;
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        newCharCmd = new Label("New Char");
        setupCharList();
        
        // Hide char option stuff
        actionPane.setVisible(false);
        charListenerInit();
    }
    
    private void setupCharList() {
        
        // Functionality for the New Char option
        newCharCmd.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TextInputDialog saveDialog = new TextInputDialog();
                saveDialog.setTitle("New Character");
                saveDialog.setHeaderText(null);
                saveDialog.setContentText("Please enter a name for the new character.");

                // Traditional way to get the response value.
                Optional<String> result = saveDialog.showAndWait();
                if (result.isPresent()){
                    CharManager.createChar(result.get(), null);
                    updateCharList();
                }
            }
        });
        
        charList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newCharCmd.setPrefWidth(getWidth);
            }
        });
        
        updateCharList();
        
    }
    
    private void updateCharList() {
        charList.getItems().clear();
        charList.getItems().add(newCharCmd);
        
        List<StoryChar> chars = wip.chars;
        
        for (StoryChar sc : chars) {
            Label newChar = new Label(sc.getName());
            newChar.setPrefWidth(newCharCmd.getPrefWidth());
            newCharCmd.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    newChar.setPrefWidth(getWidth);
                }
            });
            
            addCharDisplay(newChar);
            charList.getItems().add(newChar);
        }
    }
    
    private void addCharDisplay(Label charLabel) {
        // Functionality for each character in the list
        charLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                charNameLabel.setText(charLabel.getText());
                
                StoryChar curr = wip.getCharByName(charLabel.getText());
                activeChar = curr;
                
                // Update the right side's info
                // name
                charNameField.setText(curr.getName());
                // img list
                int img_num = curr.getImgNum(), i = 0;
                System.out.println("listener: " + img_num);
                allCharImgs = new ArrayList<String>();
                while (i < img_num) {
                    allCharImgs.add("IMG " + i);
                    i++;
                }
                delImgChoice.setItems(FXCollections.observableArrayList(allCharImgs));
                System.out.println("end listener");
                
                actionPane.setVisible(true);
            }
        });
    }
    
    private void charListenerInit() {
        // Listener to grab character img selection
        delImgChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                   public void changed(ObservableValue ov, 
                           Number value, Number new_value) {
                       
                       int index = new_value.intValue();
                       if (index < activeChar.getImgNum()) {
                           CharManager.deleteCharImg(activeChar.getName(), index);
                       }
                   }
                });
        
        // Listener for name change
        charNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    String x = charNameField.getText();
                    charNameLabel.setText(x);
                    CharManager.renameChar(activeChar.getName(), x);
                    updateCharList();
                }
            }
        });
    }
    
    /**
     * Open file explorer and add char image
     * @throws IOException
     */
    @FXML
    private void openImgFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {            
            // Save new image to character's image list
            Image newCharImg = ImageIO.read(file);
            int index = activeChar.getImgNum();
            
            CharManager.addImgToChar(activeChar.getName(), newCharImg);
            allCharImgs.add("IMG " + index);
            delImgChoice.setItems(FXCollections.observableArrayList(allCharImgs));
        }
    }
    
}
