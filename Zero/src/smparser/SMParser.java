package smparser;

/*
 * Parses a .sm file
 */
public class SMParser {

    /**
     * @param args the command line arguments
     */
    public static void main() {
        String file = "Mother 3.sm"; // Can use any .sm file. Must be in the same folder.
        Song song = new Song(file);
        song.parse();
        System.out.println(String.format("Headers Content:\n\tnumElements = %d\n\tsize = %d\n", song.headers.numElements, song.headers.size));
        for(int i = 0; i < song.headers.numElements; i++) {
            System.out.println(song.headers.data[i].toString());
        }
        System.out.println(String.format("Modes Content:\n\tnumElements = %d\n\tsize = %d\n", song.modes.numElements, song.modes.size));
        for(int i = 0; i < song.modes.numElements; i++) {
            System.out.println(song.modes.data[i].toString());
        }
    }
}