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
import android.view.MotionEvent;
import au.com.gman.glyph.R;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.GateINPUT;
import au.com.gman.glyph.gate.GateOUTPUT;
import au.com.gman.glyph.gate.GateBOMB;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.LevelState;
import au.com.gman.glyph.dialogs.DialogButton;
import au.com.gman.glyph.profile.ProfileAccount;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gub
 */
public class ScreenGameTestBed extends ScreenGame {
    
    public ScreenGameTestBed(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile,
            Level testLevel) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile);
        level = testLevel;
        reset(false);
    }
    
    @Override
    protected void reset(boolean FullReset) {
        victoryTick = 0;
        level.unpackIfRequired();
        levelState = LevelState.UNSOLVED;
        primary = level.getPrimary();
        secondary = level.getSecondary();
        P.applyColors(primary, secondary);
        if (!(helpButton == null)) {
            helpButton.applyNewColours(primary, secondary);
        }
        if (FullReset) { level.restore(); }
        level.reEvaluate();               
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {         
            if (!(level == null)) {
                if (!(levelState.equals(LevelState.TUTORIAL))) {
                    if (hasTutorial()) {
                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
                        helpButton.draw(c, p, helpButton.getRect(), false);
                    }
                    Screen.drawLinks(c, level, p);
                    Screen.drawGates(c, level, p, sprites, victoryTick);
                }
                p.setXfermode(null);
                p.setTextSize(17.5f);
                p.setTypeface(font);
                p.setColor(Color.BLACK);
                c.drawText(P.getLocaleString(au.com.gman.glyph.R.string.editor_mode_testing), Library.WIDTH / 2, Library.REFERENCE_HEIGHT - (Library.HEIGHT / 2), p);
            }
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    @Override
    public void tickObjects() throws Exception { 
        if (!(level == null)) {
            for (Gate gate : level.getGates().values()) {  
                gate.tickGate();
            }
            for (Link link : level.getLinks().values()) {
                link.tickLink();
            }                    
        }
        if ((levelState.equals(LevelState.SOLVED)) || (levelState.equals(LevelState.FAILED))) {
            victoryTick++;
            if (victoryTick >= Library.VICTORY_PAUSE) {
                if (!(P == null)) { 
                    if (levelState.equals(LevelState.SOLVED)) {
                        if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_C); }
                        P.displayDialog(Library.Dialogs.TEST_SUCCESS);
                    } else {
                        if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_F); }
                        P.displayDialog(Library.Dialogs.TEST_FAILURE);
                    }
                    levelState = LevelState.SUMMARY;
                }
            } else {
            }
        }
        if (levelState.equals(LevelState.UNSOLVED)) {
            // add to elapsed time
            nanos = P.getScreenNanos();
            level.setNanos(level.getNanos() + (nanos / 100));
        }
    }
    
    @Override
    protected void outputsVictory() {
        if (!(level == null)) {
            for (Gate gate : level.getGates().values()) {
                if (gate.getClass().equals(GateOUTPUT.class)) {
                    ((GateOUTPUT)(gate)).victory();                    
                }
            }
            sounds.playSound(Library.SOUND_ALERT_E);
        }
    }
    
    @Override
    public void destroy() {        
        helpButton.destroy();
        super.destroy();
    }
}
