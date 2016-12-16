package engine;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
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
        me.windowWidth = 450;
        me.trackLength = 200;
        me.windowHeight = 2*me.trackLength;
        me.scrollDirection = 270;
        me.name = "Trumpet63";
        me.noteSize = 1;
        me.noteSkin = "assets/noteskins/arrow.png";
        me.receptorLocation = .20;
        me.scrollSpeed = 80;
        me.trackSpacing = .25;
        me.trackWidth = .90;
        
        // create the play-pane
        PlayPane playPane = new PlayPane("assets\\levels\\lotd\\09_-_lord_of_the_dance.sm", "assets/levels/level_1/level_1.mp3");
        
        // create the main scene with dimensions specified by player
        Scene playScene = new Scene(playPane, 1.1*me.windowWidth, 1.1*me.windowHeight);
        
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