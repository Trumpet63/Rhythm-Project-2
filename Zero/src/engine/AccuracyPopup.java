package engine;

import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 
 */
public class AccuracyPopup extends Transition{
    Text text;
    Color color;
    String string;
    
    double attackDuration = 0.25;
    double scalingRate = 1;
    
    public AccuracyPopup(Text text, double duration) {
        this.setCycleDuration(new Duration(duration*1000));
        this.text = text;
    }
    
    public void initialize(Color color, String string) {
        this.stop();
        this.color = color;
        this.string = string;
    }
    
    @Override
    protected void interpolate(double frac) {
        text.setText(string);
        text.setFill(color);
        text.setOpacity(-Math.abs(frac - attackDuration) + (1 - attackDuration));
        text.setScaleX(scalingRate*(-Math.abs(frac - attackDuration) + (1 - attackDuration)));
        text.setScaleY(scalingRate*(-Math.abs(frac - attackDuration) + (1 - attackDuration)));
        // text.setX(x);
        // text.setY(y);
    }
}
