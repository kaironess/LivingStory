package toolUI.statMenu;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sharedClasses.Stat;

public class StatController implements Initializable {
        @FXML
        private AnchorPane optionPane;
        @FXML
        private ListView statList;
        @FXML
        private Label newStatCmd;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupStatList();
    }
    
    private void setupStatList() {
        newStatCmd.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TextInputDialog saveDialog = new TextInputDialog();
                saveDialog.setTitle("New Stat");
                saveDialog.setHeaderText(null);
                saveDialog.setContentText("Please enter a name for the new stat.");

                // Traditional way to get the response value.
                Optional<String> result = saveDialog.showAndWait();
                if (result.isPresent()){
                    Stat myStat = new Stat(result.get());
                }
            }
        });
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
//        String statname = "";
//        int statvalue = 0;
//        
//        if ((newStatNameField.getText() != null && !newStatNameField.getText().isEmpty())) {
//             statname = newStatNameField.getText();
//        }
//        
//        if ((newStatInitField.getText() != null && !newStatInitField.getText().isEmpty())) {
//            String input = newStatInitField.getText();
//            if (input.matches("\\d*")) {
//                statvalue = Integer.parseInt(input);
//            }
//            
//            Stat myStat = new Stat(statname, statvalue);
//            System.out.println("Saved Stat Name: " + myStat.getName());
//            System.out.println("Saved Stat Initial Val: " + myStat.getCount());
//
//        }
    }

}
