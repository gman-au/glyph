/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.OrganicBackground;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.level.Level;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.view.MotionEvent;
import au.com.gman.glyph.profile.ProfileAccount;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.GateNOT;
import au.com.gman.glyph.gate.GateINPUT;
import au.com.gman.glyph.gate.GateOUTPUT;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Point;
import java.util.Collection;
/**
 *
 * @author gub
 */
public abstract class Screen {
    
    abstract public void tickObjects() throws Exception;
    abstract public void drawObjects (Canvas c, Paint p) throws Exception;
    abstract public void handleTouchEvent(MotionEvent event);
    protected Panel P;
    protected SpriteController sprites;
    protected SoundController sounds;
    protected ColorX primary;
    protected ColorX secondary;
    protected Typeface font;
    protected ProfileAccount profileAccount;
    
    public Screen(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) {
        P = xP;
        font = xFont;
        sprites = xSprites;
        sounds = xSounds;
        profileAccount = xProfile;        
    }
    
    public static void drawOrganicBackground(Canvas c, OrganicBackground ob, Panel P) {
        if (!(ob == null)) {
            ob.draw(c, P.getWidth(), P.getHeight());
        }
    }
    
    public static void drawLinks(Canvas c, Level l, Paint p) {
        drawLinks(c, l.getLinks().values(), p);        
    }
    
    public static void drawGates(Canvas c, Level l, Paint p, SpriteController sc) {
        drawGates(c, l.getGates().values(), p, sc);
    }
    
    public static void drawGates(Canvas c, Level l, Paint p, SpriteController sc, int victoryTick) {
        drawGates(c, l, p, sc, victoryTick, null);
    }
    
    public static void drawGates(Canvas c, Level l, Paint p, SpriteController sc, int victoryTick, ColorMatrixColorFilter cf) {
        drawGates(c, l.getGates().values(), p, sc, victoryTick, cf);
    }
    
    public static void drawLinks(Canvas c, Collection<Link> l, Paint p) {
        if (!(l == null)) {
            for (Link link : l) {
                try {
                    Link.drawLink(c, p, link);
                }
                catch(Exception e) {}
            }
        }
    }
    
    public static void drawGates(Canvas c, Collection<Gate> g, Paint p, SpriteController sc) {
        drawGates(c, g, p, sc, 0);
    }
    
    public static void drawGates(Canvas c, Collection<Gate> g, Paint p, SpriteController sc, int victoryTick) {
        drawGates(c, g, p, sc, victoryTick, null);
    }
    
    public static void drawGates(Canvas c, Collection<Gate> g, Paint p, SpriteController sc, int victoryTick, ColorMatrixColorFilter cf) {
        if (!(g == null)) {
            if (!(sc == null)) {
                for (Gate gate : g) {
                    try {
                        if (((gate.getTransition().equals(Library.Transition.VICTORY))) || 
                                ((gate.getDetonated() && (victoryTick > 0)))) {                            
                            sc.getGateSprite(gate).draw(c, new Point(gate.getXc(), gate.getYc()), gate.getTick(), p, gate.getTransition(), victoryTick, cf);
                        } else {
                            sc.getGateSprite(gate).draw(c, new Point(gate.getXc(), gate.getYc()), gate.getTick(), p, gate.getTransition(), cf);
                        }
                        if (gate.getHelper()) {
                            if (gate.getClass().equals(GateINPUT.class)) {
                                if ((((GateINPUT)gate).getEnabled()) && (!((GateINPUT)gate).getSwitched())) {
                                    sc.getHelperSprite().draw(c, new Point(gate.getXc(), gate.getYc()), gate.getTick(), p, gate.getTransition(), PorterDuff.Mode.ADD, cf);
                                }
                            } else {
                                sc.getHelperSprite().draw(c, new Point(gate.getXc(), gate.getYc()), gate.getTick(), p, gate.getTransition(), PorterDuff.Mode.ADD, cf);
                            }
                        }
                    }
                    catch(Exception e) {}
                }
            }
        }
    }
    
    public void destroy() { 
        P = null;
        font = null;
        sprites = null;
        sounds = null;
        profileAccount = null;
    }
    
    public abstract Level getCurrentLevel();
    public abstract Chapter getCurrentChapter();
}
