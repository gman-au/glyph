/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Paint;
import android.graphics.Typeface;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.profile.ProfileAccount;

/**
 *
 * @author gub
 */
public class ScreenTutorial extends ScreenGame {
    
    public ScreenTutorial(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, Library.TUTORIAL);
    }
    
    public ScreenTutorial(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            int xChapter,
            int xLevel) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, Library.TUTORIAL, xChapter, xLevel);
    }
}
