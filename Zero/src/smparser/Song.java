package smparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    
    /**
     * Constructs and initializes a song object
     * @param fileName The name of the .sm file for the song.
     */
    Song(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Parses the .sm file associated with the song object.
     */
    public void parse() {
        List<String> records = new ArrayList<>();
        
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
        
        int endOfHeader = parseHeader(records);
        parseModes(endOfHeader, records);
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
}