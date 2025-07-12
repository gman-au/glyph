 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.provider.CalendarContract;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.dialogs.DialogButton;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.GateMENU;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Point;
import au.com.gman.glyph.profile.ProfileAccount;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gub
 */
public class ScreenOptions extends Screen {
    
    private Gate gateMusic;
    private Gate gateMusicOff;
    private Gate gateMusicOn;
    private Gate gateSounds;
    private Gate gateSoundsOff;
    private Gate gateSoundsOn;
    private Gate gateReset;
    private Gate gateEditor;
    
    private DialogButton bannerTitle;
    private DialogButton buttonMusic;
    private DialogButton buttonMusicOff;
    private DialogButton buttonMusicOn;
    private DialogButton buttonSounds;
    private DialogButton buttonSoundsOff;
    private DialogButton buttonSoundsOn;
    private DialogButton buttonReset;
    private DialogButton buttonEditor;
    
    private List<Gate> gates;
    private List<Link> links;
    
    private Library.SwitchingContext context = Library.SwitchingContext.DISREGARD_RESOLUTION;
    
    public ScreenOptions(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) {
        super(xP, xFont, xSprites, xSounds, xProfile);
        
        primary = new ColorX(255, 230, 240, 230);
        secondary = new ColorX(255, 122, 161, 105);
        P.applyColors(primary, secondary);
        
        gates = new ArrayList<Gate>();
        
        gateMusic = new GateMENU("music", 191.0f, 66.0f, 0);
        gateSounds = new GateMENU("new", 191.0f, 116.0f, 0);
        gateReset = new GateMENU("reset", 191.0f, 166.0f, 0);
        gateMusicOn = new GateMENU("musicOn", 266.0f, 66.0f, 1);
        gateMusicOff = new GateMENU("musicOff", 266.0f, 116.0f, 1);
        gateSoundsOn = new GateMENU("soundsOn", 266.0f, 116.0f, 1);
        gateSoundsOff = new GateMENU("soundsOff", 266.0f, 166.0f, 1);
        gateEditor = new GateMENU("reset", 191.0f, 216.0f, 0);
        
        gates.add(gateMusic);
        gates.add(gateSounds);
        gates.add(gateReset);
        gates.add(gateMusicOn);
        gates.add(gateMusicOff);
        gates.add(gateSoundsOn);
        gates.add(gateSoundsOff);
        gates.add(gateEditor);
        
        bannerTitle = new DialogButton(new RectF(0, -5.0f, Library.REFERENCE_WIDTH, 40.0f), 
                new ColorX(0, 255, 255, 255), new ColorX(0, 255, 255, 255), xP.getLocaleString(au.com.gman.glyph.R.string.title_options), Library.Button.NOTHING, font, 30.0f);
        buttonMusic = new DialogButton(new RectF(gateMusic.getX() - 150, gateMusic.getY(), gateMusic.getX(), gateMusic.getY() + gateMusic.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_music), Library.Button.NOTHING, font);
        buttonSounds = new DialogButton(new RectF(gateSounds.getX() - 150, gateSounds.getY(), gateSounds.getX(), gateSounds.getY() + gateSounds.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_sounds), Library.Button.NOTHING, font);
        buttonReset = new DialogButton(new RectF(gateReset.getX() - 150, gateReset.getY(), gateReset.getX(), gateReset.getY() + gateReset.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_reset), Library.Button.NOTHING, font);
        
        buttonMusicOn = new DialogButton(new RectF(gateMusicOn.getX() + gateMusicOn.getWidth(), gateMusicOn.getY(), 150 + gateMusicOn.getX() + Library.WIDTH, gateMusicOn.getY() + gateMusicOn.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_on), Library.Button.NOTHING, font);
        buttonMusicOff = new DialogButton(new RectF(gateMusicOff.getX() + gateMusicOff.getWidth(), gateMusicOff.getY(), 150 + gateMusicOff.getX() + Library.WIDTH, gateMusicOff.getY() + gateMusicOff.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_off), Library.Button.NOTHING, font);
        
        buttonSoundsOn = new DialogButton(new RectF(gateSoundsOn.getX() + gateSoundsOn.getWidth(), gateSoundsOn.getY(), 150 + gateSoundsOn.getX() + Library.WIDTH, gateSoundsOn.getY() + gateSoundsOn.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_on), Library.Button.NOTHING, font);
        buttonSoundsOff = new DialogButton(new RectF(gateSoundsOff.getX() + gateSoundsOff.getWidth(), gateSoundsOff.getY(), 150 + gateSoundsOff.getX() + Library.WIDTH, gateSoundsOff.getY() + gateSoundsOff.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_off), Library.Button.NOTHING, font);
        
        //buttonReset = new DialogButton(new RectF(gateReset.getX() - 150, gateReset.getY(), gateReset.getX(), gateReset.getY() + gateReset.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_reset), Library.Button.NOTHING, font);
        
        buttonEditor = new DialogButton(new RectF(gateEditor.getX() - 150, gateEditor.getY(), gateEditor.getX(), gateEditor.getY() + gateEditor.getHeight()), primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_editor), Library.Button.NOTHING, font);
        
