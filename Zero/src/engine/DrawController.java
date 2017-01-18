/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import javafx.animation.AnimationTimer;

/**
 * 
 */
public class DrawController extends AnimationTimer {
    NoteTrack[] allTracks;
    double startTime;
    
    public DrawController(NoteTrack[] allTracks) {
        this.allTracks = allTracks;
    }
    
    public void initializeStartTime(double startTime) {
        this.startTime = startTime;
    }
    
    @Override
    public void handle(long now) {
        double currentTime = ((double)now)/1000000000 - startTime;
        for(NoteTrack track: allTracks) {
            track.updateTo(currentTime);
        }
    }
}
