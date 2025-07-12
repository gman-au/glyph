/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.profile.ProfileAccount;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.GateOUTPUT;
import au.com.gman.glyph.level.Point;

/**
 *
 * @author gub
 */
public class ScreenLocking extends ScreenGameTimed {
    
    private List<GateOUTPUT> locks;
    private int patternsSolved;
    
    public ScreenLocking(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, Library.LOCKING);        
        timeLeft = Library.LOCKING_INITIAL_TIME;
    }
    
    public ScreenLocking(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            int xChapter,
            int xLevel) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile, Library.LOCKING, xChapter, xLevel);
        timeLeft = Library.LOCKING_INITIAL_TIME;
    }
    
    private void initialiseLocks() {
        if (locks == null) { locks = new ArrayList<GateOUTPUT>(); }
        locks.clear();
        for (Gate gate : level.getGates().values()) {
             if (gate.getClass().equals(GateOUTPUT.class)) {
                 locks.add(((GateOUTPUT)gate));
             }
        }
    }
    
    private Boolean randomiseLocks(boolean CanBeAllOff) {
        Random random = new Random();
        boolean allOff = true;
        boolean state = false;
        boolean same = true;
        for (GateOUTPUT gate : locks) {
            state = (random.nextBoolean());
            same = ((state == gate.getLockState()) && (same));
            gate.setLockState(state);
            allOff = (allOff && (!state));
        }
        // If all the gates are off, and we don't want 
        // them to be, return true (this will repeat the parent loop).
        if (allOff && (!CanBeAllOff)) { return true; }
        return same;
    }
    
    @Override
    protected void reset(boolean FullReset) {
        super.reset(FullReset);
        patternsSolved = 0;
        initialiseLocks();
        while(randomiseLocks(false));       
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        super.drawObjects(c, p);
        if (!(sprites == null)) {
            for (GateOUTPUT gate : locks) {
                try {
                    if (gate.getLockState()) {
                        sprites.getHelperSprite().draw(c, new Point(gate.getXc(), gate.getYc()), gate.getTick(), p, gate.getTransition(), PorterDuff.Mode.ADD);
                    }
                }
                catch(Exception e) {}
            }
        }
        
        p.setColor(Color.WHITE);
        p.setXfermode(null);
        p.setShadowLayer(2.0f, 0.0f, 0.0f, Color.BLACK);
        c.drawText(patternsSolved + "/" + Library.LOCKING_PATTERNS_PER_LEVEL,  5.0f, Library.REFERENCE_HEIGHT - 10.0f, p);
        p.clearShadowLayer();
        
    }
    
    @Override
    public void evaluatePuzzle() {
        // Re-assess all states        
        level.unResolve();
        level.reEvaluate();
        
        boolean s = true;
        for (GateOUTPUT gate : locks) {
            s = s && (gate.getOutput().getState() == gate.getLockState());                
        }
        if (s) {
            sounds.playSound(Library.SOUND_BEEP_D);
            // Grant bonus time
            timeLeft += Library.LOCKING_TIME_BONUS;
            patternsSolved++;            
            if (patternsSolved >= Library.LOCKING_PATTERNS_PER_LEVEL) {
                patternsSolved = Library.LOCKING_PATTERNS_PER_LEVEL;
                levelState = Library.LevelState.SOLVED;
                victoryTick = 0;            
                outputsVictory();
            } else {
                // Generate new locking pattern
                while(randomiseLocks(true));
            }
        }
    }
    
    @Override
    protected void outputsVictory() {
        if (!(level == null)) { profileAccount.setLevelPatterns(level, patternsSolved); }
        super.outputsVictory();
    }
    
    @Override
    protected void outputsFailed() {
        if (!(level == null)) { profileAccount.setLevelPatterns(level, patternsSolved); }
        super.outputsFailed();
    }
}
