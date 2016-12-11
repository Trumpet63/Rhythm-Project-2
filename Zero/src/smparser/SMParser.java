package smparser;

/*
 * Parses a .sm file
 */
public class SMParser {

    /**
     * @param args the command line arguments
     */
    public static void main() {
        // testing time
        NoteArray notes = new NoteArray();
        
        Note note1 = new Note();
        note1.time = 1;
        note1.measureSize = 4;
        note1.measurePosition = 3;
        note1.columnNumber = 1;
        
        Note note2 = new Note();
        note2.time = 2.5;
        note2.measureSize = 8;
        note2.measurePosition = 6;
        note2.columnNumber = 3;
        
        notes.insert(note1);
        notes.insert(note2);
        
        System.out.println(String.format("Size = %d   NumElements = %d\n\tnote 1 time: %f\n\tnote 2 time: %f", notes.size, notes.numElements, notes.data[0].time, notes.data[1].time));
        
        HeaderArray headers = new HeaderArray();
        
        Header head1 = new Header();
        head1.type = "ARTIST";
        head1.data = "Trumpet63";
        head1.isBlank = false;
        
        Header head2 = new Header();
        head2.type = "TITLE";
        head2.data = "";
        head2.isBlank = true;
        
        headers.insert(head1);
        headers.insert(head2);
        
        System.out.println(String.format("Size = %d   NumElements = %d\n\theader 1 type: %s\n\theader 2 type: %s", headers.size, headers.numElements, headers.data[0].type, headers.data[1].type));
    }
}