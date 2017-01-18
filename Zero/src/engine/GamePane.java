package engine;

import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import song.Song;
import song.NoteArray;

/**
 * Pane that manages a group of tracks.
 */
public class GamePane extends Pane {
    Song song;
    Player player;
    NoteTrack[] tracks;
    
    Text accuracyPopupText;
    AccuracyPopup accuracyPopup;
    
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
     * Initialize the game pane and the tracks belonging to the pane.
     * @param width The width of the screen space allotted for this game.
     * @param height The height of the screen space allotted for this game.
     */
    public void initialize(double width, double height) {
        int numTracks = song.modes.data[song.currentMode].tracks.numElements;
        tracks = new NoteTrack[numTracks];
        setWidth(width);
        setHeight(height);
        
        accuracyPopupText = new Text(-0.1*width + player.windowPadding, height/5, ""); // position the popup text
        accuracyPopupText.setWrappingWidth(1.2*width); // ensure the text wrapping width spans the width of the screen
        accuracyPopupText.setFont(Font.font("Verdana", FontWeight.BOLD, width/5)); // set the popup font
        accuracyPopupText.setTextAlignment(TextAlignment.CENTER); // center the popup text
        getChildren().add(accuracyPopupText); // add the popup to the pane
        accuracyPopup = new AccuracyPopup(accuracyPopupText, 0.50); // create the animation that will manage the popup text
        accuracyPopup.setInterpolator(Interpolator.EASE_OUT); // give the animation an "easing" behavior
        
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
                    numTracks,
                    getTrackX(trackNumber),
                    height + player.noteSize / 2,
                    this
                    );
            
            this.getChildren().add(tracks[trackNumber]); // display the note track (but there's nothing to display right now)
        } // end for (each track)
    } // end initialize
    
    /**
     * Calculates the x-coordinate for a track's start position.
     * @param positionPercent The ratio between the track number (starts at
     * zero) and the total number of tracks.
     * @return The x-coordinate of the track's start position.
     */
    private double getTrackX(int trackNumber) {
        double trackX = player.noteSize / 2;
        trackX += trackNumber * (player.trackSpacing + player.noteSize);
        return trackX + player.windowPadding;
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
    
    public void causeAccuracyPopup(int accuracyRank, int trackNumber) {
        accuracyPopup.initialize(player.accuracies[accuracyRank].textColor, player.accuracies[accuracyRank].name);
        accuracyPopup.play();
    }
}