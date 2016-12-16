package engine;

import javafx.scene.layout.Pane;
import song.Song;
import song.NoteArray;

/**
 * Pane that manages a group of tracks.
 */
public class GamePane extends Pane {
    Song song;
    Player player;
    NoteTrack[] tracks;
    
    // x and y position of upper left corner
    double x;
    double y;
    
    double width;
    double height;
    
    /**
     * Create an instance of a game-pane object.
     * @param song The song used to run this game.
     * @param player The player assigned to this game.
     */
    public GamePane(Song song, Player player) {
        this.song = song;
        this.player = player;
    }
    
    /**
     * Initialize the game pane and the tracks to the pane.
     * @param x The x-coordinate of this game's position.
     * @param y The y-coordinate of this game's position.
     * @param width The width of the screen space allotted for this game.
     * @param height The height of the screen space allotted for this game.
     */
    public void initialize(double x, double y, double width, double height) {
        int numTracks = song.modes.data[song.currentMode].tracks.numElements;
        tracks = new NoteTrack[numTracks];
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // create tracks for each of the tracks of notes that were parsed from the .sm file
        for(int trackNumber = 0; trackNumber < numTracks; trackNumber ++) { // for each track
            NoteArray parsedNotes = song.modes.data[song.currentMode].tracks.data[trackNumber]; // get the parsed notes from the song
            int numNotes = parsedNotes.numElements;
            Note[] trackNotes = new Note[numNotes]; // set up a new array of engine.Note's
            
            for(int noteNumber = 0; noteNumber < numNotes; noteNumber++) { // for each note
                trackNotes[noteNumber] = new Note(parsedNotes.data[noteNumber].time); // store the data for this note
            }
            
            tracks[trackNumber] = new NoteTrack( // create a new note track and add it to the array of tracks
                    trackNotes,
                    player,
                    trackNumber, // so the track can tell which keybinding applies to it
                    getTrackX((double)trackNumber/numTracks),
                    y + height/2
                    );
            
            this.getChildren().add(tracks[trackNumber]); // display the note track (but there's nothing to display right now)
        } // end for (each track)
    } // end initialize
    
    /**
     * Sets the x-coordinate of this game's position.
     * @param x The new x-coordinate of this game's position.
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * Sets the y-coordinate of this game's position.
     * @param y The new x-coordinate of this game's position.
     */
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * Calculates the x-coordinate for a track's start position.
     * @param positionPercent The ratio between the track number (starts at
     * zero) and the total number of tracks.
     * @return The x-coordinate of the track's start position.
     */
    private long getTrackX(double positionPercent) {
        return Math.round(x + width*(positionPercent + 0.07));
    }
    
    /**
     * Initialize each of this games' tracks.
     */
    public void startGame(){
        for(NoteTrack track: tracks) {
            track.startGame();
        }
    }
    
    /**
     * Update each of this games' tracks to the current time.
     * @param currentTime The current time position within the song.
     */
    public void updateTo(double currentTime) {
        for(NoteTrack track: tracks) {
            track.updateTo(currentTime);
        }
    }
}