package engine;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Visual representation of a note parsed from the .sm file.
 */
public class Note extends ImageView {
    double time; // the time at which the note occurs in the song
    
    /**
     * Create an instance of the note object.
     * @param time The time at which the note occurs in the song.
     */
    public Note(double time) {
        this.time = time;
    }
    
    /**
     * Prepare the note to be displayed onscreen.
     * @param rotation The angle at which the note's noteskin will be rotated.
     * @param size
     * @param fileName The name of the image file to use as the noteskin.
     */
    public void initialize(double rotation, double size, String fileName) {
        this.setRotate(rotation);
        loadImage(fileName);
        double scaleFactor = size / this.getImage().getWidth();
        this.setScaleX(scaleFactor);
        this.setScaleY(scaleFactor);
    }
    
    /**
     * Construct the requisite image object to be able to display a note.
     * @param imagePath The simpler version of the file path that is used to
     * create the image object.
     */
    private void loadImage(String imagePath) {
        this.setImage(
            new Image(
                new File(imagePath).toURI().toString()
            )
        );
    }
}
