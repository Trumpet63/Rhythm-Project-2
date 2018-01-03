package zero;

import javafx.application.Application;
import javafx.stage.Stage;
import engine.Engine;

/**
 * @version 0.9 01/03/18
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
