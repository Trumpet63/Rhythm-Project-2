package song;

/**
 * A resizing array of bpms. Initial array size is hard-coded in its
 * constructor.
 */
public class BPMArray extends ResizingArray<BPM> {
    
    /**
     * Constructs and initializes a bpm array object.
     */
    BPMArray() {
        numElements = 0;
        data = new BPM[10];
        size = 10;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        BPM[] newArray = new BPM[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
}