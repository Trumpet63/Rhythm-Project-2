package smparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * The song object contains the methods necessary to parse a song's .sm file and
 * stores the information therein.
 */
class Song {
    public String fileName;
    public HeaderArray headers;
    public ModeArray modes;
    public int currentMode;
    
    /**
     * Constructs and initializes a song object
     * @param fileName The name of the .sm file for the song.
     */
    Song(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Parses the .sm file given by the song's fileName.
     */
    public void parse() {
        List<String> records = new ArrayList<>();
        
        // read the contents of the .sm file into records line-by-line
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } 
        catch (IOException ex) {
            System.out.println("IO exception");
        }
        
        // parse the header into the header array and get the line at which the headers ended
        int endOfHeader = parseHeader(records);
        
        // parse the mode information from each mode present in the file into the mode array
        parseModes(endOfHeader, records);
        
        // set the current mode to the last mode (temporary)
        currentMode = modes.numElements - 1;
        
        // parse the notes for the current mode into that mode's track array
        parseNotes(currentMode, records);
    }
    
    /**
     * Parses the block of # statements at the start of the .sm file and stores
     * them in the headers array.
     * @param records The .sm file broken into lines.
     * @return The line number where the parsing ended.
     */
    private int parseHeader(List<String> records) {
        headers = new HeaderArray(); // initialize headers
        
        int lineIndex = 0;
        boolean end = false;
        String line;
        int endOfLabel;
        
        while(!end) { // while header section hasn't ended
            line = records.get(lineIndex); // get next line
            if(!line.isEmpty()) {
                if(line.charAt(0) == '#') { // if line begins a new label block
                    Header tempHeader = new Header();
                    endOfLabel = line.indexOf(':');
                    tempHeader.type = line.substring(1, endOfLabel); // store the label type (ex: ARTIST)
                    line = line.substring(endOfLabel + 1); // truncate off the label

                    if(line.indexOf(';') == -1) { // if data for one label spans multiple lines
                        do {
                            line = line.concat(records.get(++lineIndex)); // concat next line to this line
                        }
                        while(records.get(lineIndex).indexOf(';') == -1);
                    }

                    tempHeader.data = line.replaceAll("[\\n\\t\\r\\;]", ""); // scrub the line of unwanted characters and store it
                    if(tempHeader.data.isEmpty()) {
                        tempHeader.isBlank = true;
                    }
                    headers.insert(tempHeader); // insert the completed header object
                }
                else if(line.charAt(0) == '/'){ // if found the start of a note section
                    end = true;
                    lineIndex --; // decrement to keep lineIndex where it is upon loop exit
                }
            } // end if
            lineIndex ++;
        } // end while
        
        headers.consolidate(); // save space by shrinking array size
        return lineIndex; // return line where parsing stopped
    }
    
    /**
     * Parses the five lines of data in the header of the notes label/tag for
     * each of the modes present in the .sm file.
     * @param endOfHeader The line where parseHeader finished parsing, and the
     * line on which to begin searching for modes.
     * @param records The .sm file broken into lines.
     */
    private void parseModes(int endOfHeader, List<String> records) {
        modes = new ModeArray(); // initialize modes
        
        int currentLine = endOfHeader;
        int recordsSize = records.size();
        String line;
        
        while(currentLine < recordsSize) {
            if(records.get(currentLine).contains("#NOTES:")) {
                Mode newMode = new Mode();
                for(int i = 0; i < 6; i++) { // loop through 5 lines plus one more to get correct startLine
                    currentLine++;
                    line = records.get(currentLine).trim().replaceAll("[\\n\\t\\r\\:]", ""); // scrub the line of unwanted characters
                    switch(i) { // assign each line to a property in order (plus one more for startLine)
                        case 0:
                            newMode.chartType = line;
                            break;
                        case 1:
                            newMode.description = line;
                            break;
                        case 2:
                            newMode.difficulty = line;
                            break;
                        case 3:
                            newMode.numericalMeter = Integer.parseInt(line);
                            break;
                        case 4:
                            newMode.grooveRadar = line;
                            break;
                        case 5:
                            newMode.startLine = currentLine; // store line where actual note data starts
                            break;
                        default:
                            System.out.println("I am error");
                            break;
                    } // end switch
                } // end for
                modes.insert(newMode);
            } // end if
            currentLine++;
        } // end while
        
        modes.consolidate(); // save space by shrinking array size
    }
    
