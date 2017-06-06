package toolUI.decisionsMenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import creationToolClasses.FrameManager;
import creationToolClasses.WIP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sharedClasses.Decision;
import sharedClasses.StatReq;
import sharedClasses.Frame;
import sharedClasses.Requirement;

public class StatReqController implements Initializable {
    
    WIP wip = WIP.getWIP();
    List<StatReq> statReqs;
    
    // --------------------------------------------------------------------------
    //                              UI INJECTIONS
    // --------------------------------------------------------------------------
    
    @FXML
    private AnchorPane basePane, actionPane;
    
    @FXML
    private ListView statReqList;
    
    @FXML 
    private Button deleteReqButton;
    
    @FXML
    private ChoiceBox statChoice;
    
    private Label newReqCmd;
    private StatReq currReq;
    private Decision currDec;
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        currDec = FrameManager.getCurDec();
        newReqCmd = new Label("New Stat Requirement");
        statReqs = new ArrayList<StatReq>();
        
        setupDecList();
        
        actionPane.setVisible(false);
    }
    
    private void setupDecList() {
        
        // Functionality for the New decision option
        newReqCmd.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currReq = new StatReq();
                currDec.addReq(currReq);
            }
        });
        
        statReqList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newReqCmd.setPrefWidth(getWidth);
            }
        });
    }
    
    private void updateReqList() {
        statReqList.getItems().clear();
        statReqList.getItems().add(newReqCmd);
        
        List<Requirement> reqs = FrameManager.getCurDec().getReqs();
        
        for (Requirement req : reqs) {
            if (req instanceof StatReq) {
                Label newReq = new Label("Requirement " + req.hashCode());
                newReq.setPrefWidth(newReqCmd.getPrefWidth());
                newReqCmd.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                     Number newWidth) {
                        double getWidth = newWidth.doubleValue();
                        newReq.setPrefWidth(getWidth);
                    }
                });
                addReqDisplay(newReq, req);
                statReqList.getItems().add(newReq);
            }
        }
    }
    
    private void addReqDisplay(Label label, Requirement req) {
        label.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                
                actionPane.setVisible(true);
            }
        });
    }
    
    @FXML
    private void deleteCurReq() {
        currDec.removeReq(currReq);
        updateReqList();
    }
   
}
