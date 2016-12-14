package engine;

import javafx.scene.input.KeyCode;

/**
 * 
 */
public class Player {
    String name;
    KeyCode[] bindings;
    
    public Player() {
        bindings = new KeyCode[4];
    }
}
