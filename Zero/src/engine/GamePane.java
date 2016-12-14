/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import javafx.scene.layout.Pane;
import song.Song;

/**
 *
 * @author Owner
 */
public class GamePane extends Pane {
    
    public GamePane(Song song, Player player) {
        
    }
    
    public void startGame(){
        Note note1 = new Note(0.0, 0, 270, "assets/noteskins/arrow.png");
        Note note2 = new Note(100, 0, 180, "assets/noteskins/arrow.png");
        Note note3 = new Note(200, 0, 0, "assets/noteskins/arrow.png");
        Note note4 = new Note(300, 0, 90, "assets/noteskins/arrow.png");
        
        getChildren().add(note1);
        getChildren().add(note2);
        getChildren().add(note3);
        getChildren().add(note4);
    }
}