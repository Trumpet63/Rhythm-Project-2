package smparser;

/**
 * A resizing array of modes. Initial array size is hard-coded in its
 * constructor.
 */
public class ModeArray extends ResizingArray<Mode> {
    /**
     * Constructs and initializes a mode array object.
     */
    ModeArray() {
        numElements = 0;
        data = new Mode[5];
        size = 5;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        Mode[] newArray = new Mode[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
}