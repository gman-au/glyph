/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import au.com.gman.glyph.R;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.level.Library;
import java.util.HashMap;
import android.content.res.Resources;

/**
 *
 * @author gub
 */
public class SpriteController {
    private HashMap<String, Sprite> swOnSprites;
    private HashMap<String, Sprite> swOffSprites;
    private HashMap<String, Sprite> flOnSprites;
    private HashMap<String, Sprite> flOffSprites;
    private HashMap<String, Sprite> vicSprites;
    private Sprite dummy;
    
    public SpriteController(Resources res) {
        swOnSprites = new HashMap<String, Sprite>();
        swOffSprites = new HashMap<String, Sprite>();
        flOnSprites = new HashMap<String, Sprite>();
        flOffSprites = new HashMap<String, Sprite>();
        vicSprites = new HashMap<String, Sprite>();
        
        Options options = new BitmapFactory.Options();
        options.inScaled = false;
                
        swOnSprites.put(Library.TYPE_INPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputswon, options), 30));
        flOffSprites.put(Library.TYPE_INPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputfloff, options), 1));
        flOnSprites.put(Library.TYPE_INPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputflon, options), 1));//40));
        swOffSprites.put(Library.TYPE_INPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputswoff, options), 30));
        
        swOnSprites.put(Library.TYPE_AND, new Sprite(BitmapFactory.decodeResource(res, R.drawable.andswon, options), 16));
        flOffSprites.put(Library.TYPE_AND, new Sprite(BitmapFactory.decodeResource(res, R.drawable.andfloff, options), 1));
        flOnSprites.put(Library.TYPE_AND, new Sprite(BitmapFactory.decodeResource(res, R.drawable.andflon, options), 16));
        swOffSprites.put(Library.TYPE_AND, new Sprite(BitmapFactory.decodeResource(res, R.drawable.andswoff, options), 16));
        
        swOnSprites.put(Library.TYPE_OR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.orswon, options), 30));
        flOffSprites.put(Library.TYPE_OR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.orfloff, options), 1));
        flOnSprites.put(Library.TYPE_OR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.orflon, options), 30));
        swOffSprites.put(Library.TYPE_OR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.orswoff, options), 30));

        swOnSprites.put(Library.TYPE_OUTPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.outswon, options), 30));
        flOffSprites.put(Library.TYPE_OUTPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.outfloff, options), 1));
        flOnSprites.put(Library.TYPE_OUTPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.outflon, options), 1));//40));
        swOffSprites.put(Library.TYPE_OUTPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.outswoff, options), 30));
        vicSprites.put(Library.TYPE_OUTPUT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.outvic, options), 30));
        
        swOnSprites.put(Library.TYPE_MENU, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputswon, options), 30));
        flOffSprites.put(Library.TYPE_MENU, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputfloff, options), 1));
        flOnSprites.put(Library.TYPE_MENU, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputflon, options), 1));//40));
        swOffSprites.put(Library.TYPE_MENU, new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputswoff, options), 30));
        
        swOnSprites.put(Library.TYPE_NOT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.notswon, options), 30));
        flOffSprites.put(Library.TYPE_NOT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.notfloff, options), 1));
        flOnSprites.put(Library.TYPE_NOT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.notflon, options), 1));
        swOffSprites.put(Library.TYPE_NOT, new Sprite(BitmapFactory.decodeResource(res, R.drawable.notswoff, options), 30));
        
        swOnSprites.put(Library.TYPE_XOR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.xorswon, options), 30));
        flOffSprites.put(Library.TYPE_XOR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.xorfloff, options), 1));
        flOnSprites.put(Library.TYPE_XOR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.xorflon, options), 30));
        swOffSprites.put(Library.TYPE_XOR, new Sprite(BitmapFactory.decodeResource(res, R.drawable.xorswoff, options), 30));
        
        swOnSprites.put(Library.TYPE_BOMB, new Sprite(BitmapFactory.decodeResource(res, R.drawable.bombswon, options), 30));
        flOffSprites.put(Library.TYPE_BOMB, new Sprite(BitmapFactory.decodeResource(res, R.drawable.bombfloff, options), 1));
        flOnSprites.put(Library.TYPE_BOMB, new Sprite(BitmapFactory.decodeResource(res, R.drawable.bombflon, options), 1));
        swOffSprites.put(Library.TYPE_BOMB, new Sprite(BitmapFactory.decodeResource(res, R.drawable.bombswoff, options), 1));

        swOnSprites.put(Library.TYPE_FUSE, new Sprite(BitmapFactory.decodeResource(res, R.drawable.fuseswon, options), 30));
        flOffSprites.put(Library.TYPE_FUSE, new Sprite(BitmapFactory.decodeResource(res, R.drawable.fusefloff, options), 1));
        flOnSprites.put(Library.TYPE_FUSE, new Sprite(BitmapFactory.decodeResource(res, R.drawable.fuseflon, options), 1));
        swOffSprites.put(Library.TYPE_FUSE, new Sprite(BitmapFactory.decodeResource(res, R.drawable.fuseswoff, options), 1));
        
        swOnSprites.put(Library.TYPE_FLIP, new Sprite(BitmapFactory.decodeResource(res, R.drawable.flipswon, options), 30));
        flOffSprites.put(Library.TYPE_FLIP, new Sprite(BitmapFactory.decodeResource(res, R.drawable.flipfloff, options), 1));
        flOnSprites.put(Library.TYPE_FLIP, new Sprite(BitmapFactory.decodeResource(res, R.drawable.flipflon, options), 10));
        swOffSprites.put(Library.TYPE_FLIP, new Sprite(BitmapFactory.decodeResource(res, R.drawable.flipswoff, options), 30));
        
        flOnSprites.put(Library.TYPE_HELPER, new Sprite(BitmapFactory.decodeResource(res, R.drawable.helper, options), 60));
        
        dummy = new Sprite(BitmapFactory.decodeResource(res, R.drawable.inputfloff), 1);
    }
    
    public Sprite getGateSprite(String type, Library.Transition transition) {
        Sprite output = null;        
        if (transition.equals(Library.Transition.SWITCHING_OFF)) {
            output = swOffSprites.get(type); 
        }
        if (transition.equals(Library.Transition.SWITCHING_ON)) { 
            output = swOnSprites.get(type); 
        }
        if (transition.equals(Library.Transition.FULL_OFF) || 
                transition.equals(Library.Transition.UNDETERMINED)) { 
            output = flOffSprites.get(type); 
        }
        if (transition.equals(Library.Transition.FULL_ON)) { 
            output = flOnSprites.get(type); 
        }
        if (transition.equals(Library.Transition.VICTORY)) { 
            output = vicSprites.get(type); 
        }
        if (!(output == null)) { return output; }
        return dummy;
    }
    
    public Sprite getGateSprite(Gate g) {
        return getGateSprite(g.getType(), g.getTransition());
    }
    
    public Sprite getHelperSprite() { return flOnSprites.get(Library.TYPE_HELPER); }
    
    public void destroy() {
        for(Sprite sprite : flOnSprites.values()) { sprite.destroy(); }
        for(Sprite sprite : flOffSprites.values()) { sprite.destroy(); }
        for(Sprite sprite : swOnSprites.values()) { sprite.destroy(); }
        for(Sprite sprite : swOffSprites.values()) { sprite.destroy(); }
        dummy.destroy();
    }
}
