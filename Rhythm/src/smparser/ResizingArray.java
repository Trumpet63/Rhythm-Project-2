package smparser;

/**
 * The resizing array allows for infinite, fast insertions. Useful when only
 * simple operations are required.
 * 
 * @param <T> The type of data the array will store
 */
public abstract class ResizingArray<T> {
    T[] data;
    int size; // max number of elements array can hold
    int numElements; // current number of elements in array
    
    /**
     * Insert an item into the array. Insert triggers a resize when the array is
     * full. The new array size will be larger by a factor of 1.5, plus one to
     * handle an edge case where the current array size is 1.
     * @param item The item to be inserted.
     */
    public void insert(T item) {
        if(numElements + 1 > size) {
            resize(size+size/2+1);
        }
        
        data[numElements++] = item;
    }
    
    /**
     * Allows for manual resizing. Will not resize if size is already bigger
     * than the requested size.
     * @param size Requested size to make the array.
     */
    public void guaranteeSize(int size) {
        if(this.size < size) {
            resize(size);
        }
    }
    
    // cannot create a new array in an abstract class unless you do something fancy
    /**
     * Copies the elements in data into a new, presumably larger array.
     * @param newSize Size of the array to copy data array into.
     */
    protected abstract void resize(int newSize);
}
