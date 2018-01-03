package song;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The song object contains the data of a song.
 */
public class Song {
    public String notesFileName; // usually a .sm file
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
    
    public void getInfo() {
        parser.preParse();
    }
    
    /**
     * Parses the .sm file given by the song's fileName.
     * @param mode
     */
    public void parse(int mode) {
        parser.parse(mode);
    }
    
    /**
     * Construct the requisite media player object to be able to play the music.
     * @param fileName The simpler version of the file path that is used to
     * create the media player object.
     */
    private void loadMusic(String fileName) {
        music = new MediaPlayer(
            new Media(
                new File(fileName).toURI().toString()
            )
        );
    }
}