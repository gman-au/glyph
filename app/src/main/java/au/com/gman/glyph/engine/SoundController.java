/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import au.com.gman.glyph.R;
import au.com.gman.glyph.level.Library;
import android.content.Context;
import android.media.SoundPool;
import android.media.AudioManager;
import java.util.HashMap;
import java.util.Vector;
import android.os.Handler;
import android.media.SoundPool.OnLoadCompleteListener;
/**
 *
 * @author gub
 */
public class SoundController {

    private int loaded = 0;
    private SoundPool mSoundPool; 
    private HashMap<Integer, Integer> mSoundPoolMap; 
    private AudioManager  mAudioManager;
    private Context mContext;
    private Vector<Integer> mAvailibleSounds = new Vector<Integer>();
    private Vector<Integer> mKillSoundQueue = new Vector<Integer>();
    private Handler mHandler = new Handler();
    private boolean enabled = true;
    
    public boolean getEnabled() { return enabled; }
    public boolean hasLoaded() { return loaded == mSoundPoolMap.size() - 1; }
    public void setEnabled(boolean value) { enabled = value; }
    
    public SoundController(Context theContext) {
        initSounds(theContext);
    }

    public void initSounds(Context theContext) { 
        mContext = theContext;
        mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); 
        mSoundPoolMap = new HashMap<Integer, Integer>(); 
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);      
        
        addSound(Library.SOUND_ALERT_A, R.raw.alert_a);
        addSound(Library.SOUND_ALERT_B, R.raw.alert_b);
        addSound(Library.SOUND_ALERT_C, R.raw.alert_c);
        addSound(Library.SOUND_ALERT_D, R.raw.alert_d);
        addSound(Library.SOUND_ALERT_E, R.raw.alert_e);
        addSound(Library.SOUND_ALERT_F, R.raw.alert_f);
        addSound(Library.SOUND_BEEP_A, R.raw.beep_a);
        addSound(Library.SOUND_BEEP_B, R.raw.beep_b);
        addSound(Library.SOUND_BEEP_C, R.raw.beep_c);
        addSound(Library.SOUND_BEEP_D, R.raw.beep_d);
        addSound(Library.SOUND_BEEP_E, R.raw.beep_e);
        addSound(Library.SOUND_BEEP_F, R.raw.beep_f);
        addSound(Library.SOUND_BEEP_G, R.raw.beep_g);
        addSound(Library.SOUND_BEEP_H, R.raw.beep_h);
        addSound(Library.SOUND_BEEP_I, R.raw.beep_i);
        addSound(Library.SOUND_BOMB_EXPLODE, R.raw.bombexplode);
        
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                loaded++;
            }
        });
    } 

    public void addSound(int Index, int SoundID)
    {
        mAvailibleSounds.add(Index);
        mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
    }

    public void playSound(int index) { 
        if (enabled) {
            // dont have a sound for this obj, return.
            if(mAvailibleSounds.contains(index)){

                int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
                int soundId = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);

                mKillSoundQueue.add(soundId);

                // schedule the current sound to stop after set milliseconds
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if(!mKillSoundQueue.isEmpty()){
                        mSoundPool.stop(mKillSoundQueue.firstElement());
                        }
                    }
                }, 3000);   
            }
        }
    }
    
    public void destroy() {
        mContext = null;
        mAvailibleSounds.clear();
        mKillSoundQueue.clear();
        mSoundPoolMap.clear();
        mAudioManager.unloadSoundEffects();
        mAudioManager = null;
        mSoundPool.release();
        mSoundPool = null;
    }

}
