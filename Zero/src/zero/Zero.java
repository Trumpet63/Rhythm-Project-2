package zero;

import javafx.application.Application;
import javafx.stage.Stage;
import engine.Engine;

/**
 * @version 0.6 12/13/16
 * @author Trumpet63
 */
public class Zero extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Engine engine = new Engine();
        engine.start(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
