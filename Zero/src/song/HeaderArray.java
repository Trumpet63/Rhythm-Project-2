package song;

/**
 * A resizing array of headers. Initial array size is hard-coded in its
 * constructor.
 */
public class HeaderArray extends ResizingArray<Header> {
    /**
     * Constructs and initializes a header array object.
     */
    HeaderArray() {
        numElements = 0;
        data = new Header[20];
        size = 20;
    }
    
    @Override
    /**
     * Copies the elements in data into a new array.
     * @param newSize Size of the array to copy data array into.
     */
    protected void resize(int newSize) {
        Header[] newArray = new Header[newSize];
        System.arraycopy(data, 0, newArray, 0, numElements);
        data = newArray;
        size = newSize;
    }
    
    /**
     * Does a linear search on the contents of data, matching the header type
     * with the given type.
     * @param typeKey Type to be found in the headers.
     * @return The matched header, or null if no match was found.
     */
    public Header find(String typeKey) {
        Header header = null;
        boolean found = false;
        for(int i = 0; i < numElements && !found; i++) {
            if(data[i].type.compareToIgnoreCase(typeKey) == 0) {
                header = data[i];
                found = true;
            }
        }
        return header;
    }
}
