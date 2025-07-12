package au.com.gman.glyph;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.engine.OrganicSound;
import au.com.gman.glyph.engine.Panel;

public class Glyph extends Activity
{
    private SoundController sounds;
    private OrganicSound music;
    private Panel panel;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try {
            sounds = new SoundController(this);
            music = new OrganicSound(this);
            panel = new Panel(this);
            
            super.onCreate(savedInstanceState);
            
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            setContentView(panel);             
        } catch (Exception e) {
            System.exit(1);
        }
    }
    
    public SoundController getSounds() { return sounds; }
    public OrganicSound getMusic() { return music; }
    
    @Override
    public void onBackPressed() { }

    public void terminate() {        
        if (!(sounds == null)) { sounds.destroy(); }
        sounds = null;
        if (!(music == null)) { music.destroy(); }
        music = null;                
        if (!(panel == null)) { panel.destroy(); }
        panel = null;
        this.finish();
        //System.gc();
        System.exit(1);
    }
    
    @Override
    public void onSaveInstanceState(Bundle save) {
        super.onSaveInstanceState(save);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}
