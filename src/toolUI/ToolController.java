package toolUI;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ToolController implements Initializable {

    @FXML
    private TitledPane frameSetup;

    @FXML
    private GridPane effectsGrid;

    @FXML
    private MenuItem openFile;

    @FXML
    private TitledPane frameEffects;

    @FXML
    private MenuItem saveProject;

    @FXML
    private Menu viewMenu;

    @FXML
    private Accordion accord;

    @FXML
    private Menu helpMenu;

    @FXML
    private AnchorPane framePane;

    @FXML
    private TitledPane frameChara;

    @FXML
    private AnchorPane propPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem newDFrame;

    @FXML
    private MenuItem newBFrame;

    @FXML
    private GridPane setupGrid;

    @FXML
    private AnchorPane basePane;

    @FXML
    private SplitPane toolPane;

    @FXML
    private Menu fileMenu;

    @FXML
    private GridPane charaGrid;
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        // initialize your logic here: all @FXML variables will have been injected

    }

}