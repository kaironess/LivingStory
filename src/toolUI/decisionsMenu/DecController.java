package toolUI.decisionsMenu;

import java.net.URL;
import java.util.ResourceBundle;

import creationToolClasses.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sharedClasses.*;

public class DecController implements Initializable {
    private WIP wip = WIP.getWIP();
    private Frame currFrame;
    
    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane optionPane, actionPane, newDecAnchor, decInfoPane;

    @FXML
    private SplitPane toolPane;
    
    @FXML
    private GridPane decInfoGrid;
    
    @FXML
    private VBox decInfoVBox;
    
    @FXML
    private ListView decisionList;
    
    @FXML
    private Label newDecCmd;
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // stuff
        currFrame = FrameManager.getCurFrame();
    }
}
