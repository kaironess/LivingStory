package gameUI;

import sharedClasses.*;
import java.util.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import createdGameClasses.GameController;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import javafx.scene.layout.*;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sharedClasses.Frame;


public class UIControl implements Initializable {
    private GameController gc;
    private TextFlow gameTitle;
    private ImageView bgView;

    @FXML
    private Pane gamePane;
    @FXML
    private BorderPane mainPane, pausePane, settingsPane, galleryPane;
    
    private List<Pane> allMainPanes;
    private Pane prevPane; // last pane that was displayed before the current one
    
    @FXML
    private StackPane displayPane;
    
    @FXML
    private AnchorPane basePane; //, audioWindowBasePane;
    
    @FXML // Game Menu
    private Button nextButton;
    @FXML // Main Menu
    private Button startButton, settingsButton, quitButton;
    @FXML // Pause Menu
    private Button pauseSettingsButton, pauseQuitButton;
    @FXML // Settings Menu
    private Button settingsQuitButton;
    @FXML // Gallery Menu
    private Button galleryQuitButton;
    
    @FXML
    private VBox mainMenuButtons;

//    @FXML
//    private GridPane audioGrid;
//
//    @FXML
//    private MenuItem audioSetButton, aboutButton, displaySetButton, saveGameButton, quitGameButton, 
//     closeWindowButton;
//
//    @FXML
//    private Button closeAudioButton;
//    
//    @FXML
//    private Label audioTitle, sfxVol, musicVol;
//    
//    @FXML
//    private Slider volumeSlider, sfxvolumeSlider;
//    

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // Testing: Load in a Game
        try {
            FileInputStream fis = new FileInputStream("testGame.save");
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            this.gc = (GameController)ois.readObject();
            this.gc.setup();
            displayCurFrame();
            
            ois.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        allMainPanes = new ArrayList<Pane>();
        allMainPanes.add(gamePane);
        allMainPanes.add(mainPane);
        allMainPanes.add(pausePane);
        allMainPanes.add(settingsPane);
        allMainPanes.add(galleryPane);
        
        setupMainMenu();
        setupPauseMenu();
        setupSettingsMenu();
        displayPane(mainPane); // Start at the main menu
        
        // Handle window resizing events
        basePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldHeight,
             Number newHeight) {
                for (Pane pane : allMainPanes) 
                    pane.setPrefHeight(newHeight.doubleValue());
            }
        });
        basePane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                for (Pane pane : allMainPanes) 
                    pane.setPrefWidth(newWidth.doubleValue());
            }
        });
        
        // Handle next frame event
        nextButton.setAlignment(Pos.TOP_RIGHT);
        nextButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER) || 
                    event.getCode().equals(KeyCode.SPACE)) {
                    System.out.println("ENTER PRESSED! NEXT FRAME TIME!");
                    // Change current frame
                    /** changeFrame();
                    displayCurFrame();
                    */
                }
                else if (event.getCode().equals(KeyCode.ESCAPE)) {
                    displayPane(pausePane);
                }
            }
        });
        
    }
    
    private void setupMainMenu() {
        // Add the game's title to the mainPane
        gameTitle = new TextFlow(new Text("Game Title"));
        gameTitle.setTextAlignment(TextAlignment.CENTER);
        mainPane.getChildren().add(gameTitle);
        mainPane.setBorder(new Border(new BorderStroke(Color.BLACK, 
         BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        
        // Switch over to the start of the game
        startButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(gamePane);
            }
         });
        
        // Open the settings menu
        settingsButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(settingsPane);
            }
         });
        
        //Quit the game and close the program
        quitButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage) basePane.getScene().getWindow()).close();
            }
         });
    }
    
    private void setupPauseMenu() {
         // Open the settings menu
        pauseSettingsButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(settingsPane);
            }
         });
        
        //Quit the game and close the program
        pauseQuitButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(mainPane);
            }
         });
    }
    
    private void setupSettingsMenu() {
        settingsQuitButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returnToPrevPane();
            }
         });
    }
    
    private void setupMainPanes() {
        for (Pane pane : allMainPanes) 
            pane.managedProperty().bind(pane.visibleProperty());
    }
    
    // Displays the given Pane and sets all other Panes to be not visible
    private void displayPane(Pane displayPane) {
        // Save the previous pane
        for (Pane pane : allMainPanes) 
            if (pane.isVisible()) {
                prevPane = pane;
                break;
            }
                
        // Switch to the requested pane 
        for (Pane pane : allMainPanes) 
            pane.setVisible(pane.equals(displayPane));
    }
    
    // Displays the previous pane, and saves the current pane as the new "prevPane"
    private void returnToPrevPane() {
        Pane nextFrame = prevPane;
        displayPane(nextFrame);
    }
    
    private void changeFrame() {
        this.gc.nextFrame();
        displayCurFrame();
    }
    
    private void displayCurFrame() {
        // Display the frame's background picture
        Frame curFrame = this.gc.getCurFrame();
        Image bg = imgConverter(this.gc.getCurBG());
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.gamePane.widthProperty());
        bgView.fitHeightProperty().bind(this.gamePane.heightProperty());
        this.gamePane.getChildren().add(0, bgView);
        
