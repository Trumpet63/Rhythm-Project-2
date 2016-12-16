package engine;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the current song time for a note track, and indicates to the
 * player the correct time at which to press a button.
 */
public class Receptor extends ImageView {
    
    /**
     * Creates an instance of a receptor object.
     * @param x The x-coordinate of this receptor.
     * @param y The y-coordinate of this receptor.
     * @param rotation The angle at which the receptor's noteskin will be rotated.
     * @param fileName The name of the image file to use as the noteskin.
     */
    public Receptor(double x, double y, double rotation, String fileName) {
        this.setX(x);
        this.setY(y);
        this.setRotate(rotation);
        loadImage(fileName);
        this.setOpacity(.70); // by default the receptor will be slightly transparent
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
