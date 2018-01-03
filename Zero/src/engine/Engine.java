package engine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller class for the project.
 */
public class Engine extends Application {
    Player currentPlayer;
    Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        
        // initialize all the player options and properties
        Player me = new Player();
        me.bindings[0] = KeyCode.E;
        me.bindings[1] = KeyCode.F;
        me.bindings[2] = KeyCode.K;
        me.bindings[3] = KeyCode.O;
        me.trackLength = 400;
        me.scrollDirection = 270;
        me.name = "Trumpet63";
        me.noteSize = 40;
        me.windowHeight = me.trackLength - me.noteSize;
        me.noteSkin = "assets/noteskins/arrow.png";
        me.receptorLocation = 0.20;
        me.scrollSpeed = 350;
        me.trackSpacing = 20;
        me.windowWidth = 4 * me.noteSize + (4 - 1) * me.trackSpacing;
        me.windowPadding = 20;
        me.accuracies = new AccuracyRank[9]; // match FFR accuracies
        me.accuracies[0] = new AccuracyRank(-0.117, "Miss", -10, Color.RED);
        me.accuracies[1] = new AccuracyRank(-0.083, "Average", 5, Color.GOLD);
        me.accuracies[2] = new AccuracyRank(-0.050, "Good", 25, Color.YELLOWGREEN);
        me.accuracies[3] = new AccuracyRank(-0.017, "Perfect", 50, Color.DARKGREEN);
        me.accuracies[4] = new AccuracyRank(0.017, "Amazing", 50, Color.BLUEVIOLET);
        me.accuracies[5] = new AccuracyRank(0.050, "Perfect", 50, Color.DARKGREEN);
        me.accuracies[6] = new AccuracyRank(0.083, "Good", 25, Color.YELLOWGREEN);
        me.accuracies[7] = new AccuracyRank(0.117, "Average", 5, Color.GOLD);
        me.accuracies[8] = new AccuracyRank(0.117, "Boo", -5, Color.DARKRED);
        me.noteOffset = -0.1;
        me.judgeOffset = 0.0;
        currentPlayer = me;

        File file = new File("./assets/levels/."); // returns a file for the current working directory
        
        VBox vb = new VBox(); // container to use with scroll pane
        ScrollPane sp = new ScrollPane(); // create the start menu's pane
        VBox box = new VBox(); // box containing the scene
        Scene startMenu = new Scene(box, 300, 250); // create a scene for the start menu
        primaryStage.setScene(startMenu); // set the window's scene to the start menu
        primaryStage.setTitle("Project Zero"); // give the window a title
        box.getChildren().add(sp);
        VBox.setVgrow(sp, Priority.ALWAYS);
        
        fileButton(file.getAbsoluteFile().getParentFile(), vb, 0);
        sp.setContent(vb);
        
        primaryStage.show(); // show the window
    }

    /**
     * Method included to allow the program to be launched from a different
     * context.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void switchToPlay(File[] files, int mode) {
        // create the play-pane
        PlayPane playPane = new PlayPane(files, mode);
        
        // create the main scene with dimensions specified by player
        Scene playScene = new Scene(playPane, currentPlayer.windowWidth + 2 * currentPlayer.windowPadding, currentPlayer.windowHeight);
        
        playPane.addGame(currentPlayer); // add a game to the play-pane and assign a player to it
        stage.setScene(playScene); // switch the scene to the play scene
        playPane.start(); // start the play-pane, which starts the game(s)
    }
    
    public void fileButton(File file, VBox box, int depth) {
        box.getChildren().clear();
        Button up = new Button(); // button to go up one level (parent folder)
        up.setText("...");
        up.setOnAction((ActionEvent event) -> {
            fileButton(file.getParentFile(), box, depth - 1);
        } // on-click
        );
        if(depth > 0) { // do not travel futher up than the levels folder
            box.getChildren().add(up);
        }
        for(File item: file.listFiles()) {
            Button button = new Button();
            String name = item.getName();
            if(item.isDirectory()) {
                File[] files = isSong(item);
                if(files[0] != null && files[1] != null) {
                    String text = name + " (Modes:";
                    try {
                        song.Song song = new song.Song(files[0].getCanonicalPath(), files[1].getCanonicalPath());
                        song.getInfo();
                        for(int i = 0; i < song.modes.data.length; i++) {
                            song.Mode mode = song.modes.data[i];
                            text += " ";
                            if(mode.difficulty.equals("")) {
                                text += i;
                            }
                            else {
                                text += mode.difficulty;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    text += ")";
                    button.setText(text);
                    box.getChildren().add(button); // add button if file is song
                }
                else {
                    if(containsSong(item)) {
                        button.setText(name + "(contains song)");
                        box.getChildren().add(button); // or, add button if file contains song
                    }
                    else {
                        button.setText(name);
                    }
                }
                button.setOnAction((ActionEvent event) -> {
                    if(item.isDirectory()){
                        File[] item_files = isSong(item);
                        if(item_files[0] == null || item_files[1] == null) { // item is not a song
                            fileButton(item, box, depth + 1);
                        }
                        else { // item is a song
                            songButton(item, box, depth + 1, item_files);
                        }
                    }
                } // on-click
                );
            }
        }
    }
    
    private void songButton(File file, VBox box, int depth, File[] songFiles) {
        box.getChildren().clear();
        Button up = new Button(); // button to go up one level (parent folder)
        up.setText("...");
        up.setOnAction((ActionEvent event) -> {
            fileButton(file.getParentFile(), box, depth - 1);
        } // on-click
        );
        box.getChildren().add(up);
        
        try {
            song.Song song = new song.Song(songFiles[0].getCanonicalPath(), songFiles[1].getCanonicalPath());
            song.getInfo();
            for(int i = 0; i < song.modes.data.length; i++) {
                int modeNum = i;
                Button button = new Button();
                song.Mode mode = song.modes.data[i];
                String text = "";
                if(mode.difficulty.equals("")) {
                    text += i;
                }
                else {
                    text += mode.difficulty;
                }
                button.setText(text);
                button.setOnAction((ActionEvent event) -> {
                    switchToPlay(songFiles, modeNum);
                } // on-click
                );
                box.getChildren().add(button); // add button if file is song
            }
        } catch (IOException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private File[] isSong(File file) {
        File[] contents = file.listFiles();
        File[] files = new File[2];
        boolean foundSM = false;
        boolean foundMP3 = false;
        for(File item: contents) {
            if(foundSM && foundMP3){
                break;
            }
            String extension = "";
            String fileName = item.getName();
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i+1);
            }
            if(!foundSM) {
                if(extension.toLowerCase().equals("sm")) {
                    foundSM = true;
                    files[0] = item;
                }
            }
            if(!foundMP3) {
                if(extension.toLowerCase().equals("mp3")) {
                    foundMP3 = true;
                    files[1] = item;
                }
            }
        }
        return files;
    }
    
    private boolean containsSong(File file) {
        if(file.isDirectory()) {
            File[] files = isSong(file);
            if(files[0] != null && files[1] != null) {
                return true;
            }
            for(File item: file.listFiles()) {
                if(containsSong(item)){
                    return true;
                }
            }
        }
        return false;
    }
}