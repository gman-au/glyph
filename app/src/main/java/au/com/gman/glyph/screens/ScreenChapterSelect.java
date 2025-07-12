/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.view.MotionEvent;
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
public class ScreenChapterSelect extends Screen {
    
    private DialogButton bannerTitle;
    private DialogButton pressed;
    private String type;
    
    private List<DialogButton> buttons;
    
    public ScreenChapterSelect(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile,
            String xType) {
        super(xP, xFont, xSprites, xSounds, xProfile);
        pressed = null;
        primary = new ColorX(255, 230, 230, 240);
        secondary = new ColorX(255, 65, 143, 129);
        P.applyColors(primary, secondary);
        buttons = new ArrayList<DialogButton>();
        type = xType;
        
        int columns = (int)(Library.scaleX(xP, Library.REFERENCE_WIDTH) / ( Library.scaleX(xP, 120)));
        for (int rows = 0; rows < 10; rows++) {
            if ((((rows)*(columns))) >= profileAccount.getChapters(xType).size()) { break; }
            for (int cols = 0; cols < columns; cols++) {
                if ((((rows)*(columns) + (cols))) >= profileAccount.getChapters(xType).size()) { break; }
                String caption;
                if (profileAccount.getChapters(xType).get((rows)*(columns) + (cols)).getLocked()) { 
                    caption = xP.getLocaleString(au.com.gman.glyph.R.string.button_locked); 
                } else { 
                    caption = xP.getLocaleString(au.com.gman.glyph.R.string.chapter_prefix) + " " + ((rows)*(columns) + (cols) + 1); 
                }
                buttons.add(new DialogButton(new RectF(20 + ((cols)*120), 55 + (rows*50), 120 + ((cols)*120), 90 + (rows*50)), primary, secondary, 
                        caption, Library.Button.NOTHING, font, profileAccount.getChapters(xType).get((rows)*(columns) + (cols)).getLocked()));                
            }
        }
        bannerTitle = new DialogButton(new RectF(0, -5.0f, Library.REFERENCE_WIDTH, 40.0f), 
                new ColorX(0, 255, 255, 255), new ColorX(0, 255, 255, 255), xP.getLocaleString(au.com.gman.glyph.R.string.title_select_chapter), Library.Button.NOTHING, font, 30.0f);
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {            
            for (DialogButton button : buttons) {
                DialogButton.drawButton(c, p, button, button.equals(pressed));
            }   
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            bannerTitle.draw(c, p, bannerTitle.getRect(), false);        
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    @Override
    public void tickObjects() {}
    
    @Override
    public void handleTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        // check if a gate has been clicked
            for (DialogButton button : buttons) {
                if (Library.inBounds(event.getX(), event.getY(), button.getRect())) {
                    if (!(button.getLocked())) { pressed = button; }
                    break;
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (!(pressed == null)) {
                if (!(Library.inBounds(event.getX(), event.getY(), pressed.getRect()))) {
                    pressed = null;
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!(pressed == null)) {
                // Navigate to level select
                navigateToLevelSelect(pressed);
            }
        }
    }
    
    private void navigateToLevelSelect(DialogButton d) {
        try {
            int chapter = Integer.parseInt(d.getText().replace(P.getLocaleString(au.com.gman.glyph.R.string.chapter_prefix) + " ", ""));
            P.navigateToLevelSelect(type, chapter);
        } catch (Exception e) { }
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