//        Image bg = new Image("https://s-media-cache-ak0.pinimg.com/originals/92/15/a2/9215a21cb4be4b2a92e981c87da88331.jpg");
//        BackgroundImage img = new BackgroundImage((Image)bg, BackgroundRepeat.NO_REPEAT, 
//                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//            this.displayPane.setBackground(new Background(img));
//        
//        // Display characters present in the current frame
//        /** ArrayList<DisplayChar> frameChars = curFrame.getChars();
//        for (DisplayChar c : frameChars) {
//            Image currImg = imgConverter(c.getCharImg());
//            ImageView currImgView = new ImageView();
//            currImgView.setImg(currImg);
//            someChar.setPreserveRatio(true);
//            someChar.setCache(true);
//            someChar.setSmooth(true);
//            someChar.setTranslateX(c.getLeftMargin());
//            someChar.setTranslateY(125);            
//            this.displayPane.getChildren().addAll(someChar);
//        }
//        */
//        Image cloud = new Image("http://s33.postimg.org/xqnceoz67/reffence.png", 400, 300, false, false);
//        ImageView someChar = new ImageView();
//        someChar.setImage(cloud);
//        someChar.setPreserveRatio(true);
//        someChar.setSmooth(true);
//        someChar.setCache(true);
//        someChar.setTranslateX(275.0);
//        someChar.setTranslateY(125);
//        this.displayPane.getChildren().addAll(someChar);
//        
//        // Display dialog box and dialog text
//        /** Label dialogLabel = new Label(curFrame.getDialog());
//         */
//        Label dialogLabel = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque efficitur augue nec porta ullamcorper. Ut cursus vestibulum blandit. Proin mollis ut est bibendum laoreet. Ut sagittis ante id ante sollicitudin, bibendum laoreet ligula elementum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam vulputate libero diam, eu tincidunt mi placerat quis. Curabitur consequat eget orci sit amet ullamcorper. Nam rhoncus, mauris sit amet mattis consequat, ipsum lectus consequat augue, quis sodales tellus ligula et elit. Aenean euismod sollicitudin congue. Maecenas interdum ac ante quis venenatis. Nunc faucibus, urna sed condimentum condimentum, quam massa porttitor neque, ac congue mauris diam eleifend purus. Vestibulum elementum felis vitae auctor cursus. Mauris mattis dapibus ipsum, id gravida elit rhoncus hendrerit. Fusce lobortis tellus lacus, sit amet porttitor velit feugiat non. Pellentesque tincidunt eleifend tellus ut condimentum.");
//        dialogLabel.setMaxWidth(this.basePane.getPrefWidth() - 40);
//        dialogLabel.setPadding(new Insets(10, 10, 10, 10));
//        dialogLabel.setWrapText(true);
//        dialogLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
//        dialogLabel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, null)));
//        this.displayPane.setAlignment(dialogLabel, Pos.BOTTOM_CENTER);   
//        this.displayPane.getChildren().addAll(dialogLabel);
        
    }
    
    @FXML
    private void saveGameState() {
        System.out.println("SAVE CURR FRAME / GAME STATE");
        this.gc.createSave();
    }

    @FXML
    private void exitGame() {
        System.out.println("EXIT GAME? BACK TO MAIN MENU?");
    }
    
    @FXML
    private void openAudioDialog() {            
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AudioWindow.fxml"));
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
    private void openDisplayDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DisplayWindow.fxml"));
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
    private void openAboutDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutWindow.fxml"));
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
    private void setAudioVolume() {
//        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (! volumeSlider.isValueChanging()) {
//                System.out.println("Vol Slider Value Changed (newValue: " + newValue.intValue() + ")");
//            }
//        });
    }
    
    @FXML
    private void setSFXVolume() {
//        sfxvolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (! sfxvolumeSlider.isValueChanging()) {
//                System.out.println("SFX Slider Value Changed (newValue: " + newValue.intValue() + ")");
//            }
//        });
    }
    
    @FXML
    private void closeAudioWindow() {
//        Stage stage = (Stage) closeAudioButton.getScene().getWindow();
//        stage.close();
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
