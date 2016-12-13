package smparser;

import java.math.BigDecimal;

/**
 * Class that stores information describing a BPM.
 */
public class BPM {
    BigDecimal beat; // the beat position at which this bpm occurs
    String bpm; // the beats per minute
    
    @Override
    public String toString() {
        return String.format("BPM::\n\tbeat = %s\n\tbpm = %s\n", beat.toPlainString(), bpm);
    }
}