    /**
     * Parses the notes for the current mode into that mode's track array.
     * @param currentMode The index of the mode for which the note data will be
     * parsed.
     * @param records The .sm file broken into lines.
     */
    private void parseNotes(int currentMode, List<String> records) {
        int scale = 10; // the digits of precision that the calculations will be carried out with
        int measureSize;
        int currentLineIndex = modes.data[currentMode].startLine; // the line in records at which the current mode's note data starts
        int currentBPMIndex = 0; // the index of the bpm that applies to the current line being processed
        boolean tracksInitialized = false; // ensures the track array will be initialized only once
        if(records.get(currentLineIndex).contains("/")) { // if the note data doesn't actually start at this line in records
            currentLineIndex ++; // the note data (probably) starts on the next line
        }
        BPMArray bpms = parseBPMS(headers.find("bpms").data); // parse the bpms into a easier-to-use format
        BigDecimal currentTime = new BigDecimal(headers.find("offset").data).negate().setScale(scale); // initialize current time to the negative of the offset
        BigDecimal currentBPM = new BigDecimal(bpms.data[0].bpm).setScale(scale); // initial bpm is always the first bpm given
        BigDecimal currentBeat = BigDecimal.ZERO.setScale(scale); // the first note starts at beat zero
        BigDecimal four = new BigDecimal(4).setScale(scale); // the number 4 is useful in the beatLength calculation
        
        StringArray measure; // array where each line is a line in the current measure
        System.out.println("time       measurePosition  measureSize  columnNumber  bpm");
        while((measure = getNextMeasure(currentLineIndex, records)) != null) { // while there is a next measure, store it in the array
            if(!tracksInitialized) { // the current mode's tracks must be initialized (only runs once)
                modes.data[currentMode].tracks = new TrackArray();
                for(int i = 0; i < measure.data[0].length(); i++) { // the number of characters in a line is the number of tracks
                    modes.data[currentMode].tracks.insert(new NoteArray()); // so insert an array of notes for each track
                }
                tracksInitialized = true; // tracks will only initialize once
            }
            
            measureSize = measure.numElements;
            for(int noteNumber = 0; noteNumber < measureSize; noteNumber++) { // process the measure
                String line = measure.data[noteNumber]; // get current line from measure
                for(int colNumber = 0; colNumber < line.length(); colNumber++) { // store one line's worth of notes
                    char noteCharacter = line.charAt(colNumber); // get current character from line
                    if(noteCharacter == '1' || noteCharacter == '2' || noteCharacter == '4') { // if the character is an actual note
                        Note newNote = new Note(); // build a note
                        newNote.time = currentTime.doubleValue();
                        newNote.bpm = currentBPM.doubleValue();
                        newNote.columnNumber = colNumber;
                        newNote.measurePosition = noteNumber;
                        newNote.measureSize = measureSize;
                        modes.data[currentMode].tracks.data[colNumber].insert(newNote); // store the note in the proper track
                        System.out.println(newNote.toString());
                    }
                }
                
                BigDecimal beatLength = four.divide(new BigDecimal(measureSize).setScale(scale), RoundingMode.HALF_UP); // the number of beats a single line of the measure is worth
                BigDecimal nextBeat = currentBeat.add(beatLength); // the beat number of the next line
                
                if(bpmChanges(nextBeat, bpms, scale, currentBPMIndex)) { // if the bpm changes between this line and the next
                    BigDecimal midBeat; // a beat number (given by a bpm change) part-way between current and next beat
                    do {
                        midBeat = new BigDecimal(bpms.data[currentBPMIndex + 1].beat); // get the beat position of the upcoming bpm change
                        
                        // add: 60*(nextBeat - currentBeat)/currentBPM
                        currentTime = currentTime.add(new BigDecimal(60).setScale(scale).multiply(midBeat.subtract(currentBeat)).divide(currentBPM, RoundingMode.HALF_UP));
                        
                        currentBPM = new BigDecimal(bpms.data[++currentBPMIndex].bpm); // now the bpm has been updated
                        currentBeat = midBeat; // we have moved past the bpm change
                    } while(bpmChanges(nextBeat, bpms, scale, currentBPMIndex)); // check for another bpm change
                }
                // add: 60*(nextBeat - currentBeat)/currentBPM
                currentTime = currentTime.add(new BigDecimal(60).setScale(scale).multiply(nextBeat.subtract(currentBeat)).divide(currentBPM, RoundingMode.HALF_UP));
                
                currentBeat = nextBeat; // we have moved to the next line
            } // end for
            currentLineIndex += measureSize + 1; // move the current line to the next measure (plus one to skip over the measure delimiter)
        } // end while
    } // end parseNotes
    
