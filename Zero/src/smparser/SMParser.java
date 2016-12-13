package smparser;

/*
 * Parses a .sm file
 */
public class SMParser {

    /**
     * @param args the command line arguments
     */
    public static void main() {
        String file = "09_-_lord_of_the_dance.sm";
        // String file = "Mother 3.sm"; // Can use any .sm file. Must be in the same folder.
        Song song = new Song(file);
        song.parse();
    }
}