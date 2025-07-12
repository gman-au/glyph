/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.provider.CalendarContract;
import au.com.gman.glyph.dialogs.DialogButton;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.profile.ProfileAccount;

/**
 *
 * @author gub
 */
public class ScreenGameTimed extends ScreenGame {
    
    protected long timeLeft;
    private ColorX timeColor;
    
    public ScreenGameTimed(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            String xMode) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, xMode); 
        timeLeft = Library.DEFAULT_INITIAL_TIME;
    }
    
    @Override
    protected void reset(boolean FullReset) {
        super.reset(FullReset);
        levelState = Library.LevelState.UNSOLVED;
        timeColor = new ColorX(255, 255, 255, 255);
    }
    
    public ScreenGameTimed(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            String xMode,
            int xChapter,
            int xLevel) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, xMode, xChapter, xLevel);
        timeLeft = Library.DEFAULT_INITIAL_TIME;
    }
    
    @Override
    public void tickObjects() throws Exception { 
        super.tickObjects();
        if (levelState.equals(Library.LevelState.UNSOLVED)) {
            timeLeft -= nanos / 100;
            if (timeLeft < 0) {
                outputsFailed();
            }
        }
    }
    
    protected void outputsFailed() {
        levelState = Library.LevelState.FAILED;
        sounds.playSound(Library.SOUND_ALERT_F);
        // Transfer stats to profile
        profileAccount.setLevelProperties(level, level.getClicks(), level.getNanos());
        P.displayDialog(Library.Dialogs.LEVEL_FAILURE);
    }
    
    @Override
    protected boolean isGateEligibleToBeTouched(Gate gate) {
        return true;
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        super.drawObjects(c, p);
        try {         
            if (!(level == null)) {
                if (levelState.equals(Library.LevelState.UNSOLVED)) {
                    p.setTextSize(30.0f);
                    p.setTypeface(font);
                    //timeColor.setR(255 - ((int)(((float)timeLeft / 300000000.0f) * 255.0f)));
                    if ((float)timeLeft < Library.WARNING_TIME) {
                        timeColor.setR((((int)(((float)timeLeft / 2500000.0f))) % 2) * 255);
                        timeColor.setG((((int)(((float)timeLeft / 2500000.0f))) % 2) * 255);
                        timeColor.setB((((int)(((float)timeLeft / 2500000.0f))) % 2) * 255);
                    } else {
                        timeColor.setR(255);
                        timeColor.setG(255);
                        timeColor.setB(255);
                    }
                                        
                    p.setColor(timeColor.getColor());
                    p.setXfermode(null);
                    p.setShadowLayer(2.0f, 0.0f, 0.0f, Color.BLACK);
                    c.drawText(String.format("%.2f", ((float)timeLeft / 10000000.0f)), Library.REFERENCE_WIDTH - 100.0f - Library.WIDTH, Library.REFERENCE_HEIGHT - 10.0f, p);
                    p.clearShadowLayer();
                }
            }
        } catch (Exception e) {
            throw(e); 
        }
    }
    
}
