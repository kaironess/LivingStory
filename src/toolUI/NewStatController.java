package toolUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sharedClasses.Stat;

public class NewStatController implements Initializable {
    
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        
    }
    
    // --------------------------------------------------------------------------
    //                              STAT CONTROLS
    // --------------------------------------------------------------------------
    
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

}
