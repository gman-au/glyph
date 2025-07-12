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
import android.view.MotionEvent;
import au.com.gman.glyph.R;
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
public class ScreenMenu extends Screen {
    
    private boolean hasContinue;
        
    private Gate gateNew;
    private Gate gateReset;
    private Gate gateContinue;
    private Gate gateExit;
    private Gate gateTutorial;
    private Gate gateOriginal;
    private Gate gateLocking;
    private Gate gateContinueLaunch;
    private Gate gateResetLaunch;
    private Gate gateTutorialLaunch;
    private Gate gateOriginalLaunch;
    private Gate gateLockingLaunch;
    private Gate gateOptions;
    
    private DialogButton bannerTitle;
    private DialogButton buttonContinue;
    private DialogButton buttonReset;
    private DialogButton buttonNew;
    private DialogButton buttonExit;
    private DialogButton buttonTutorial;
    private DialogButton buttonOriginal;
    private DialogButton buttonLocking;
    private DialogButton buttonContinueLaunch;
    private DialogButton buttonResetLaunch;
    private DialogButton buttonTutorialLaunch;
    private DialogButton buttonOriginalLaunch;
    private DialogButton buttonLockingLaunch;
    private DialogButton buttonOptions;
    
    private List<Gate> gates;
    private List<Link> links;
    
    private List<String> newsFeed;
    private int newsIndex;
    private int tick;
    private ColorX newsTint;
    private boolean newsOn;
    
    private Library.SwitchingContext context = Library.SwitchingContext.DISREGARD_RESOLUTION;
    
