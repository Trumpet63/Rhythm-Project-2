package smparser;

/**
 * Class that stores information describing a header.
 */
public class Header {
    String type; // the label that was associated with the header, i.e. ARTIST
    String data; // the information that followed the label, i.e. the artist's name
    boolean isBlank; // whether the data string is of length 0
}
