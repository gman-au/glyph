/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import au.com.gman.glyph.R;
import au.com.gman.glyph.level.Library.Transition;
import java.util.Random;
import android.media.MediaPlayer;
import android.content.Context;

/**
 *
 * @author gub
 */
public class OrganicSound {
   
    public static final int MINIMUM_INTERVAL = 1000;
    public static final int MAXIMUM_INTERVAL = 4000;
    public static final float AMPLITUDE_TRANSITION_SPEED = 0.001f;
    
    private float[] amplitudes;
    private Transition[] transitions;
    private int[] intervals;
    private int[] streamIDs;
    private MediaPlayer[] players;
    private MediaPlayer playerX;
    private Random random;
    private boolean enabled = true;
    private boolean beatX;
    
    public boolean getEnabled() { return enabled; }    
    public boolean getBeatX() { return beatX; }
    public void setBeatX(boolean BeatX) { beatX = BeatX; }
    
    public OrganicSound(Context context) {
        int channels = 7;
        random = new Random();
        amplitudes = new float[channels];
        transitions = new Transition[channels];
        intervals = new int[channels];
        streamIDs = new int[channels];
        players = new MediaPlayer[channels];
        playerX = new MediaPlayer();
        beatX = false;
        
        for (int i=0; i < intervals.length; i++) {
            intervals[i] = random.nextInt(MAXIMUM_INTERVAL - MINIMUM_INTERVAL) + MINIMUM_INTERVAL;
            amplitudes[i] = 0.0f;
            transitions[i] = Transition.SWITCHING_ON;
            streamIDs[i] = 0;            
        }
        players[0] = MediaPlayer.create(context, R.raw.track1);
        players[1] = MediaPlayer.create(context, R.raw.track2);
        players[2] = MediaPlayer.create(context, R.raw.track3);
        players[3] = MediaPlayer.create(context, R.raw.track4);
        players[4] = MediaPlayer.create(context, R.raw.track5);
        players[5] = MediaPlayer.create(context, R.raw.track6);
        players[6] = MediaPlayer.create(context, R.raw.track7);
        playerX = MediaPlayer.create(context, R.raw.track8);
        initSounds();
    }
    
    private void initSounds() {
        for (int i=0; i < intervals.length; i++) {
            players[i].setLooping(true);
            players[i].start();
            players[i].setVolume(amplitudes[i], amplitudes[i]);
        }        
        playerX.setLooping(true);
        playerX.start();
        playerX.setVolume(0.0f, 0.0f);
    }
    
    public void tick() {
        for (int i=0; i < intervals.length; i++) {
            intervals[i]--;
            if (intervals[i] <= 0) {
                if (transitions[i].equals(Transition.SWITCHING_OFF)) { 
                    transitions[i] = Transition.SWITCHING_ON; 
                } else if (transitions[i].equals(Transition.SWITCHING_ON)) { 
                    transitions[i] = Transition.SWITCHING_OFF; 
                }
                intervals[i] = random.nextInt(MAXIMUM_INTERVAL - MINIMUM_INTERVAL) + MINIMUM_INTERVAL;
            }

            if (transitions[i].equals(Transition.SWITCHING_ON)) { amplitudes[i] += AMPLITUDE_TRANSITION_SPEED ; if (amplitudes[i] > 1.0f) {amplitudes[i] = 1.0f; } }
            if (transitions[i].equals(Transition.SWITCHING_OFF)) { amplitudes[i] -= AMPLITUDE_TRANSITION_SPEED ; if (amplitudes[i] < 0.0f) {amplitudes[i] = 0.0f; } }

            players[i].setVolume(amplitudes[i] * 0.5f, amplitudes[i] * 0.5f);
        }
        playerX.setVolume(1.0f * (beatX? 1:0), 1.0f * (beatX? 1:0));
    }
    
    public void setEnabled(boolean value) {    
        if (!value) {
            playerX.pause();
        } else { 
            if (!(playerX.isPlaying())) { 
                if (beatX) { 
                    playerX.start(); 
                } 
            } 
        }
        for (MediaPlayer player : players) {
            if (!value) {
                player.pause();
            } else {
                if (!(player.isPlaying())) {
                    player.start();   
                }
            }
        }
        enabled = value;
    }
    
    public void destroy() {
        for (MediaPlayer player : players) {
            player.stop();
            player.release();
            player = null;
        }
        players = null;
        playerX.stop();
        playerX.release();
        playerX = null;
    }
}
