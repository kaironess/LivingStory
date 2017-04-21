package toolUI.charMenu;

import java.net.URL;
import java.util.ResourceBundle;

import creationToolClasses.FrameManager;
import creationToolClasses.WIP;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import sharedClasses.Frame;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
    private ListView<String> charList;
    
    
    // --------------------------------------------------------------------------
    //                              UI INITIALIZATIONS
    // --------------------------------------------------------------------------
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        
        // Initialize character list
        ObservableList<String> seasonList = FXCollections.<String>observableArrayList("Spring", "Summer", "Fall", "Winter");
        charList.setItems(seasonList);
    }
    
}
