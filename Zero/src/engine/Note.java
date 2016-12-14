package engine;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 */
public class Note extends ImageView {
    
    public Note(double x, double y, double rotation, String fileName) {
        this.setX(x);
        this.setY(y);
        this.setRotate(rotation);
        loadImage(fileName);
    }
    
    private void loadImage(String imagePath) {
        this.setImage(
            new Image(
                new File(imagePath).toURI().toString()
            )
        );
    }
}
