package toolUI.frameMenu;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import creationToolClasses.FrameManager;
import creationToolClasses.StatManager;
import creationToolClasses.WIP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sharedClasses.Frame;
import sharedClasses.Stat;

public class FrSwitchController implements Initializable {

    WIP wip = WIP.getWIP();
    Frame currFrame;
    
    // INJECTIONS
    
    @FXML
    private AnchorPane basePane;
    
    @FXML
    private ListView frameList;
    
    // CODE
    
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
       currFrame = FrameManager.getCurFrame();   
       setupFrameList();
    }
    
    private void setupFrameList() {
        frameList.getItems().clear();
        
        List<Frame> frame_list = wip.frames;
        System.out.println("Num frame: " + frame_list.size());
        int i = 0;
        
        for (Frame fr : frame_list) {
            Label next = new Label("FRAME " + i);
 
            next.setPrefWidth(next.getPrefWidth());
            next.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
                 Number newWidth) {
                    double getWidth = newWidth.doubleValue();
                    next.setPrefWidth(getWidth);
                }
            });
            
            addListener(next);
            frameList.getItems().add(next);
            i++;
        }        
    }
    
    private void addListener(Label fr) {
        fr.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = fr.getText();
                String num = "" + text.charAt(text.length() - 1);
                int index = Integer.parseInt(num);
                currFrame = wip.frames.get(index);
                FrameManager.setCurFrame(currFrame);
                System.out.println("curr frame changed to " + index);

                //Stage stage = (Stage) basePane.getScene().getWindow();
                //stage.hide();
            }
        });
    }
}
