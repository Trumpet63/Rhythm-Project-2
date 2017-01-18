package engine;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller class for the project.
 */
public class Engine extends Application {
    
    @Override
    public void start(Stage primaryStage) {
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
        me.accuracies = new AccuracyRank[9];
        me.accuracies[0] = new AccuracyRank(-0.117, "Miss", -10, Color.RED);
        me.accuracies[1] = new AccuracyRank(-0.083, "Average", 5, Color.GOLD);
        me.accuracies[2] = new AccuracyRank(-0.050, "Good", 25, Color.YELLOWGREEN);
        me.accuracies[3] = new AccuracyRank(-0.017, "Perfect", 50, Color.DARKGREEN);
        me.accuracies[4] = new AccuracyRank(0.017, "Amazing", 50, Color.BLUEVIOLET);
        me.accuracies[5] = new AccuracyRank(0.050, "Perfect", 50, Color.DARKGREEN);
        me.accuracies[6] = new AccuracyRank(0.083, "Good", 25, Color.YELLOWGREEN);
        me.accuracies[7] = new AccuracyRank(0.117, "Average", 5, Color.GOLD);
        me.accuracies[8] = new AccuracyRank(0.117, "Boo", -5, Color.DARKRED);
        me.noteOffset = 0.0;
        me.judgeOffset = 0.0;
        
        // create the play-pane
        PlayPane playPane = new PlayPane("assets\\levels\\Death By Glamour\\Death By Glamour.sm", "assets/levels/Death By Glamour/Undertale OST- 068 - Death by Glamour.mp3");
        
        // create the main scene with dimensions specified by player
        Scene playScene = new Scene(playPane, me.windowWidth + 2 * me.windowPadding, me.windowHeight);
        
        // create the start button
        Button button = new Button();
        button.setText("Start Game");
        button.setOnAction(new EventHandler<ActionEvent>() { // on-click
            @Override
            public void handle(ActionEvent event) {
                playPane.addGame(me); // add a game to the play-pane and assign a player to it
                primaryStage.setScene(playScene); // switch the scene to the play scene
                playPane.start(); // start the play-pane, which starts the game(s)
            }
        });
        
        StackPane startPane = new StackPane(); // create the start menu's pane
        startPane.getChildren().add(button); // add the button to the pane
        Scene startMenu = new Scene(startPane, 300, 250); // create a scene for the start menu
        primaryStage.setTitle("Project Zero"); // give the window a title
        primaryStage.setScene(startMenu); // set the window's scene to the start menu
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
}