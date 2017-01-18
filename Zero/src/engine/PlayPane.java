package engine;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaPlayer;
import song.Song;

/**
 * Main host pane for gameplay that controls each of its games.
 */
public class PlayPane extends FlowPane {
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
                        game.player.windowWidth, 
                        game.player.windowHeight);
            }
        }
        
        this.requestFocus(); // get keyboard focus
        this.setOnKeyPressed((event) -> { // create key event handler
            handleInput(event.getCode());
        });
        
        // start all the games
        for(Node nodeIn:this.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                ((GamePane)nodeIn).startGame(); // start this game
            }
        }
        
        song.music.setVolume(0.1); // set the music volume
        
        double endTime = song.music.getTotalDuration().toSeconds(); // the maximum length of time the games will last
        
        // create an update loop that will run every frame
        DrawController updateGames = new DrawController(getAllTracks(this));
        
        while(song.music.getStatus() != MediaPlayer.Status.READY) {} // wait unti the music is ready
        song.music.play(); // play the music
        updateGames.initializeStartTime(((double)System.nanoTime())/1000000000);
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
                for(int i = 0; i < game.tracks.length && i < game.player.bindings.length; i++) { // for each track
                    if(game.player.bindings[i] == key) { // if the track is bound to the pressed key
                        game.tracks[i].handleInput(); // activate this track
                    }
                }
            }
        }
    }
    
    private NoteTrack[] getAllTracks(PlayPane playPane) {
        NoteTrack[] allTracks;
        int trackCount = 0;
        int trackNumber = 0;
        for(Node nodeIn: playPane.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                GamePane game = ((GamePane)nodeIn);
                trackCount += game.tracks.length;
            }
        }
        
        allTracks = new NoteTrack[trackCount];
        
        for(Node nodeIn: playPane.getChildren()) { // for each game
            if(nodeIn instanceof GamePane) {
                GamePane game = ((GamePane)nodeIn);
                for(NoteTrack track: game.tracks) { // for each track
                    allTracks[trackNumber++] = track;
                }
            }
        }
        
        return allTracks;
    }
}