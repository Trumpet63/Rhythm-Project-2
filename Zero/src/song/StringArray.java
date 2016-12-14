package song;

/**
 * A resizing array of strings. Initial array size is hard-coded in its
 * constructor.
 */
public class StringArray extends ResizingArray<String> {
    
    /**
     * Constructs and initializes a note array object.
     */
    StringArray() {
        numElements = 0;
        data = new String[64];
        size = 64;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        String[] newArray = new String[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
}