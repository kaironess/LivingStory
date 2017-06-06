package toolUI.decisionsMenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.DocumentEvent.EventType;

import creationToolClasses.FrameManager;
import creationToolClasses.StatManager;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import sharedClasses.Stat;

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
    
    @FXML
    private TextField requiredNum;
    
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
        
        setupReqList();
        reqListenerInit();
        
        actionPane.setVisible(false);
    }
    
    private void setupReqList() {
        
        // Functionality for the New decision option
        newReqCmd.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currReq = new StatReq();
                currDec.addReq(currReq);
                updateReqList();
                actionPane.setVisible(false);
            }
        });
        
        statReqList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                newReqCmd.setPrefWidth(getWidth);
            }
        });
        
        updateReqList();
    }
    
    private void updateReqList() {
        statReqList.getItems().clear();
        statReqList.getItems().add(newReqCmd);
        
        List<Requirement> reqs = FrameManager.getCurDec().getReqs();
        
        int index = 1;
        for (Requirement req : reqs) {
            if (req instanceof StatReq) {
                Label newReq = new Label("Requirement " + index++);
                newReq.setPrefWidth(newReqCmd.getPrefWidth());
                newReqCmd.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                     Number newWidth) {
                        double getWidth = newWidth.doubleValue();
                        newReq.setPrefWidth(getWidth);
                    }
                });
                addReqDisplay(newReq, (StatReq)req);
                statReqList.getItems().add(newReq);
            }
        }
    }
    
    private void addReqDisplay(Label label, StatReq req) {
        label.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currReq = req;
                statChoice.getItems().clear();
                requiredNum.setText("");
                
                ArrayList<String> statNames = new ArrayList<>();
                for (Stat stat : StatManager.getStats())
                    statNames.add(stat.getName());
                
                statChoice.setItems(FXCollections.observableArrayList(statNames));
                
                if (req.getStatName() != null) {
                    Stat curStat = null;
                    int index = 0;
                    for (Stat stat : StatManager.getStats())
                        if (stat.getName().equals(req.getStatName())) {
                            curStat = stat;
                            break;
                        }
                        else index++;
                    
                    statChoice.getSelectionModel().select(index);
                    requiredNum.setText(Integer.toString(req.getCount()));
                }
                
                actionPane.setVisible(true);
            }
        });
    }
    
    private void reqListenerInit() {
        StatReqController self = this;
        // Listener to grab next frame selection
        statChoice.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
               public void changed(ObservableValue ov, Number value, Number new_value) {
                   if (new_value.intValue() != -1)
                       currReq.setStatName(StatManager.getStats().get(new_value.intValue()).getName());
               }
            });
        requiredNum.textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> arg0, 
                 String oldVal, String newVal) {
                    if (!newVal.equals(""))
                        currReq.setCount(Integer.valueOf(newVal));
                }
               
            });
    }
    
    @FXML
    private void deleteCurReq() {
        currDec.removeReq(currReq);
        currReq = null;
        updateReqList();
        actionPane.setVisible(false);
    }
   
}
