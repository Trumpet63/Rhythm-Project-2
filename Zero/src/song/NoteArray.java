package song;

/**
 * A resizing array of notes. Initial array size is hard-coded in its
 * constructor.
 */
public class NoteArray extends ResizingArray<Note> {
    boolean isInitialized = false;
    
    /**
     * Constructs and initializes a note array object.
     */
    NoteArray() {
        numElements = 0;
        data = new Note[100];
        size = 100;
        isInitialized = true;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        Note[] newArray = new Note[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
}