    public ScreenMenu(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile,
            boolean xContinue) {
        super(xP, xFont, xSprites, xSounds, xProfile);
        
        tick = 0;
        newsFeed = new ArrayList<String>();
        newsIndex = 0;
        newsOn = false;
        hasContinue = xContinue;
        primary = new ColorX(255, 230, 230, 240);
        secondary = new ColorX(255, 106, 111, 160);
        P.applyColors(primary, secondary);
        
        gates = new ArrayList<Gate>();
        gateContinue = new GateMENU("continue", 141.0f, 48.0f, 0);
        gateReset = new GateMENU("continue", 141.0f, 98.0f, 0);
        gateNew = new GateMENU("new", 141.0f, 148.0f, 0);
        gateOptions = new GateMENU("options", 141.0f, 198.0f, 0);
        gateExit = new GateMENU("exit", 141.0f, 248.0f, 0);
        gateTutorial = new GateMENU("tutorial", 216.0f, 98.0f, 1);
        gateOriginal = new GateMENU("original", 216.0f, 148.0f, 1);
        gateLocking = new GateMENU("locking", 216.0f, 198.0f, 1);
        gateContinueLaunch = new GateMENU("continueLaunch", 241.0f, 73.0f, 1);
        gateResetLaunch = new GateMENU("resetLaunch", 241.0f, 123.0f, 1);
        gateTutorialLaunch = new GateMENU("tutorialLaunch", 416.0f, 123.0f, 1);
        gateOriginalLaunch = new GateMENU("originalLaunch", 416.0f, 173.0f, 1);
        gateLockingLaunch = new GateMENU("lockingLaunch", 416.0f, 223.0f, 1);
        
        gates.add(gateContinue);
        gates.add(gateReset);
        gates.add(gateNew);
        gates.add(gateOptions);
        gates.add(gateExit);
        gates.add(gateTutorial);
        gates.add(gateOriginal);
        gates.add(gateLocking);
        gates.add(gateContinueLaunch);        
        gates.add(gateResetLaunch);        
        gates.add(gateTutorialLaunch);        
        gates.add(gateOriginalLaunch);        
        gates.add(gateLockingLaunch);
        
        bannerTitle = new DialogButton(new RectF(0, -5.0f, Library.REFERENCE_WIDTH, 40.0f), 
                new ColorX(0, 255, 255, 255), new ColorX(0, 255, 255, 255), xP.getLocaleString(au.com.gman.glyph.R.string.app_name), Library.Button.NOTHING, font, 30.0f);
        buttonContinue = new DialogButton(new RectF(gateContinue.getX() - 100, gateContinue.getY(), gateContinue.getX(), gateContinue.getY() + gateContinue.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_continue), Library.Button.NOTHING, font);
        buttonReset = new DialogButton(new RectF(gateReset.getX() - 100, gateReset.getY(), gateReset.getX(), gateReset.getY() + gateReset.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_reset_level), Library.Button.NOTHING, font);
        buttonNew = new DialogButton(new RectF(gateNew.getX() - 100, gateNew.getY(), gateNew.getX(), gateNew.getY() + gateNew.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_new), Library.Button.NOTHING, font);
        buttonOptions = new DialogButton(new RectF(gateOptions.getX() - 100, gateOptions.getY(), gateOptions.getX(), gateOptions.getY() + gateOptions.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_options), Library.Button.NOTHING, font);
        buttonExit = new DialogButton(new RectF(gateExit.getX() - 100, gateExit.getY(), gateExit.getX(), gateExit.getY() + gateExit.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_exit), Library.Button.NOTHING, font);
        buttonTutorial = new DialogButton(new RectF(gateTutorial.getX() + gateTutorial.getWidth(), gateTutorial.getY(), 150 + gateTutorial.getX() + Library.WIDTH, gateTutorial.getY() + gateTutorial.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_tutorial), Library.Button.NOTHING, font);
        buttonOriginal = new DialogButton(new RectF(gateOriginal.getX() + gateOriginal.getWidth(), gateOriginal.getY(), 150 + gateOriginal.getX() + Library.WIDTH, gateOriginal.getY() + gateOriginal.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_original), Library.Button.NOTHING, font);
        buttonLocking = new DialogButton(new RectF(gateLocking.getX() + gateLocking.getWidth(), gateLocking.getY(), 150 + gateLocking.getX() + Library.WIDTH, gateLocking.getY() + gateLocking.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_locking), Library.Button.NOTHING, font);        
        buttonContinueLaunch = new DialogButton(new RectF(gateContinueLaunch.getX() + gateContinueLaunch.getWidth(), gateContinueLaunch.getY(), 80 + gateContinueLaunch.getX() + Library.WIDTH, gateContinueLaunch.getY() + gateContinueLaunch.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_launch), Library.Button.NOTHING, font);
        buttonResetLaunch = new DialogButton(new RectF(gateResetLaunch.getX() + gateResetLaunch.getWidth(), gateResetLaunch.getY(), 80 + gateResetLaunch.getX() + Library.WIDTH, gateResetLaunch.getY() + gateResetLaunch.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_launch), Library.Button.NOTHING, font);
        buttonTutorialLaunch = new DialogButton(new RectF(gateTutorialLaunch.getX() + gateTutorialLaunch.getWidth(), gateTutorialLaunch.getY(), 80 + gateTutorialLaunch.getX() + Library.WIDTH, gateTutorialLaunch.getY() + gateTutorialLaunch.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_launch), Library.Button.NOTHING, font);
        buttonOriginalLaunch = new DialogButton(new RectF(gateOriginalLaunch.getX() + gateOriginalLaunch.getWidth(), gateOriginalLaunch.getY(), 80 + gateOriginalLaunch.getX() + Library.WIDTH, gateOriginalLaunch.getY() + gateOriginalLaunch.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_launch), Library.Button.NOTHING, font);        
        buttonLockingLaunch = new DialogButton(new RectF(gateLockingLaunch.getX() + gateLockingLaunch.getWidth(), gateLockingLaunch.getY(), 80 + gateLockingLaunch.getX() + Library.WIDTH, gateLockingLaunch.getY() + gateLockingLaunch.getHeight()), primary, secondary, P.getLocaleString(R.string.menu_launch), Library.Button.NOTHING, font);
        
        links = new ArrayList<Link>();
        links.add(new Link(1, gateNew, gateOriginal));
        links.add(new Link(1, gateNew, gateLocking));
        links.add(new Link(1, gateNew, gateTutorial));
        links.add(new Link(1, gateContinue, gateContinueLaunch));
        links.add(new Link(1, gateReset, gateResetLaunch));
        links.add(new Link(1, gateTutorial, gateTutorialLaunch));
        links.add(new Link(1, gateOriginal, gateOriginalLaunch));        
        links.add(new Link(1, gateLocking, gateLockingLaunch));
        
        newsTint = new ColorX(255, 255, 255, 255);
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {
            for (Link link : links) {
                    Link.drawLink(c, p, link, Library.Segment.ON_SEGMENT);
            }   
            if (hasContinue) { 
                 sprites.getGateSprite(gateContinue).draw(c, new Point(gateContinue.getXc(), gateContinue.getYc()), gateContinue.getTick(), p, gateContinue.getTransition(), P.getColorFilter());
                 buttonContinue.draw(c, p, buttonContinue.getRect(), gateContinue.getTransition().equals(Library.Transition.FULL_ON));
                 sprites.getGateSprite(gateReset).draw(c, new Point(gateReset.getXc(), gateReset.getYc()), gateReset.getTick(), p, gateReset.getTransition(), P.getColorFilter());
                 buttonReset.draw(c, p, buttonReset.getRect(), gateReset.getTransition().equals(Library.Transition.FULL_ON));
            }           
            if (!(sprites == null)) {
                sprites.getGateSprite(gateNew).draw(c, new Point(gateNew.getXc(), gateNew.getYc()), gateNew.getTick(), p, gateNew.getTransition(), P.getColorFilter());
                buttonNew.draw(c, p, buttonNew.getRect(), gateNew.getTransition().equals(Library.Transition.FULL_ON));
                sprites.getGateSprite(gateExit).draw(c, new Point(gateExit.getXc(), gateExit.getYc()), gateExit.getTick(), p, gateExit.getTransition(), P.getColorFilter());
                buttonExit.draw(c, p, buttonExit.getRect(), gateExit.getTransition().equals(Library.Transition.FULL_ON));
                sprites.getGateSprite(gateOptions).draw(c, new Point(gateOptions.getXc(), gateOptions.getYc()), gateOptions.getTick(), p, gateOptions.getTransition(), P.getColorFilter());
                buttonOptions.draw(c, p, buttonOptions.getRect(), gateOptions.getTransition().equals(Library.Transition.FULL_ON));
                if (Gate.isOn(gateNew)) {
                    // Tutorial                    
                    sprites.getGateSprite(gateTutorial).draw(c, new Point(gateTutorial.getXc(), gateTutorial.getYc()), gateTutorial.getTick(), p, gateTutorial.getTransition(), P.getColorFilter());
                    buttonTutorial.draw(c, p, buttonTutorial.getRect(), gateTutorial.getTransition().equals(Library.Transition.FULL_ON));
                    // Original                    
                    sprites.getGateSprite(gateOriginal).draw(c, new Point(gateOriginal.getXc(), gateOriginal.getYc()), gateOriginal.getTick(), p, gateOriginal.getTransition(), P.getColorFilter());
                    buttonOriginal.draw(c, p, buttonOriginal.getRect(), gateOriginal.getTransition().equals(Library.Transition.FULL_ON));
                    // Locking
                    sprites.getGateSprite(gateLocking).draw(c, new Point(gateLocking.getXc(), gateLocking.getYc()), gateLocking.getTick(), p, gateLocking.getTransition(), P.getColorFilter());
                    buttonLocking.draw(c, p, buttonLocking.getRect(), gateLocking.getTransition().equals(Library.Transition.FULL_ON));
                }
                if (Gate.isOn(gateContinue)) {
                    sprites.getGateSprite(gateContinueLaunch).draw(c, new Point(gateContinueLaunch.getXc(), gateContinueLaunch.getYc()), gateContinueLaunch.getTick(), p, gateContinueLaunch.getTransition(), P.getColorFilter()); 
                    buttonContinueLaunch.draw(c, p, buttonContinueLaunch.getRect(), gateContinueLaunch.getTransition().equals(Library.Transition.FULL_ON));
                }
                if (Gate.isOn(gateReset)) {
                    sprites.getGateSprite(gateResetLaunch).draw(c, new Point(gateResetLaunch.getXc(), gateResetLaunch.getYc()), gateResetLaunch.getTick(), p, gateResetLaunch.getTransition(), P.getColorFilter()); 
                    buttonResetLaunch.draw(c, p, buttonResetLaunch.getRect(), gateResetLaunch.getTransition().equals(Library.Transition.FULL_ON));
                }
                if (Gate.isOn(gateTutorial)) {
                    sprites.getGateSprite(gateTutorialLaunch).draw(c, new Point(gateTutorialLaunch.getXc(), gateTutorialLaunch.getYc()), gateTutorialLaunch.getTick(), p, gateTutorialLaunch.getTransition(), P.getColorFilter()); 
                    buttonTutorialLaunch.draw(c, p, buttonTutorialLaunch.getRect(), gateTutorialLaunch.getTransition().equals(Library.Transition.FULL_ON));                
                }
                if (Gate.isOn(gateOriginal)) {
                    sprites.getGateSprite(gateOriginalLaunch).draw(c, new Point(gateOriginalLaunch.getXc(), gateOriginalLaunch.getYc()), gateOriginalLaunch.getTick(), p, gateOriginalLaunch.getTransition(), P.getColorFilter()); 
                    buttonOriginalLaunch.draw(c, p, buttonOriginalLaunch.getRect(), gateOriginalLaunch.getTransition().equals(Library.Transition.FULL_ON));
                }
                if (Gate.isOn(gateLocking)) {
                    sprites.getGateSprite(gateLockingLaunch).draw(c, new Point(gateLockingLaunch.getXc(), gateLockingLaunch.getYc()), gateLockingLaunch.getTick(), p, gateLockingLaunch.getTransition(), P.getColorFilter());  
                    buttonLockingLaunch.draw(c, p, buttonLockingLaunch.getRect(), gateLockingLaunch.getTransition().equals(Library.Transition.FULL_ON));
                }
            }
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            bannerTitle.draw(c, p, bannerTitle.getRect(), false);        
            if (newsOn) {
                if (!(newsFeed == null)) {
                    p.setTextSize(20.0f);
                    p.setTypeface(font);
                    p.setColor(Color.WHITE);
                    p.setXfermode(null);
                    newsTint.setR(Math.max(255 - (tick * 3), 0));
                    newsTint.setG(Math.max(255 - (tick * 3), 0));
                    newsTint.setB(Math.max(255 - (tick * 3), 0));
                    // Don't flicker the text if only one element in the news feed
                    if (newsFeed.size() <= 1) { newsTint.setR(0); newsTint.setG(0); newsTint.setB(0);}
                    p.setShadowLayer(0.75f, 0.5f, 0.5f, newsTint.getColor());
                    if (newsIndex < newsFeed.size()) { c.drawText(newsFeed.get(newsIndex), 10.0f, Library.REFERENCE_HEIGHT - 10.0f, p); }
                    p.clearShadowLayer();
                }
            }
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
                        if ((!hasContinue) && (gate.equals(gateContinue))) { break; }
                        newsOn = false;
                        gate.Switch(true, context);
                        tick = 0;
                        sounds.playSound(Library.SOUND_BEEP_B);                        
                        if (gate.equals(gateNew)) { gateExit.Switch(false, context); gateContinue.Switch(false, context); gateOptions.Switch(false, context); gateReset.Switch(false, context); }
                        if (gate.equals(gateExit)) { gateNew.Switch(false, context); gateContinue.Switch(false, context); gateOptions.Switch(false, context); gateReset.Switch(false, context); }
                        if (gate.equals(gateContinue)) { gateExit.Switch(false, context); gateNew.Switch(false, context); gateOptions.Switch(false, context); gateReset.Switch(false, context); }
                        if (gate.equals(gateOptions)) { gateExit.Switch(false, context); gateNew.Switch(false, context); gateContinue.Switch(false, context); gateReset.Switch(false, context); }
                        if (gate.equals(gateReset)) { gateExit.Switch(false, context); gateNew.Switch(false, context); gateContinue.Switch(false, context); gateNew.Switch(false, context); }
                        if (gate.equals(gateOriginal)) { gateLocking.Switch(false, context); gateTutorial.Switch(false, context); loadNews(Library.ORIGINAL);}
                        if (gate.equals(gateLocking)) { gateOriginal.Switch(false, context); gateTutorial.Switch(false, context); loadNews(Library.LOCKING);}
                        if (gate.equals(gateTutorial)) { gateOriginal.Switch(false, context); gateLocking.Switch(false, context); loadNews(Library.TUTORIAL);}
                        
                        // Handlers
                        if ((gate.equals(gateExit)) && gateExit.getOutput().getState()) {
                            P.displayDialog(Library.Dialogs.EXIT_TO_HOME);
                        }
                        if ((gate.equals(gateTutorialLaunch)) && gateTutorialLaunch.getOutput().getState()) {
                            P.newGame(Library.Mode.TUTORIAL);
                        }
                        if ((gate.equals(gateOriginalLaunch)) && gateOriginalLaunch.getOutput().getState()) {
                            P.newGame(Library.Mode.ORIGINAL);
                        }
                        if ((gate.equals(gateLockingLaunch)) && gateLockingLaunch.getOutput().getState()) {
                            P.newGame(Library.Mode.LOCKING_PATTERN);
                        }
                        if ((gate.equals(gateContinueLaunch)) && gateContinueLaunch.getOutput().getState()) {
                            P.continueGame();
                        }
                        if ((gate.equals(gateResetLaunch)) && gateResetLaunch.getOutput().getState()) {
                            P.displayDialog(Library.Dialogs.RESET_LEVEL);
                        }
                        if ((gate.equals(gateOptions)) && gateOptions.getOutput().getState()) {
                            P.navigateToOptions();
                        }
                        break;
                    }
                }
            }            
        }
    }
   
    private void loadNews(String mode) {        
        // Title case
        newsOn = true;
        //String title = mode.substring(0, 1).toUpperCase() + mode.substring(1, mode.length());
        String title = P.translateTitle(mode);
        newsFeed.clear(); 
        newsFeed.add(title + " % " + P.getLocaleString(R.string.news_completed) + ": " + String.format("%.0f", (ProfileAccount.getPercentageCompleted(profileAccount, mode) * 100)) + "%");
        if (!(ProfileAccount.getQuickestTime(profileAccount, mode, P) == null)) {
            newsFeed.add(ProfileAccount.getQuickestTime(profileAccount, mode, P));
        }
        if (ProfileAccount.getAverageTime(profileAccount, mode) > 0.0f) {
            newsFeed.add(P.getLocaleString(R.string.news_average_solve_time) + " (" + title + "): " + String.format("%.2f", (ProfileAccount.getAverageTime(profileAccount, mode) / 10000000.0f)) + "s");        
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
        tick++;
        if (tick >= Library.NEWS_PAUSE) {
            tick = 0;
            if (newsIndex >= newsFeed.size() - 1) {
                newsIndex = 0;
            } else {
                newsIndex++;
            }
        }
    }
    
    @Override
    public void destroy() {
        
        gateReset.destroy();
        gateContinue.destroy();
        gateNew.destroy();
        gateOptions.destroy();
        gateExit.destroy();
        gateTutorial.destroy();
        gateOriginal.destroy();
        gateLocking.destroy();        
        gateContinueLaunch.destroy();
        gateResetLaunch.destroy();
        gateTutorialLaunch.destroy();
        gateOriginalLaunch.destroy();
        gateLockingLaunch.destroy();

        bannerTitle.destroy();
        buttonReset.destroy();
        buttonContinue.destroy();
        buttonNew.destroy();
        buttonOptions.destroy();
        buttonExit.destroy();
        buttonTutorial.destroy();
        buttonOriginal.destroy();
        buttonLocking.destroy();
        buttonContinueLaunch.destroy();
        buttonResetLaunch.destroy();
        buttonTutorialLaunch.destroy();
        buttonOriginalLaunch.destroy();
        buttonLockingLaunch.destroy();

        gates.clear();
        gates = null;
        for (Link link : links) { link.destroy(); }
        links.clear();
        links = null;
        
        newsTint = null;
        
        super.destroy();
    }
    
    @Override
    public Level getCurrentLevel() { return null; }
    @Override
    public Chapter getCurrentChapter() { return null; }
}


