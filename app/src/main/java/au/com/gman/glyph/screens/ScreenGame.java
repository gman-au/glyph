/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Canvas;
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
public class ScreenGame extends Screen {
    
    private List<Chapter> chapters;
    protected int victoryTick;
    
    protected Chapter chapter;
    protected Level level;
    protected LevelState levelState;
    
    protected long nanos;
    protected DialogButton helpButton;
    
    public LevelState getLevelState() { return levelState; }
    public void setLevelState(LevelState LevelState) { levelState = LevelState; }
    
    protected boolean hasTutorial() {
        if (!(level == null)) {
            return (level.getTutorialEvents().size() > 0);
        } else { return false; }
    }
    
    @Override
    public Level getCurrentLevel() { return level; }
    @Override
    public Chapter getCurrentChapter() { return chapter; }
    
    public ScreenGame(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) throws Exception {
        this(xP, xFont, xSprites, xSounds, xProfile, Library.ORIGINAL);
    }
    
    public ScreenGame(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            String xType) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile);
        try {
            chapters = Library.getChaptersFromType(P, xType);
            if (chapters.size() > 0) { chapter = chapters.get(0); }
            if (chapter.getLevels().size() > 0) { level = chapter.getLevels().get(0); }
            reset(false);     
            helpButton = new DialogButton(new RectF(P.getWidth() - (Library.WIDTH + (Library.TEXT_OFFSET / 2)), P.getHeight() - (Library.HEIGHT +(Library.TEXT_OFFSET / 2)), 
                    P.getWidth() - (Library.TEXT_OFFSET / 2), P.getHeight() - (Library.TEXT_OFFSET / 2)), primary, secondary, "?", Library.Button.NEXT_LEVEL, xFont);
        } catch (Exception e) {
            throw(e);
        }
    }
    
    public ScreenGame(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            int xChapter,
            int xLevel) throws Exception {
        this(xP, xFont, xSprites, xSounds, xProfile, Library.ORIGINAL, xChapter, xLevel);
    }
    
    public ScreenGame(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile, 
            String xType,
            int xChapter,
            int xLevel) throws Exception {
        this(xP, xFont, xSprites, xSounds, xProfile, xType);
        try {
            if (chapters.size() >= xChapter) {
                if (chapters.get(xChapter - 1).getLevels().size() >= xLevel) {
                    chapter = chapters.get(xChapter - 1);
                    level = chapter.getLevels().get(xLevel - 1);
                    reset(false);
                }
            }
        } catch (Exception e) {
            throw(e);
        }
    }
    
    public void restartLevel() {
        reset(true);
        level.setNanos(0);
        level.setClicks(0);
        evaluatePuzzle();
    }
    
    protected void reset(boolean FullReset) {
        victoryTick = 0;
        level.unpackIfRequired();
        profileAccount.unlockThisLevel(level);
        levelState = LevelState.UNSOLVED;
        primary = level.getPrimary();
        secondary = level.getSecondary();
        P.applyColors(primary, secondary);
        if (!(helpButton == null)) {
            helpButton.applyNewColours(primary, secondary);
        }
        if (FullReset) { level.restore(); }
        level.unResolve();
        level.reEvaluate();               
    }
    
    public void loadTutorial() {
        if (hasTutorial()) {            
            P.displayTutorial(level.getTutorialGates(), level.getTutorialLinks(), level.getTutorialEvents());
        }
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
                    Screen.drawGates(c, level, p, sprites, victoryTick, P.getColorFilter());
                }
            }
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    protected boolean isGateEligibleToBeTouched(Gate gate) {
        return (!(gate.getTransition().equals(Library.Transition.SWITCHING_OFF) || gate.getTransition().equals(Library.Transition.SWITCHING_ON)));
    }
    
    @Override
    public void handleTouchEvent(MotionEvent event) {
        if (levelState.equals(LevelState.UNSOLVED)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // check if a gate has been clicked
                if (!(level == null)) {
                    for (Gate gate : level.getGates().values()) {
                        if (Library.inBounds(event.getX(), event.getY(), gate)) {
                            if (gate.getClass().equals(GateINPUT.class)) {
                                if (isGateEligibleToBeTouched(gate)) {                                    
                                    gate.Switch(Library.SwitchingContext.REQUIRE_RESOLUTION);
                                    level.setClicks(level.getClicks() + 1);
                                    sounds.playSound(Library.SOUND_BEEP_B);
                                    evaluatePuzzle();
                                    break;
                                }
                            }
                        }
                    }
                    if (hasTutorial()) {
                        if (Library.inBounds(event.getX(), event.getY(), helpButton.getRect())) {
                            loadTutorial();
                        }
                    }
                }
            }
        }
    }
    
    public void evaluatePuzzle() {
        // Re-assess all states        
        level.unResolve();
        level.reEvaluate();
        for (Gate gate : level.getGates().values()) {
            if (gate.getClass().equals(GateBOMB.class)) {
                if (gate.getOutput().getState()) {
                    levelState = LevelState.FAILED;
                    ((GateBOMB)gate).setDetonate(true);
                    sounds.playSound(Library.SOUND_BOMB_EXPLODE);
                    victoryTick = 0;                    
                    return;
                }                
            }
        }
        // Then check outputs
        boolean s = true;
        for (Gate gate : level.getGates().values()) {
            if (gate.getClass().equals(GateOUTPUT.class)) {
                s = s && gate.getOutput().getState();                
            }
        }
        if (s) {
            levelState = LevelState.SOLVED;
            victoryTick = 0;            
            outputsVictory();
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
                        // Having completed this level, we unlock the next one
                        profileAccount.unlockNextLevel(level);    
                        if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_C); }
                        P.displayDialog(Library.Dialogs.LEVEL_SUCCESS);
                    } else {
                        if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_F); }
                        P.displayDialog(Library.Dialogs.LEVEL_FAILURE);
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
    
    protected void outputsVictory() {
        if (!(level == null)) {
            for (Gate gate : level.getGates().values()) {
                if (gate.getClass().equals(GateOUTPUT.class)) {
                    ((GateOUTPUT)(gate)).victory();                    
                }
            }
            sounds.playSound(Library.SOUND_ALERT_E);
            // Transfer stats to profile
            profileAccount.setLevelProperties(level, level.getClicks(), level.getNanos());
        }
    }
    
    @Override
    public void destroy() {        
        if (!(chapters == null))
        {
            for (Chapter c : chapters) {
                c.destroy();
            }
            chapters.clear();
            chapters = null;
        }
        helpButton.destroy();        
        super.destroy();
    }
    
    private boolean endOfChapter() {
        return (chapter.getLevels().indexOf(level) >= chapter.getLevels().size()-1);
    }
    
    private boolean endOfChapters() {
        return (chapters.indexOf(chapter) >= chapters.size() - 1);
    }
    
    public boolean nextLevel() {
        try {
            if (!endOfChapter()) {
                // Pack up the old level
                level.repack();
                level = chapter.getLevels().get(chapter.getLevels().indexOf(level) + 1);
                reset(false);
            } else {
                    if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_A); }
                    P.displayDialog(Library.Dialogs.CHAPTER_SUCCESS);
                    return false;
                }
            } catch (Exception e) {
            }
        return true;
    }
    
    public boolean nextChapter() {
        try {
            if (!endOfChapters()) {
                chapter = chapters.get(chapters.indexOf(chapter) + 1);
                if (chapter.getLevels().size() > 0) {
                    // Pack up the old level
                    level.repack();
                    level = chapter.getLevels().get(0);
                    reset(false);
                }
            } else {
                if (!(sounds == null)) { sounds.playSound(Library.SOUND_ALERT_A); }
                P.displayDialog(Library.Dialogs.GAME_SUCCESS);
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }
    
}
