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
    GamePane game;
    int trackCount; // the number of tracks in the game to which this track belongs
    
    double currentLength;
    double currentDirection;
    int maxOnscreenIndex; // index of the note on screen with the highest time
    int minOnscreenIndex; // index of the note on screen with the lowest time
    int maxHittableIndex;
    int minHittableIndex;
    Receptor receptor;
    Point2D currentTrackStart;
    double currentTime = 0;
    
    /**
     * Construct a note-track object.
     * @param notes The array of notes from a particular column of the notes
     * parsed from the .sm file.
     * @param player The player belonging to this track's parent game-pane.
     * @param trackNumber The index of the track in the parent game-pane's track
     * array, also, the column number that each of the notes will have.
     * @param trackCount The number of tracks in the game to which this track
     * belongs.
     * @param x The x-coordinate of the track's default starting position.
     * @param y The y-coordinate of the track's default starting position.
     * @param game The game to which the track belongs.
     */
    public NoteTrack(
            Note[] notes, 
            Player player,
            int trackNumber,
            int trackCount,
            double x,
            double y,
            GamePane game) {
        this.notes = notes;
        this.player = player;
        this.defaultLength = player.trackLength;
        currentLength = defaultLength;
        defaultDirection = player.scrollDirection + 180;
        currentDirection = defaultDirection;
        this.trackNumber = trackNumber;
        this.trackCount = trackCount;
        setDefaultRotation(trackNumber, trackCount);
        defaultTrackStart = new Point2D(x, y);
        currentTrackStart = defaultTrackStart;
        maxOnscreenIndex = -1;
        minOnscreenIndex = 0;
        maxHittableIndex = -1;
        minHittableIndex = 0;
        this.game = game;
    }
    
    /**
     * Set the default rotation for the track, which is the default rotation for
     * the notes and receptors (unrelated to direction).
     * @param trackNumber The index of the track in the parent game-pane's track
     * array, also, the column number that each of the notes will have.
     */
    private void setDefaultRotation(int trackNumber, int trackCount) {
        int side = trackNumber / (trackCount / 2);
        int positionInSide = trackNumber % (trackCount / 2);
        
        double angle = 270 + side * 90;
        angle -= Math.pow(-1, side) * positionInSide * 90f / (trackCount / 2 - 1);
        
        defaultRotation = angle;
    }
    
    public void handleInput() {
        if(minHittableIndex > maxHittableIndex) {
            game.causeAccuracyPopup(player.accuracies.length - 1, trackNumber);
        }
        else {
            double accuracy = notes[minHittableIndex].time - currentTime - player.judgeOffset; // + is early, - is late
            int rank = 0;
            while(accuracy > player.accuracies[rank].boundaryValue) {
                rank ++;
                if(rank > player.accuracies.length - 1) {
                    break;
                }
            }
            game.causeAccuracyPopup(rank, trackNumber);
            notes[minHittableIndex].setOpacity(0.1);
            minHittableIndex ++;
        }
    }
    
    private void causeMiss() {
        game.causeAccuracyPopup(0, trackNumber);
    }
    
    /**
     * Initialize the note track.
     */
    public void startGame() {
        receptor = new Receptor( // create a receptor object
                defaultTrackStart.getX() + Math.cos(Math.toRadians(defaultDirection)) * player.receptorLocation * player.trackLength,
                defaultTrackStart.getY() + Math.sin(Math.toRadians(defaultDirection + 180)) * player.receptorLocation * player.trackLength,
                defaultRotation,
                player.noteSize,
                player.noteSkin);
        
        getChildren().add(receptor); // put the receptor onscreen
    }
    
    /**
     * Redraw the note track to be up-to-date with the current time.
     * @param currentTime The current time position within the song.
     */
    public void updateTo(double currentTime) {
        currentTime += player.noteOffset;
        this.currentTime = currentTime ;
        Point2D unitVector = new Point2D( // define the unit of distance as a vector based on the current direction
                Math.cos(Math.toRadians(currentDirection)),
                Math.sin(Math.toRadians(currentDirection + 180)));
        double maxTime = currentTime + (1 - player.receptorLocation) * player.trackLength / player.scrollSpeed; // the latest time in the song at which a note can put onscreen
        double minTime = currentTime - player.receptorLocation * player.trackLength / player.scrollSpeed; // the earliest time in the song at which a note can be removed from the screen
        
        // put new notes onscreen if necessary
        if(maxOnscreenIndex < notes.length - 1) { // if there are notes left in the array that haven't been displayed yet
            while(notes[maxOnscreenIndex + 1].time < maxTime) { // while there is a note that needs to be put onscreen
                notes[++maxOnscreenIndex].initialize(defaultRotation, player.noteSize, player.noteSkin); // initialize the note
                getChildren().add(notes[maxOnscreenIndex]); // put the note onscreen
                if(maxOnscreenIndex + 1 > notes.length - 1) { // if we reached the end of the array
                    break;
                }
            }
        }
        
        // take notes offscreen if necessary
        if(minOnscreenIndex < notes.length) { // if there are notes left in the array that haven't been removed yet
            while(notes[minOnscreenIndex].time < minTime) { // while there is a note that needs to be taken offscreen
                getChildren().remove(notes[minOnscreenIndex ++]); // remove the note
                if(minOnscreenIndex > maxOnscreenIndex) { // if there are no notes onscreen
                    break;
                }
            }
        }
        
        // update the positions of the onscreen notes
        for(int noteIndex = minOnscreenIndex; noteIndex < maxOnscreenIndex + 1; noteIndex++) { // for each note onscreen
            Point2D receptorPosition = new Point2D(receptor.getX(), receptor.getY()); // make a vector for the receptor position
            Point2D notePosition = receptorPosition.add(unitVector.multiply((notes[noteIndex].time - currentTime) * player.scrollSpeed)); // calculate the note position
            
            // update the note position
            notes[noteIndex].setX(notePosition.getX());
            notes[noteIndex].setY(notePosition.getY());
        }
        
        // moves notes into hittable
        if(maxHittableIndex < notes.length - 1) {
            while(notes[maxHittableIndex + 1].time - currentTime - player.judgeOffset < player.accuracies[player.accuracies.length - 1].boundaryValue) {
                maxHittableIndex ++;
                if(maxHittableIndex + 1 > notes.length - 1) { // if we reached the end of the array
                    break;
                }
            }
        }
        
        // takes notes out of hittable and causes misses if necessary
        if(minHittableIndex < notes.length) {
            while(notes[minHittableIndex].time - currentTime - player.judgeOffset < player.accuracies[0].boundaryValue) {
                causeMiss();
                minHittableIndex ++;
                if(minHittableIndex > maxHittableIndex) {
                    break;
                }
            }
        }
    }
}