        links = new ArrayList<Link>();
        links.add(new Link(1, gateMusic, gateMusicOn));
        links.add(new Link(1, gateMusic, gateMusicOff));
        links.add(new Link(1, gateSounds, gateSoundsOn));
        links.add(new Link(1, gateSounds, gateSoundsOff));
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {
            for (Link link : links) {
                Link.drawLink(c, p, link, Library.Segment.ON_SEGMENT);
            }   
            sprites.getGateSprite(gateMusic).draw(c, new Point(gateMusic.getXc(), gateMusic.getYc()), gateMusic.getTick(), p, gateMusic.getTransition(), P.getColorFilter());
            buttonMusic.draw(c, p, buttonMusic.getRect(), gateMusic.getTransition().equals(Library.Transition.FULL_ON));
            sprites.getGateSprite(gateSounds).draw(c, new Point(gateSounds.getXc(), gateSounds.getYc()), gateSounds.getTick(), p, gateSounds.getTransition(), P.getColorFilter());
            buttonSounds.draw(c, p, buttonSounds.getRect(), gateSounds.getTransition().equals(Library.Transition.FULL_ON));
            
            sprites.getGateSprite(gateReset).draw(c, new Point(gateReset.getXc(), gateReset.getYc()), gateReset.getTick(), p, gateReset.getTransition(), P.getColorFilter());
            buttonReset.draw(c, p, buttonReset.getRect(), gateReset.getTransition().equals(Library.Transition.FULL_ON));
            
            sprites.getGateSprite(gateEditor).draw(c, new Point(gateEditor.getXc(), gateEditor.getYc()), gateEditor.getTick(), p, gateEditor.getTransition(), P.getColorFilter());
            buttonEditor.draw(c, p, buttonEditor.getRect(), gateEditor.getTransition().equals(Library.Transition.FULL_ON));
            
            if (Gate.isOn(gateMusic)) {
                sprites.getGateSprite(gateMusicOn).draw(c, new Point(gateMusicOn.getXc(), gateMusicOn.getYc()), gateMusic.getTick(), p, gateMusicOn.getTransition(), P.getColorFilter());
                buttonMusicOn.draw(c, p, buttonMusicOn.getRect(), gateMusicOn.getTransition().equals(Library.Transition.FULL_ON));
                sprites.getGateSprite(gateMusicOff).draw(c, new Point(gateMusicOff.getXc(), gateMusicOff.getYc()), gateMusicOff.getTick(), p, gateMusicOff.getTransition(), P.getColorFilter());
                buttonMusicOff.draw(c, p, buttonMusicOff.getRect(), gateMusicOff.getTransition().equals(Library.Transition.FULL_ON));
            }
            if (Gate.isOn(gateSounds)) {
                sprites.getGateSprite(gateSoundsOn).draw(c, new Point(gateSoundsOn.getXc(), gateSoundsOn.getYc()), gateSoundsOn.getTick(), p, gateSoundsOn.getTransition(), P.getColorFilter());
                buttonSoundsOn.draw(c, p, buttonSoundsOn.getRect(), gateSoundsOn.getTransition().equals(Library.Transition.FULL_ON));
                sprites.getGateSprite(gateSoundsOff).draw(c, new Point(gateSoundsOff.getXc(), gateSoundsOff.getYc()), gateSoundsOff.getTick(), p, gateSoundsOff.getTransition(), P.getColorFilter());
                buttonSoundsOff.draw(c, p, buttonSoundsOff.getRect(), gateSoundsOff.getTransition().equals(Library.Transition.FULL_ON));
            }
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            bannerTitle.draw(c, p, bannerTitle.getRect(), false);        
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    @Override
    public void handleTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        // check if a gate has been clicked
            for (Gate gate : gates) {
                if (Library.inBounds(event.getX(), event.getY(), gate)) {
                    if (!(gate.getTransition().equals(Library.Transition.SWITCHING_OFF) || gate.getTransition().equals(Library.Transition.SWITCHING_ON))) {
                        gate.Switch(true, context);
                        sounds.playSound(Library.SOUND_BEEP_B);                        
                        if (gate.equals(gateMusic)) { gateReset.Switch(false, context); gateSounds.Switch(false, context);}
                        if (gate.equals(gateReset)) { gateMusic.Switch(false, context); gateSounds.Switch(false, context);}
                        if (gate.equals(gateSounds)) { gateReset.Switch(false, context); gateMusic.Switch(false, context);}
                        if (gate.equals(gateMusicOn)) { gateMusicOff.Switch(false, context);}
                        if (gate.equals(gateMusicOff)) { gateMusicOn.Switch(false, context);}
                        if (gate.equals(gateSoundsOn)) { gateSoundsOff.Switch(false, context);}
                        if (gate.equals(gateSoundsOff)) { gateSoundsOn.Switch(false, context);}
                        
                        // Handlers
                        if ((gate.equals(gateMusicOn)) && gateMusicOn.getOutput().getState() && gateMusic.getOutput().getState()) {
                            P.setMusicEnabled(true);
                        }
                        if ((gate.equals(gateMusicOff)) && gateMusicOff.getOutput().getState() && gateMusic.getOutput().getState()) {
                            P.setMusicEnabled(false);
                        }
                        if ((gate.equals(gateSoundsOn)) && gateSoundsOn.getOutput().getState() && gateSounds.getOutput().getState()) {
                            P.setSoundsEnabled(true);
                        }
                        if ((gate.equals(gateSoundsOff)) && gateSoundsOff.getOutput().getState() && gateSounds.getOutput().getState()) {
                            P.setSoundsEnabled(false);
                        }
                        if ((gate.equals(gateReset)) && gateReset.getOutput().getState()) {
                            P.displayDialog(Library.Dialogs.RESET_PROFILE_CONFIRM);
                        }
                        if ((gate.equals(gateEditor)) && gateEditor.getOutput().getState()) {
                            P.launchEditor();
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void tickObjects() throws Exception { 
        for(Gate gate : gates) {
            gate.tickGate();
        }
        for(Link link: links) {
            link.tickLink();
        }
    }
    
    @Override
    public void destroy() {
        
        gateMusic.destroy();
        gateMusicOn.destroy();
        gateMusicOff.destroy();
        gateSounds.destroy();
        gateSoundsOn.destroy();
        gateSoundsOff.destroy();
        gateReset.destroy();

        bannerTitle.destroy();
        buttonMusic.destroy();
        buttonMusicOn.destroy();
        buttonMusicOff.destroy();
        buttonSounds.destroy();
        buttonSoundsOn.destroy();
        buttonSoundsOff.destroy();
        buttonReset.destroy();

        gates.clear();
        gates = null;
        for (Link link : links) { link.destroy(); }
        links.clear();
        links = null;
        
        super.destroy();
    }
    
    @Override
    public Level getCurrentLevel() { return null; }
    @Override
    public Chapter getCurrentChapter() { return null; }
}


