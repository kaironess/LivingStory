package gameUI;

import sharedClasses.*;
import java.util.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import createdGameClasses.GameController;
import createdGameClasses.GameController.BGIndex;
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
    private double getHeight;
    private double getWidth;

    @FXML
    private AnchorPane gamePane;
    
    @FXML
    private BorderPane mainPane, pausePane, savePane, loadPane, settingsPane, galleryPane;
    
    private List<Pane> allMainPanes;
    private Pane prevPane; // last pane that was displayed before the current one
    
    @FXML
    private StackPane displayPane;
    
    @FXML
    private AnchorPane basePane; //, audioWindowBasePane;
    
    @FXML // Game Menu
    private Button nextButton;
    @FXML // Main Menu
    private Button startButton, loadButton, settingsButton, galleryButton, quitButton;
    @FXML // Pause Menu
    private Button pauseSaveButton, pauseLoadButton, pauseSettingsButton, pauseMainMenuButton,
     pauseGalleryButton, pauseReturnButton;
    @FXML // Settings Menu
    private Button settingsReturnButton;
    @FXML // Gallery Menu
    private Button galleryReturnButton;
    @FXML // Save Menu
    private Button saveButton, saveReturnButton;
    @FXML // Load Menu
    private Button loadLoadButton, loadReturnButton;
    
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
        allMainPanes.add(savePane);
        allMainPanes.add(loadPane);
        allMainPanes.add(settingsPane);
        allMainPanes.add(galleryPane);
        
        setupMainMenu();
        setupPauseMenu();
        setupSettingsMenu();
        setupGalleryMenu();
        setupSaveMenu();
        setupLoadMenu();
        displayPane(mainPane); // Start at the main menu
        
        // Handle window resizing events
        basePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldHeight,
             Number newHeight) {
                getHeight = newHeight.doubleValue();
                for (Pane pane : allMainPanes) 
                    pane.setPrefHeight(newHeight.doubleValue());
            }
        });
        basePane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth,
             Number newWidth) {
                getWidth = newWidth.doubleValue();
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
                    changeFrame();
                    
                }
                else if (event.getCode().equals(KeyCode.ESCAPE)) {
                    displayPane(pausePane);
                }
            }
        });
        
    }
    
    private void setupMainMenu() {
        // Display the main menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.MAIN_MENU));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.mainPane.widthProperty());
        bgView.fitHeightProperty().bind(this.mainPane.heightProperty());
        this.mainPane.getChildren().add(0, bgView);
        
        // Switch over to the start of the game
        startButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(gamePane);
            }
         });
        
        loadButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(loadPane);
            }
         });
        
        // Open the settings menu
        settingsButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(settingsPane);
            }
         });
        
        // Open the gallery menu
        galleryButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(galleryPane);
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
        // Display the pause menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.PAUSE_MENU));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.pausePane.widthProperty());
        bgView.fitHeightProperty().bind(this.pausePane.heightProperty());
        this.pausePane.getChildren().add(0, bgView);
        
        // Open the settings menu
        pauseSettingsButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(settingsPane);
            }
         });
        
        pauseSaveButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(savePane);
            }
         });
        
        pauseLoadButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(loadPane);
            }
         });
        
        // Open the gallery menu
        pauseGalleryButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(galleryPane);
            }
         });
        
        // Open the main menu
        pauseMainMenuButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(mainPane);
            }
         });
        
        // Return to the game 
        pauseReturnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(gamePane);
            }
         });
    }
    
    private void setupSaveMenu() {
        // Display the save menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.SAVE));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.savePane.widthProperty());
        bgView.fitHeightProperty().bind(this.savePane.heightProperty());
        this.savePane.getChildren().add(0, bgView);
        
        saveButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.createSave();
            }
         });
        
        saveReturnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayPane(pausePane);
            }
         });
    }
    
    private void setupLoadMenu() {
        // Display the save menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.LOAD));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.loadPane.widthProperty());
        bgView.fitHeightProperty().bind(this.loadPane.heightProperty());
        this.loadPane.getChildren().add(0, bgView);
        
        loadLoadButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //gc.loadSave("");
                displayPane(gamePane);
            }
         });
        
        loadReturnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returnToPrevPane();
            }
         });
    }
    
    private void setupSettingsMenu() {
        // Display the setting menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.SETTINGS));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.settingsPane.widthProperty());
        bgView.fitHeightProperty().bind(this.settingsPane.heightProperty());
        this.settingsPane.getChildren().add(0, bgView);
        
        settingsReturnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returnToPrevPane();
            }
         });
    }
    
    private void setupGalleryMenu() {
        // Display the gallery menu's bg
        Image bg = imgConverter(this.gc.getBG(BGIndex.GALLERY));
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.galleryPane.widthProperty());
        bgView.fitHeightProperty().bind(this.galleryPane.heightProperty());
        this.galleryPane.getChildren().add(0, bgView);
        
        galleryReturnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returnToPrevPane();
            }
         });
    }
    
    private void setupMainPanes() {
        for (Pane pane : allMainPanes)  {
            pane.managedProperty().bind(pane.visibleProperty());
        }
    }
    
    // Displays the given Pane and sets all other Panes to be not visible
    private void displayPane(Pane displayPane) {
        // Reset the game if we're returning to the main menu
        if (displayPane.equals(mainPane)) 
            this.gc.reset();
        // Display the current frame if we're going to the game pane
        else if (displayPane.equals(gamePane))
            displayCurFrame();
        
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
        this.gamePane.getChildren().removeAll();
        
        // Display the frame's background picture
        Frame curFrame = this.gc.getCurFrame();
        Image bg = imgConverter(this.gc.getCurBG());
        bgView = new ImageView(bg);
        bgView.fitWidthProperty().bind(this.gamePane.widthProperty());
        bgView.fitHeightProperty().bind(this.gamePane.heightProperty());
        this.gamePane.getChildren().add(bgView);
       
        // Display characters present in the current frame
        ArrayList<DisplayChar> frameChars = curFrame.getChars();
        for (DisplayChar c : frameChars) {
            Image currImg = imgConverter(c.getCharImg());
            ImageView currChar = new ImageView();
            currChar.setImage(currImg);
            currChar.setPreserveRatio(true);
            currChar.setCache(true);
            currChar.setSmooth(true);
            currChar.setTranslateX(c.getLeftMargin());
            currChar.setTranslateY(125);            
            this.gamePane.getChildren().addAll(currChar);
        }
        
        // Display dialog box and dialog text
        Label dialogLabel = new Label(curFrame.getDialog());
        dialogLabel.setMinHeight(this.gamePane.getPrefWidth() / 8);
        dialogLabel.setPadding(new Insets(10, 10, 10, 10));
        dialogLabel.setWrapText(true);
        dialogLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        dialogLabel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, null)));
        this.gamePane.setLeftAnchor(dialogLabel, 10.0);
        this.gamePane.setRightAnchor(dialogLabel, 10.0);
        this.gamePane.setBottomAnchor(dialogLabel, 10.0);
        this.gamePane.getChildren().addAll(dialogLabel);
        
        // Display dialog options
        // Vbox with dialog options? How do we want it to look?
        VBox dialogChoices = new VBox();
        dialogChoices.setPadding(new Insets(10, 10, 10, 10));
        dialogChoices.setSpacing(10);
        dialogChoices.setAlignment(Pos.BOTTOM_CENTER);
        this.gamePane.setLeftAnchor(dialogChoices, 10.0);
        this.gamePane.setRightAnchor(dialogChoices, 10.0);
        this.gamePane.setBottomAnchor(dialogChoices, getHeight / 2);
        
        ArrayList<Decision> decisions = curFrame.getDialogOptions();
        for (Decision d : decisions) {
            Button btn = new Button();
            btn.setText(d.getDialog());
            btn.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Do something??
                    // Will the gc be able to tell a dialog has been chosen
                }
             });
            dialogChoices.getChildren().addAll(btn);
        }
        this.gamePane.getChildren().addAll(dialogChoices);
        
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
