package toolUI.statMenu;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import creationToolClasses.StatManager;
import creationToolClasses.WIP;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sharedClasses.Stat;

public class StatController implements Initializable {
    private WIP wip = WIP.getWIP();
    
    @FXML
    private AnchorPane optionPane, statInfoPane;
    @FXML
    private ListView statList;
    @FXML
    private Label newStatCmd;
    @FXML
    private TextField curStatName, curStatVal;
    
    private Stat curDisplayStat;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        statInfoPane.setVisible(false);
        setupStatList();
    }
    
    // Setups the New Stat Button functionality and updates the stat list
    private void setupStatList() {
        // Functionality for the New Stat option
        newStatCmd.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TextInputDialog saveDialog = new TextInputDialog();
                saveDialog.setTitle("New Stat");
                saveDialog.setHeaderText(null);
                saveDialog.setContentText("Please enter a name for the new stat.");

                // Traditional way to get the response value.
                Optional<String> result = saveDialog.showAndWait();
                if (result.isPresent()){
                    String newStatName = result.get();
                    // If a stat with that name already exists, dont let it be made
                    if (wip.getStatByName(newStatName) == null) {
                        StatManager.createStat(newStatName);
                        updateStatList();
                    }
                    else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Stat Exists");
                        alert.setHeaderText(null);
                        alert.setContentText("A stat with that name already exists.");
                        alert.showAndWait();
                    }
                }
            }
        });
        
        statList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newStatCmd.setPrefWidth(getWidth);
            }
        });
        
        updateStatList();
    }
    
    // Updates the stat list on the left
    private void updateStatList() {
        statList.getItems().clear();
        statList.getItems().add(newStatCmd);
        
        List<Stat> stats = wip.stats;
        
        for (Stat stat : stats) {
            Label newStat = new Label(stat.getName());
 
            newStat.setPrefWidth(newStatCmd.getPrefWidth());
            newStatCmd.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    newStat.setPrefWidth(getWidth);
                }
            });
            
            addStatDisplay(newStat);
            statList.getItems().add(newStat);
        }
    }
    
    // Adds functionality to a label so it displays stat info on the right
    private void addStatDisplay(Label statLabel) {
        // Functionality for each Stat in the list
        statLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                statInfoPane.setVisible(true);
                Stat curStat = wip.getStatByName(statLabel.getText());
                curDisplayStat = curStat;
                // Update the right side's stat info
                curStatName.setText(curStat.getName());
                curStatVal.setText(new Integer(curStat.getCount()).toString());
            }
        });
    }
    
    @FXML
    public void changeStatInfo() {
        curDisplayStat.setCount(Integer.valueOf(curStatVal.getText()));
        curDisplayStat.rename(curStatName.getText());
        
        updateStatList();
    }
    
    @FXML
    public void deleteStat() {
        String curStatName = curDisplayStat.getName();
        
        // Show a dialog asking if the player really wants to delete the stat
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Delete Stat");
        confirm.setHeaderText(null);
        confirm.setContentText("You are about to delete " + curStatName + ". Is this okay?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK){
            StatManager.deleteStat(curStatName);
            curDisplayStat = null;
            statInfoPane.setVisible(false);
            updateStatList();
        }
    }
}
