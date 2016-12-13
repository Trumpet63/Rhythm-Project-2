package smparser;

/**
 * Class that stores information describing a note.
 */
public class Note {
    double time; // time position in seconds of the note in the song
    int measurePosition; // position in the measure the note was a part of
    int measureSize; // size of the measure the note was a part of
    int columnNumber; // the track or column to which the note belongs
    double bpm; // the bpm of the song at the note's time
    
    @Override
    public String toString() {
        return String.format("%-10.4f %-16d %-12d %-13d %-13.4f", time, measurePosition, measureSize, columnNumber, bpm);
    }
}