package toolUI.backgroundMenu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import createdGameClasses.GameController.BGIndex;
import creationToolClasses.CharManager;
import creationToolClasses.StatManager;
import creationToolClasses.WIP;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sharedClasses.Frame;
import sharedClasses.Stat;

public class BGController implements Initializable {
    private WIP wip = WIP.getWIP();
    
    @FXML
    private AnchorPane optionPane, bgDisplayPane, bgMenuAnchor;
    @FXML
    private ListView bgList;
    @FXML
    private Label mainMenuBG, pauseMenuBG, settingsBG, saveMenuBG, loadMenuBG, galleryBG, frameBGs;
    @FXML
    private TextField curStatName, curStatVal;
    @FXML
    private ImageView bgImageView, curFrameIV;
    @FXML
    private StackPane defBGPane;
    @FXML
    private VBox frameImgBox, curFrameBG, bgDisplayVBox;
    @FXML
    private HBox frameImgs;
    @FXML
    private ScrollPane frameBGScroll;
    
    private List<Label> allDefaultLabels;
    private int curLabelIndex, curFrameIndex;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        defBGPane.setVisible(false);
        frameImgBox.setVisible(false);
        
        // Disable vertical scrolling for the box containing all frame bgs
        frameBGScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        frameBGScroll.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() != 0) {
                    event.consume();
                }
            }
        });
        
        curFrameBG.setMaxHeight(bgMenuAnchor.getPrefHeight() * .7);
        curFrameBG.setMinHeight(bgMenuAnchor.getPrefHeight() * .7);
        
        allDefaultLabels = new ArrayList<>();
        allDefaultLabels.add(mainMenuBG);
        allDefaultLabels.add(pauseMenuBG);
        allDefaultLabels.add(settingsBG);
        allDefaultLabels.add(saveMenuBG);
        allDefaultLabels.add(loadMenuBG);
        allDefaultLabels.add(galleryBG);
        
        setupDefaultList();
        
        // Bind the imageView to its parent's size
        bgImageView.fitWidthProperty().bind(this.defBGPane.widthProperty());
        bgImageView.fitHeightProperty().bind(this.defBGPane.heightProperty());

    }
    

    private void setupDefaultList() {
        // Functionality for all the default bg options
        for (int i = 0; i < WIP.BGIndex.values().length; i++) {
            final int index = i;
            allDefaultLabels.get(index).setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    displayDefaultBG(index);
                }
            });
        }
        
        bgList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                frameBGs.setPrefWidth(getWidth);
            }
        });
        
        frameBGs.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                defBGPane.setVisible(false);
                frameImgBox.setVisible(true);
                displayFrameImages();
            }
        });
        
        // Link all label buttons to the width of the bgList
        bgList.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                double getWidth = newWidth.doubleValue();
                for (Label label : allDefaultLabels) {
                    label.setPrefWidth(getWidth);
                }
            }
        });
    }
    
    // Adds functionality to a label so it displays the related bg image on the right
    private void displayDefaultBG(int labelIndex) {
        defBGPane.setVisible(true);
        frameImgBox.setVisible(false);
        curLabelIndex = labelIndex;
        bgImageView.setImage(imgConverter(wip.bgs.get(labelIndex)));
    }
    
    // Adds an image to the wip, displays it as the top image and recreates the scrollable list
    private void addImage(BufferedImage img) {
        wip.bgs.add(img);
        curFrameIndex = wip.bgs.size() - 1;
        displayFrameImages();
    }
    
    private void displayFrameImages() {
        // Update the curBG displayed on top
        curFrameBG.getChildren().clear();
        Image curBG = null;
        if (curFrameIndex > 0) {
            curBG = imgConverter(wip.bgs.get(curFrameIndex));
            curFrameIV.setImage(curBG);
            
            curFrameIV.maxWidth(bgDisplayPane.getWidth());
            curFrameIV.setFitWidth(bgDisplayPane.getWidth());
            curFrameIV.maxHeight(curFrameBG.getHeight());
            curFrameIV.setFitHeight(curFrameBG.getHeight());
            curFrameIV.setPreserveRatio(true);
            
            curFrameBG.getChildren().add(curFrameIV);
        }
        
        // Clear all the scrollable list and readd each bg
        frameImgs.getChildren().clear();
        
        // Skip the default menu images
        for (int i = WIP.BGIndex.values().length; i < wip.bgs.size(); i++) {
            final int index = i;
            Image bg = imgConverter(wip.bgs.get(i));
            ImageView iv = new ImageView(bg);
            // Make a border / box around each imageview so we can see individual imgs
            VBox box = new VBox();
            box.getChildren().add(iv);
            
            Color borderColor = curFrameIndex == i ? Color.RED : Color.BLACK;
            box.setBorder(new Border(new BorderStroke(borderColor, 
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            
            iv.fitHeightProperty().bind(frameImgBox.heightProperty().add(
                    -(frameImgBox.heightProperty().doubleValue() * .8)));
            iv.setPreserveRatio(true);
            
            // If the imageviews are clicked, switch over to displaying that image
            iv.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    curFrameIndex = index;
                    displayFrameImages();
                }
            });
            
            frameImgs.getChildren().add(box);
        }
    }
    
    // Opens a file chooser which sets the selected image to be the currently selected default menu's bg
    @FXML
    private void changeDefImg() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {            
            // Set new default image
            try {
                BufferedImage newBG = ImageIO.read(file);
                wip.bgs.set(curLabelIndex, newBG);
                displayDefaultBG(curLabelIndex);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
    
    @FXML
    private void addFrameBG() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {            
            try {
                BufferedImage newBG = ImageIO.read(file);
                wip.bgs.add(newBG);
                curFrameIndex = wip.bgs.size() - 1;
                displayFrameImages();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
    
    @FXML
    private void deleteCurBG() {
        // Show a dialog asking if the player really wants to delete the stat
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Delete Background");
        confirm.setHeaderText(null);
        confirm.setContentText("You are about to delete the current background. Is this okay?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK){
            wip.bgs.remove(curFrameIndex);
            
            for (Frame frame : wip.frames) {
                // Nullify any Frame BGs that were using the deleted BG
                if (frame.getBG() == curFrameIndex) {
                    frame.setBG(-1);
                }
                // Shift all the bgs by -1 since we removed one
                else if (frame.getBG() > curFrameIndex) {
                    frame.setBG(frame.getBG() - 1);
                }
            }
            
            curFrameIndex--;
            int numMenus = WIP.BGIndex.values().length;
            // If the first picture gets deleted
            if (curFrameIndex < numMenus) {
                // Set the cur displayed bg to be the new first picture
                if (wip.bgs.size() > numMenus)
                    curFrameIndex = numMenus;
                // Or 'null'
                else
                    curFrameIndex = -1;
            }
            
            displayFrameImages();
        }
    }
    
    // Converts a regular Java Image to a JavaFX Image
    private Image imgConverter(java.awt.Image img) {
        BufferedImage bi = (BufferedImage)img;
        
        WritableImage wr = null;
        if (img != null) {
            wr = new WritableImage(bi.getWidth(), bi.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    pw.setArgb(x, y, bi.getRGB(x, y));
                }
            }
        }
        
        return wr;
    }
}
