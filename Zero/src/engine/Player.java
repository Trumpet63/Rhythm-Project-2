package engine;

import javafx.scene.input.KeyCode;

/**
 * Class that stores the properties and settings unique to a player.
 */
public class Player {
    String name; // the player's name
    KeyCode[] bindings; // the keys to which each note track will be bound in order
    double scrollDirection; // the direction towards which the arrows are moving in degrees, where 0 is to the right and increasing counter-clockwise
    double scrollSpeed; // the rate at which the notes move on screen in units of pixels per second
    String noteSkin; // the file name for the image file to use for the notes
    double windowHeight; // preferred window height in pixels
    double windowWidth; // preferred window width in pixels
    double windowPadding;
    double trackLength; // usually decided by window height or width depending on scroll direction
    double receptorLocation; // as a percentage of receptor length
    double noteSize; // in units of pixels
    double trackSpacing; // in units of pixels
    AccuracyRank[] accuracies;
    double noteOffset;
    double judgeOffset;
    
    /**
     * Creates an instance of the player object.
     */
    public Player() {
        bindings = new KeyCode[4]; // temporarily assumes a 4-key setup
    }
}
