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
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import sharedClasses.DecisionReq;
import sharedClasses.Frame;
import sharedClasses.Requirement;

public class DReqController implements Initializable {
    
    WIP wip = WIP.getWIP();
    List<DecisionReq> dr_list;
    Decision currDec;
    
    // --------------------------------------------------------------------------
    //                              UI INJECTIONS
    // --------------------------------------------------------------------------
    
    @FXML
    private AnchorPane basePane;
    
    @FXML
    private ListView decisionReqList;
    
    /**
     * This method is called by the FXMLLoader when injections are complete
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // init
        dr_list = new ArrayList<DecisionReq>();
        
        listViewInit();
        setupDReqList();
    }
    
    private void listViewInit() {
        decisionReqList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        decisionReqList.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<Label> selectedItems =  
                        decisionReqList.getSelectionModel().getSelectedItems();
                
                for(Label l : selectedItems){
                    String text = l.getText();                    
                    try {
                        // get decision ID
                        String id = text.split("[()]")[1];
                        int d_index = Integer.parseInt(id);
                        System.out.println("decision id " + d_index);
                    
                        // get frame
                        String fr = text.split("[\\s\\.]")[1];
                        int f_index = Integer.parseInt(fr);
                        System.out.println("frame " + f_index);
                        
                        // get decision req + curr decision
                        Frame thisFrame = wip.frames.get(f_index);
                        Decision decReq = thisFrame.fromID(d_index);
                        currDec = FrameManager.getCurDec();
                        
                        if (decReq != null) {
                            DecisionReq dr = new DecisionReq(decReq);
                            if (text.charAt(0) == '*') {
                                dr_list.add(dr);
                                currDec.addReq(dr);
                                System.out.println("decreq added! now: " +
                                        currDec.getReqs().size());
                            }
                            else {
                                removeDReq(decReq);
                                System.out.println("decreq removed! size: " +
                                        currDec.getReqs().size());
                            }
                        }
                    } catch (Exception e) {  };
                }
            }
        });
    }
    
    private void removeDReq(Decision dec) {
        ArrayList<Requirement> reqs = currDec.getReqs();
        for (Requirement r : reqs) {
            if (r instanceof DecisionReq) {
                ArrayList<Integer> ids = ((DecisionReq) r).getCurrDecReq();
                if (ids.size() == 1 && ids.get(0) == dec.getId())
                    currDec.removeReq(r);
                    return;
            }
        }
    }

    private void setupDReqList() {
        decisionReqList.getItems().clear();
        Label format = new Label("Decision [FRAME#].[DEC#] (UNIQUE ID)");
        format.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        
        decisionReqList.getItems().add(format);
        
        List<Frame> frame_list = wip.frames;
        List<String> dreq_list = new LinkedList<String>();
        String name;
        int f_index = 0, d_index = 0;
        
        // Get all possible decisions
        for (Frame f : frame_list) {
            List<Decision> d_list = f.getDialogOptions();
            d_index = 0;
            for (Decision d : d_list) {
                name = "Decision " + f_index + "." + d_index;
                name += " (" + d.getId() + ")";
                dreq_list.add(name);
                d_index++;
            }
            f_index++;
        }
        
        // Make clickable labels
        for (String s : dreq_list) {
            Label next = new Label(s);
 
            next.setPrefWidth(decisionReqList.getPrefWidth());
            next.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    next.setPrefWidth(getWidth);
                }
            });
            
            addListener(next);
            decisionReqList.getItems().add(next);
        }      
    }
    
    private void addListener(Label label) {
        
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    String text = label.getText();
                    if (text.charAt(0) == '*') {
                        label.setText(text.substring(1));
                    }
                    else {
                        label.setText("*" + text);
                    }
                }
            }
        });
    }
}
