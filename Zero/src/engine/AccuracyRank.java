package engine;

import javafx.scene.paint.Color;

/**
 * 
 */
public class AccuracyRank {
    double boundaryValue;
    String name;
    double scoreValue;
    Color textColor;
    
    public AccuracyRank(double boundaryValue, String name, double scoreValue, Color textColor) {
        this.boundaryValue = boundaryValue;
        this.name = name;
        this.scoreValue = scoreValue;
        this.textColor = textColor;
    }
}
