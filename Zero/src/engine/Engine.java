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
 *
 * @author Trumpet63
 */
public class Engine extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Song song = new Song("assets\\levels\\lotd\\09_-_lord_of_the_dance.sm");
        // song.parse();
//        for(int i = 0; i < song.modes.data[song.currentMode].tracks.numElements; i++) {
//            System.out.println("\nTrack " + i + ":");
//            System.out.println("time       measurePosition  measureSize  columnNumber  bpm");
//            for(int j = 0; j < song.modes.data[song.currentMode].tracks.data[i].numElements; j++) {
//                System.out.println(song.modes.data[song.currentMode].tracks.data[i].data[j].toString());
//            }
//        }
        PlayPane root = new PlayPane("assets\\levels\\lotd\\09_-_lord_of_the_dance.sm", "assets/levels/level_1/level_1.mp3");
        Scene playScene = new Scene(root, 450, 350);
        Player me = new Player();
        me.bindings[0] = KeyCode.E;
        me.bindings[1] = KeyCode.F;
        me.bindings[2] = KeyCode.J;
        me.bindings[3] = KeyCode.I;
        
        Button btn = new Button();
        btn.setText("Start Game");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.addGame(me);
                primaryStage.setScene(playScene);
                root.start();
            }
        });
        
        StackPane startPane = new StackPane();
        startPane.getChildren().add(btn);
        
        Scene startMenu = new Scene(startPane, 300, 250);
        
        primaryStage.setTitle("FFR Zero");
        primaryStage.setScene(startMenu);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}