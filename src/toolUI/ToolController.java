package toolUI;

import sharedClasses.Stat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

public class ToolController implements Initializable {

    @FXML
    private TitledPane frameSetup;

    @FXML
    private GridPane effectsGrid;

    @FXML
    private TitledPane frameEffects;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem newStatCommand;

    @FXML
    private MenuItem saveProject;

    @FXML
    private Accordion accord;

    @FXML
    private Menu viewMenu;

    @FXML
    private AnchorPane framePane;

    @FXML
    private Menu helpMenu;

    @FXML
    private TitledPane frameChara;

    @FXML
    private AnchorPane propPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem newDFrame;

    @FXML
    private MenuItem statList;

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
    
    // CREATE STAT WINDOW INJECTIONS
    
    @FXML
    private TextField newStatNameField;

    @FXML
    private Label newStatLabel;

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

    @FXML
    private Label newStatInit;

    @FXML
    private Label newStatName;
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
      

    }

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
    void openStatList() {

    }

}