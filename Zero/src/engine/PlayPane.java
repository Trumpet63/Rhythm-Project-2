package engine;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import song.Song;

/**
 * 
 */
class PlayPane extends Pane {
    Song song;
    
    public PlayPane(String noteFileName, String musicFileName) {
        song = new Song(noteFileName, musicFileName);
        song.parse();
    }
    
//    public PlayPane(String songName, int modeNumber) {
//        Song song = new Song(songName, modeNumber);
//        song.parse();
//    }
    
    public GamePane addGame(Player player) {
        GamePane game = new GamePane(song, player);
        getChildren().add(game);
        
        return game;
    }
    
    public void start() {
        while(song.music.getStatus() != MediaPlayer.Status.READY) {}
        song.music.setVolume(0.1);
        song.music.play();
        for(Node nodeIn:this.getChildren()) {
            if(nodeIn instanceof GamePane) {
                ((GamePane)nodeIn).startGame();
            }
        }
    }
}