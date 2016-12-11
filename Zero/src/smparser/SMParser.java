package smparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Parses a .sm file
 */
public class SMParser {

    public static void main() {
        String file = "Mother 3.sm"; // Can use any .sm file. Must be in the same folder.
        Song song = new Song(file);
        song.parse();
        System.out.println(String.format("Headers Content:\n\tnumElements = %d\n\tsize = %d\n", song.headers.numElements, song.headers.size));
        for(int i = 0; i < song.headers.numElements; i++) {
            System.out.println(song.headers.data[i].toString());
        }
        
    }
}

/**
 * The song object contains the methods necessary to parse a song's .sm file and
 * stores the information therein.
 */
class Song {
    public String fileName;
    public HeaderArray headers;
    
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
            }
            lineIndex ++;
        }
        
        headers.consolidate(); // save space by shrinking array size
        return lineIndex; // return line where parsing stopped
    }
}