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
import au.com.gman.glyph.level.Point;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.dialogs.DialogButton;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.profile.ProfileAccount;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gub
 */
public class ScreenLevelSelect extends Screen {
    
    protected DialogButton bannerTitle;
    protected DialogButton pressed;
    protected DialogButton launch;
    private String type;
    private int chapter;
    protected float vertRef;
    protected float vertMove;
    protected float vertSpeed;
    protected float vertPoint;
    protected float vertBottomEdge;
    
    protected Level selectedLevel;
    protected int selectedLevelIndex;
    
    protected List<DialogButton> buttons;
    protected Point scalePoint;
    protected Point translatePoint;
    protected float buttonWidth;
    
    public ScreenLevelSelect(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) {
        super(xP, xFont, xSprites, xSounds, xProfile);
    }
    
    public ScreenLevelSelect(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile,
            String xType,
            int xChapter) {
        super(xP, xFont, xSprites, xSounds, xProfile);
        initialiseScreen();
        scalePoint = new Point(0.75f, 0.75f);
        translatePoint = new Point(140.0f, 50.0f);
        type = xType;
        chapter = xChapter - 1;
        buttonWidth = 100.0f;
        
        int columns = (int)(Library.scaleX(xP, Library.REFERENCE_WIDTH) / ( Library.scaleX(xP, 120)));
        columns = 1;
        for (int rows = 0; rows < profileAccount.getChapters(xType).get(chapter).getLevels().size(); rows++) { 
            if ((((rows)*(columns))) >= profileAccount.getChapters(xType).get(chapter).getLevels().size()) { break; }
            for (int cols = 0; cols < columns; cols++) {
                if ((((rows)*(columns) + (cols))) >= profileAccount.getChapters(xType).get(chapter).getLevels().size()) { break; }
                String caption;
                if (profileAccount.getChapters(xType).get(chapter).getLevels().get((rows)*(columns) + (cols)).getLocked()) { 
                    caption = xP.getLocaleString(au.com.gman.glyph.R.string.button_locked); 
                } else { 
                    caption = xP.getLocaleString(au.com.gman.glyph.R.string.level_prefix) + " " + ((rows)*(columns) + (cols) + 1); 
                }
                buttons.add(new DialogButton(new RectF(20, 55 + (rows*50), 20 + buttonWidth, 90 + (rows*50)), primary, secondary, 
                        caption, Library.Button.NOTHING, font, profileAccount.getChapters(xType).get(chapter).getLevels().get((rows)*(columns) + (cols)).getLocked()));   
                vertBottomEdge = 55 + (rows*50);
            }
        }
        bannerTitle = new DialogButton(new RectF(0, -5.0f, Library.REFERENCE_WIDTH, 40.0f), 
                new ColorX(0, 255, 255, 255), new ColorX(0, 255, 255, 255), xP.getLocaleString(au.com.gman.glyph.R.string.title_select_level), Library.Button.NOTHING, font, 30.0f);
        launch = new DialogButton(new RectF(440.0f, 280.0f, 520.0f, 310.0f), 
                primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_launch), Library.Button.NOTHING, font);
    }
    
    protected void initialiseScreen() {
        selectedLevel = null;
        vertRef = 0.0f;
        vertMove = 0.0f;
        vertSpeed = 0.0f;
        vertPoint = 0.0f;
        vertBottomEdge = 0.0f;
        pressed = null;
        primary = new ColorX(255, 245, 243, 177);
        secondary = new ColorX(255, 132, 150, 120);
        P.applyColors(primary, secondary);
        buttons = new ArrayList<DialogButton>();        
        selectedLevelIndex = 0;        
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {
            bannerTitle.draw(c, p, bannerTitle.getRect(), false);  
            if (!(selectedLevel == null)) { 
                launch.draw(c, p, launch.getRect(), launch.equals(pressed)); 
                if (!(selectedLevel.getAuthor() == null)) {
                    p.setShadowLayer(0.75f, 0.5f, 0.5f, Color.BLACK);
                    p.setTextSize(17.5f);
                    c.drawText(P.getLocaleString(au.com.gman.glyph.R.string.level_author) + ": " + selectedLevel.getAuthor(),
                            buttonWidth + Library.WIDTH, Library.REFERENCE_HEIGHT - 10.0f, p); 
                    p.clearShadowLayer();
                }
            }
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            if (!(selectedLevel == null)) {
                c.translate(translatePoint.x, translatePoint.y);
                c.scale(scalePoint.x, scalePoint.y);
                p.setColor(Color.BLACK);
                Screen.drawLinks(c, selectedLevel, p);                
                Screen.drawGates(c, selectedLevel, p, sprites, 0, P.getColorFilter());                
                c.scale((1 / scalePoint.x), (1 / scalePoint.y));
                c.translate(-translatePoint.x, -translatePoint.y);
            }
            c.translate(0.0f, -(vertMove));
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            for (DialogButton button : buttons) {
                DialogButton.drawButton(c, p, button, button.equals(pressed));
            }   
            c.translate(0.0f, (vertMove));
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    @Override
    public void tickObjects() { 
        if (vertSpeed > 0) { vertSpeed = Math.max(0.0f, vertSpeed - 0.025f); }
        if (vertSpeed < 0) { vertSpeed = Math.min(0.0f, vertSpeed + 0.025f); }
        vertMove += vertSpeed;
        vertMove = Math.min(Math.max(vertMove, Library.SWIPE_SCREEN_TOP_LIMIT), Library.SWIPE_SCREEN_BOTTOM_LIMIT);     
    }
    
    @Override
    public void handleTouchEvent(MotionEvent event) {
        float vertFinal = event.getY() + vertMove;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // check if a button has been clicked
            for (DialogButton button : buttons) {
                if (Library.inBounds(event.getX(), vertFinal, button.getRect())) {
                    if (!(button.getLocked())) { pressed = button; }
                    break;
                }
            }
            if (Library.inBounds(event.getX(), event.getY(), launch.getRect())) { pressed = launch; }
            // Otherwise get the swipe point
            if (vertRef == 0) { vertRef = event.getY(); } else { vertRef = vertMove + event.getY(); }
            vertSpeed = 0.0f;
            vertPoint = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pressed == null) {
                vertMove = vertRef - event.getY();
                vertMove = Math.min(Math.max(vertMove, Library.SWIPE_SCREEN_TOP_LIMIT), vertBottomEdge); //Library.SWIPE_SCREEN_BOTTOM_LIMIT);                
            }
            if (!(pressed == null)) {
                if (!(Library.inBounds(event.getX(), vertFinal, pressed.getRect()))) {
                    pressed = null;
                }
            }            
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!(pressed == null)) {
                if (!pressed.equals(launch)) {
                    // Preview level
                    selectedLevelIndex = previewLevel(pressed);
                    pressed = null;
                } else {
                    // Navigate to level 
                    navigateLevel(selectedLevelIndex);
                    pressed = null;
                }
            } else {                
                //if (vertPoint > event.getY()) { vertSpeed = 2.0f; } else { vertSpeed = -2.0f; }
            }
        }        
    }
    
    public void navigateBack() {
        if (type.equals(Library.TUTORIAL)) { P.toMain(); }
        else { P.navigateToChapterSelect(type); }
    }
    
    protected int previewLevel(DialogButton d) {
        try {
            int level = Integer.parseInt(d.getText().replace(P.getLocaleString(au.com.gman.glyph.R.string.level_prefix) + " ", "")) - 1;
            //P.loadLevel(type, chapter + 1, level);
            if (!(selectedLevel == null)) { selectedLevel.repack(); }
            Chapter selectedChapter;
            List<Chapter> chapters = Library.getChaptersFromType(P, type);
            if (chapters.size() > chapter) {
                selectedChapter = chapters.get(chapter); 
                if (selectedChapter.getLevels().size() > level) {
                    selectedLevel = selectedChapter.getLevels().get(level);
                    selectedLevel.unpackIfRequired();
                } 
            }
            sounds.playSound(Library.SOUND_BEEP_I);
            return level;
        } catch (Exception e) {
            P.exceptionDialog(e);
        }
        return -1;        
    }
    
    protected void navigateLevel(int level) {
        try {
            if (level >= 0) {
                P.loadLevel(type, chapter + 1, level + 1);
            }
        } catch (Exception e) {
            P.exceptionDialog(e);
        }
    }
    
    @Override
    public void destroy() {
        if (!(bannerTitle == null)) { bannerTitle.destroy(); }
        bannerTitle = null;
        if (!(buttons == null)) {
            for (DialogButton button : buttons) {
                button.destroy();
            }
            buttons.clear();
        }
        buttons = null;        
        super.destroy();
    }
    
    @Override
    public Level getCurrentLevel() { return null; }
    @Override
    public Chapter getCurrentChapter() { return null; }
}


