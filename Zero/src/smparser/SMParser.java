package smparser;

/*
 * Parses a .sm file
 */
public class SMParser {

    /**
     * 
     */
    public static void main() {
        String file = "09_-_lord_of_the_dance.sm"; // Can use any .sm file. Must be in the same folder.
        Song song = new Song(file);
        song.parse();
    }
}