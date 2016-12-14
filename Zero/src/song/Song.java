package song;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The song object contains the methods necessary to parse a song's .sm file and
 * stores the information therein.
 */
public class Song {
    public String notesFileName;
    public String musicFileName;
    public HeaderArray headers;
    public ModeArray modes;
    public int currentMode;
    private final Parser parser;
    public MediaPlayer music;
    
    /**
     * Constructs and initializes a song object.
     * @param notesFileName The file name of the song's .sm file.
     * @param musicFileName The file name of the song's music/sound file.
     */
    public Song(String notesFileName, String musicFileName) {
        this.notesFileName = notesFileName;
        this.musicFileName = musicFileName;
        parser = new Parser(this);
        loadMusic(musicFileName);
    }
    
//    public Song(String fileName, int mode) {
//        this.fileName = fileName;
//        parser = new Parser(this);
//    }
    
    /**
     * Parses the .sm file given by the song's fileName.
     */
    public void parse() {
        parser.parse();
    }
    
    private void loadMusic(String fileName) {
        music = new MediaPlayer(
            new Media(
                new File(fileName).toURI().toString()
            )
        );
    }
}