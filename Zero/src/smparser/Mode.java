package smparser;

/**
 * 
 */
public class Mode {
    // the five lines of data in the header of the notes label/tag
    String chartType;
    String description;
    String difficulty;
    int numericalMeter;
    String grooveRadar;
    
    /* The line number of the line in the file where the modes' note data
    *  starts, where 0 is the first line of the file.
    */
    int startLine;
    // toDo: noteData
    
    @Override
    public String toString() {
        return String.format("Mode::\n\tchartType = %s\n\tdescription = %s\n\tdifficulty = %s\n\tnumericalMeter = %d\n\tgrooveRadar = %s\n\tstartLine = %d\n", chartType, description, difficulty, numericalMeter, grooveRadar, startLine);
    }
}