    /**
     * Parse the bpm string given in the header of the .sm file into a more
     * useful format for faster access.
     * @param bpmString The string of bpms parsed from the header.
     * @return An array of beat-bpm pairs.
     */
    private BPMArray parseBPMS(String bpmString) {
        BPMArray bpms = new BPMArray();
        String[] bpmPairs = bpmString.split(",");
        bpms.guaranteeSize(bpmPairs.length);
        
        for (String bpmPair : bpmPairs) {
            String[] array = bpmPair.split("=");
            BPM newBPM = new BPM();
            newBPM.beat = array[0];
            newBPM.bpm = array[1];
            bpms.insert(newBPM);
        }
        
        bpms.consolidate();
        
        return bpms;
    }
    
    /**
     * Get the next measure from the note data in the .sm file.
     * @param currentLineIndex The line at which the measure will start.
     * @param records The sm file broken into lines.
     * @return The array of lines making up the measure, or null if there are
     * no measures left for this mode.
     */
    private StringArray getNextMeasure(int currentLineIndex, List<String> records) {
        StringArray measure = null;
        if(currentLineIndex < records.size()) { // if end of records return null
            if(!records.get(currentLineIndex).isEmpty()) { // if reached empty line return null
                measure = new StringArray();
                String line = records.get(currentLineIndex);
                while(!line.contains(",") && !line.contains(";")) { // while there are more lines in the measure
                    measure.insert(line.replaceAll("[\\n\\t\\r\\:]", "")); // add the line to the measure after scrubbing
                    currentLineIndex ++; // move to next line in records
                    line = records.get(currentLineIndex); // get a line from records
                }
                measure.consolidate(); // make array the same size as the number of elements its storing
            }
        }
        
        return measure;
    }
    
    /**
     * Tells whether the bpm changes between the current beat and the beat at
     * which the next line starts.
     * @param nextBeat The beat at which the next line starts.
     * @param bpms Array of bpm information parsed from the header of the .sm.
     * @param scale The number of digits to which the calculations will be
     * carried out.
     * @param currentBPMIndex The index of the bpm affecting the current line.
     * @return Whether there is a bpm change.
     */
    private boolean bpmChanges(BigDecimal nextBeat, BPMArray bpms, int scale, int currentBPMIndex) {
        boolean bpmChanged = false;
        BigDecimal nextBPMBeat; // the beat at which the next bpm starts
        
        if(currentBPMIndex != bpms.numElements - 1) { // if the current bpm index is not the last one in the bpms array (otherwise return false)
            nextBPMBeat = new BigDecimal(bpms.data[currentBPMIndex + 1].beat).setScale(scale); // get the beat at which the next bpm starts
            
            if(nextBPMBeat.compareTo(nextBeat) < 0) { // if the next bpm starts before the next line, then the bpm changes (otherwise return false)
                bpmChanged = true;
            }
        }
        
        return bpmChanged;
    }
}