package engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import song.Song;

/**
 * Main host pane for gameplay that controls each of its games.
 */
public class PlayPane extends Pane {
    Song song;
    
    /**
     * Create an instance of a play-pane object.
     * @param noteFileName The name of the file containing the note information,
     * usually .sm.
     * @param musicFileName The name of the file containing the music/audio.
     */
    public PlayPane(String noteFileName, String musicFileName) {
        song = new Song(noteFileName, musicFileName);
        song.parse();
    }
    
//    public PlayPane(String songName, int modeNumber) {
//        Song song = new Song(songName, modeNumber);
//        song.parse();
//    }
    
    /**
     * Add a game-pane to this play-pane.
     * @param player The player to assign to the game.
     * @return The game-pane that was created.
     */
    public GamePane addGame(Player player) {
        GamePane game = new GamePane(song, player);
        this.getChildren().add(game);
        
        return game;
    }
    
    /**
     * Prepare the games being hosted and then start them.
     */
    public void start() {
        // initialize each game pane
        for(Node nodeIn:this.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                GamePane game = (GamePane) nodeIn;
                game.initialize( // initialize this game
                        Math.round((this.getScene().getWidth() - game.player.windowWidth)/2), 
                        Math.round((this.getScene().getHeight() - game.player.windowHeight)/2), 
                        game.player.windowWidth, 
                        game.player.windowHeight);
            }
        }
        
        this.requestFocus(); // get keyboard focus
        this.setOnKeyPressed((event) -> { // create key event handler
            handleInput(event.getCode());
        });
        
        song.music.setVolume(0.0); // set the music volume
        while(song.music.getStatus() != MediaPlayer.Status.READY) {} // wait unti the music is ready
        song.music.play(); // play the music
        
        // start all the games
        double startTime = ((double)System.nanoTime())/1000000000; // the system time when the game starts
        double endTime = song.music.getTotalDuration().toSeconds(); // the maximum length of time the games will last
        for(Node nodeIn:this.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                ((GamePane)nodeIn).startGame(); // start this game
            }
        }
        
        // create an update loop that will run every frame
        PlayPane thisPane = this;
        AnimationTimer updateGames = new AnimationTimer() {
            @Override
            public void handle(long now) { // now is equal to System.nanoTime()
                for(Node nodeIn: thisPane.getChildren()) { // for each game
                    if(nodeIn instanceof GamePane) {
                        ((GamePane)nodeIn).updateTo(((double)now)/1000000000 - startTime); // update this game to the current time
                    }
                }
            }
        };
        
        updateGames.start(); // start running the update loop
    }
    
    /**
     * Responds to a user input event.
     * @param key The key pressed by the user.
     */
    private void handleInput(KeyCode key) {
        for(Node nodeIn: this.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                GamePane game = ((GamePane)nodeIn);
                for(int i = 0; i < game.tracks.length; i++) { // for each track
                    if(game.player.bindings[i] == key) { // if the track is bound to the pressed key
                        game.tracks[i].handleInput(); // activate this track
                    }
                }
            }
        }
    }
}