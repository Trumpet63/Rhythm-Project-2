package smparser;

/**
 * A resizing array of note arrays, each corresponding to a track. Initial array
 * size is hard-coded in its constructor.
 */
public class TrackArray extends ResizingArray<NoteArray> {
    /**
     * Constructs and initializes a note array object.
     */
    TrackArray() {
        numElements = 0;
        data = new NoteArray[4];
        size = 4;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        NoteArray[] newArray = new NoteArray[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
}
