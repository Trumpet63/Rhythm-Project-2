package engine;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

/**
 * Pane that displays and controls a group of notes and a receptor.
 */
public class NoteTrack extends Pane {
    Note[] notes;
    Player player;
    int trackNumber; // index of the track in the parent game's track array
    double defaultLength;
    Point2D defaultTrackStart;
    double defaultRotation;
    double defaultDirection;
    
    double currentLength;
    double currentDirection;
    int maxIndex; // index of the note on screen with the highest time
    int minIndex; // index of the note on screen with the lowest time
    Receptor receptor;
    Point2D currentTrackStart;
    
    /**
     * Construct a note-track object.
     * @param notes The array of notes from a particular column of the notes
     * parsed from the .sm file.
     * @param player The player belonging to this track's parent game-pane.
     * @param trackNumber The index of the track in the parent game-pane's track
     * array, also, the column number that each of the notes will have.
     * @param x The x-coordinate of the track's default starting position.
     * @param y The y-coordinate of the track's default starting position.
     */
    public NoteTrack(
            Note[] notes, 
            Player player,
            int trackNumber,
            double x,
            double y) {
        this.notes = notes;
        this.player = player;
        this.defaultLength = player.trackLength;
        this.currentLength = defaultLength;
        this.defaultDirection = player.scrollDirection + 180;
        this.currentDirection = defaultDirection;
        this.trackNumber = trackNumber;
        setDefaultRotation(trackNumber);
        this.defaultTrackStart = new Point2D(x, y);
        this.currentTrackStart = defaultTrackStart;
        this.maxIndex = -1;
        this.minIndex = 0;
    }
    
    /**
     * Set the default rotation for the track, which is the default rotation for
     * the notes and receptors (unrelated to direction).
     * @param trackNumber The index of the track in the parent game-pane's track
     * array, also, the column number that each of the notes will have.
     */
    private void setDefaultRotation(int trackNumber) {
        switch(trackNumber) {
            case 0:
                defaultRotation = 270;
                break;
            case 1:
                defaultRotation = 180;
                break;
            case 2:
                defaultRotation = 0;
                break;
            case 3: 
                defaultRotation = 90;
                break;
            default:
                defaultRotation = 0;
                break;
        }
    }
    
    public void handleInput() {
        System.out.println("You activated track " + trackNumber);
    }
    
    /**
     * Initialize the note track.
     */
    public void startGame() {
        receptor = new Receptor( // create a receptor object
                defaultTrackStart.getX() + Math.cos(Math.toRadians(defaultDirection)) * player.receptorLocation * player.trackLength,
                defaultTrackStart.getY() + Math.sin(Math.toRadians(defaultDirection + 180)) * player.receptorLocation * player.trackLength,
                defaultRotation,
                player.noteSkin);
        
        // set the size of the receptor
        receptor.setScaleX(0.3);
        receptor.setScaleY(0.3);
        
        getChildren().add(receptor); // put the receptor onscreen
    }
    
    /**
     * Redraw the note track to be up-to-date with the current time.
     * @param currentTime The current time position within the song.
     */
    public void updateTo(double currentTime) {
        // currentDirection = defaultDirection + 45*Math.sin(3.65*currentTime - Math.PI*trackNumber/4);
        // Point2D perpendicularVector = new Point2D(Math.cos(Math.toRadians(currentDirection + 90)), Math.sin(Math.toRadians(currentDirection + 180 + 90)));
        
        Point2D unitVector = new Point2D( // define the unit of distance as a vector based on the current direction
                Math.cos(Math.toRadians(currentDirection)),
                Math.sin(Math.toRadians(currentDirection + 180)));
        double maxTime = currentTime + (1 - player.receptorLocation) * player.trackLength / player.scrollSpeed; // the latest time in the song at which a note can put onscreen
        double minTime = currentTime - player.receptorLocation * player.trackLength / player.scrollSpeed; // the earliest time in the song at which a note can be removed from the screen
        
        // currentTrackStart = defaultTrackStart.add(unitVector.multiply(60*Math.sin(3.65 / 2 *currentTime + Math.PI*trackNumber/4)));
        // currentTrackStart = currentTrackStart.add(perpendicularVector.multiply(60*Math.sin(3.65 / 2 *currentTime + Math.PI*trackNumber/4)));
        // receptor.setRotate(defaultRotation - 45*Math.sin(3.65*currentTime));
        
        // update the receptor position
        receptor.setX(currentTrackStart.getX() + Math.cos(Math.toRadians(currentDirection)) * player.receptorLocation * player.trackLength);
        receptor.setY(currentTrackStart.getY() + Math.sin(Math.toRadians(currentDirection + 180)) * player.receptorLocation * player.trackLength);
        
        // put new notes onscreen if necessary
        if(maxIndex < notes.length - 1) { // if there are notes left in the array that haven't been displayed yet
            while(notes[maxIndex + 1].time < maxTime) { // while there is a note that needs to be put onscreen
                notes[++maxIndex].initialize(defaultRotation, player.noteSkin); // initialize the note
                notes[maxIndex].setScaleX(0.3);
                notes[maxIndex].setScaleY(0.3);
                getChildren().add(notes[maxIndex]); // put the note onscreen
                if(maxIndex + 2 > notes.length - 1) { // if we reached the end of the array
                    break;
                }
            }
        }
        
        // take notes offscreen if necessary
        if(minIndex < notes.length) { // if there are notes left in the array that haven't been removed yet
            while(notes[minIndex].time < minTime) { // while there is a note that needs to be taken offscreen
                getChildren().remove(notes[minIndex ++]); // remove the note
                if(minIndex > maxIndex) { // if there are no notes onscreen
                    break;
                }
            }
        }
        
        // update the positions of the onscreen notes
        for(int noteIndex = minIndex; noteIndex <= maxIndex && noteIndex > -1; noteIndex++) { // for each note onscreen
            Point2D receptorPosition = new Point2D(receptor.getX(), receptor.getY()); // make a vector for the receptor position
            Point2D notePosition = receptorPosition.add(unitVector.multiply((notes[noteIndex].time - currentTime) * player.scrollSpeed)); // calculate the note position
            
            // update the note position
            notes[noteIndex].setX(notePosition.getX());
            notes[noteIndex].setY(notePosition.getY());
            // notes[noteIndex].setRotate(defaultRotation - 45*Math.sin(3.65*currentTime));
        }
    }
}
