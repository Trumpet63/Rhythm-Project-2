package smparser;

/**
 * Class that stores information describing a BPM.
 */
public class BPM {
    String beat; // beat at which the bpm starts
    String bpm; // the beats per minute
    
    @Override
    public String toString() {
        return String.format("BPM::\n\tbeat = %s\n\tbpm = %s\n", beat, bpm);
    }
}