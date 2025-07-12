/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.dialogs;

import android.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.level.Library.Dialogs;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.engine.ColorX;
import java.util.List;
import java.util.ArrayList;
import android.text.TextPaint;
import au.com.gman.glyph.profile.ProfileAccount;
/*
 *
 * @author gub
 */
public class Dialog {
    
    protected RectF rect;
    protected String t;
    protected ColorX c1;
    protected ColorX c2;
    protected List<DialogButton> buttons;
    protected DialogButton pressed;
    protected StaticLayout sl;
    protected TextPaint tp;
    protected Typeface font;
    private Library.Dialogs type;
    private float canvasFade;    
    
    public Library.Dialogs getType() { return type; }
    
    public Dialog(ColorX C1, ColorX C2, String T, RectF R, Typeface Font, Library.Dialogs Type) {
        ColorX CA = Library.getAverageColor(C1, C2);
        type = Type;
        rect = R;        
        c1 = Library.adjustBrightness(CA, -180);
        c2 = Library.adjustBrightness(CA, 60);
        buttons = new ArrayList<DialogButton>();
        pressed = null;
        font = Font;
        canvasFade = 0.0f;
        setText(T);
    }
    
    protected void setText(String T) {
        t = T;
        tp = new TextPaint(Color.BLACK);
        tp.setColor(Color.WHITE);//c2.getColor());
        tp.setTextSize(Library.FONT_SIZE_DIALOG);
        tp.setAntiAlias(true);
        tp.setTypeface(font);
        tp.setShadowLayer(0.75f, 0.5f, 0.5f, Color.BLACK);
        sl = new StaticLayout(t, tp, (int)rect.width() - (Library.TEXT_OFFSET * 2), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }
    
    public void drawDialog(Canvas c, Paint p, SpriteController sc) {
        drawDialog(c, p);
    }
    
    public void drawDialog(Canvas c, Paint p) {        
        // Fade if applicable
        if (canvasFade < 0.98f) { p.setColor(new ColorX((int)((canvasFade) * 172), c1.r(), c1.g(), c1.b()).getColor()); } else { p.setColor(c1.getColor()); p.setAlpha(172);}
        c.drawRect(rect, p);
        if (canvasFade < 0.98f) { p.setColor(new ColorX((int)((canvasFade) * 255), c2.r(), c2.g(), c2.b()).getColor()); } else { p.setColor(c2.getColor()); p.setAlpha(255);}
        p.setTextAlign(Paint.Align.LEFT);
        drawDialogText(c);
        
        for (DialogButton d : buttons) {
            d.draw(c, p, rect, (d.equals(pressed))); 
        }
    }
    
    public void tick() throws Exception {
        if (canvasFade < 0.98f) {            
            canvasFade += 0.05f;
        }
    }
    public boolean nextPage() { return false; }

    protected void drawDialogText(Canvas c) {
        c.save();
        c.clipRect(rect);
        c.translate(rect.left + Library.TEXT_OFFSET, rect.top + Library.TEXT_OFFSET);
        sl.draw(c);
        c.translate(-(rect.left + Library.TEXT_OFFSET), -(rect.top + Library.TEXT_OFFSET));
        c.restore();
    }
    
    public Button handleDialogEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (DialogButton d : buttons) {
                if (Library.inBounds(event.getX(), event.getY(), d.getRect())) {
                    pressed = d;
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
                return pressed.getButton();
            }
        }
        return Button.NOTHING;
    }

    public static Dialog getDialogFromType(Library.Dialogs d, ColorX primary, ColorX secondary, Typeface font, Panel p, Level l, ProfileAccount pA, SpriteController sc) {
        return getDialogFromType(d, primary, secondary, font, p, l, pA, sc, null);
    }
    
    public static Dialog getDialogFromType(Library.Dialogs d, ColorX primary, ColorX secondary, Typeface font, Panel p, Level l, ProfileAccount pA, SpriteController sc, String msg) {
        if (d.equals(Dialogs.LEVEL_SUCCESS)) {
            String Summary = p.getLocaleString(au.com.gman.glyph.R.string.dialog_level_completed);
            if (!(l == null)) {
                Summary += "\r\n";
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_total_clicks) + ": " + l.getClicks() + " ";
                if (l.getClicks() == pA.getLevel(pA, l.getId()).getClicks()) { Summary += "(" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_new_record) + ")"; }
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_total_time) + ": " + String.format("%.2f", ((float)l.getNanos() / 10000000.0f)) + "s ";
                if (l.getNanos() == pA.getLevel(pA, l.getId()).getNanos()) { Summary += "(" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_new_record)+ ")"; }
                Summary += "\r\n";
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_best_clicks) + ": " + pA.getLevel(pA, l.getId()).getClicks();
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_best_time) + ": " + String.format("%.2f", ((float)(pA.getLevel(pA, l.getId()).getNanos() / 10000000.0f))) + "s ";
                if (ProfileAccount.getType(pA, l.getId()).equals(Library.LOCKING)) {
                    Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_avg_time_pattern) + ": " + 
                        String.format("%.2f", ((float)(pA.getLevel(pA, l.getId()).getNanos() / 10000000.0f / (pA.getLevel(pA, l.getId()).getPatterns())))) + "s ";
                }
                return new Dialog_Level_Success(primary, secondary, Summary, Library.getScaledCenteredRect(p, 0.65f, 0.90f), font, d, p); 
            }            
        }
        if (d.equals(Dialogs.CHAPTER_SUCCESS)) {
            if (!(l == null)) {
                String Summary = p.getLocaleString(au.com.gman.glyph.R.string.dialog_chapter_completed);
                Summary += "\r\n";
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_avg_clicks_level) + ": " + String.format("%.2f", (ProfileAccount.getChapter(pA, l.getId()).getAvgClicks()));
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_avg_time_level) + ":  " + String.format("%.2f", ((float)ProfileAccount.getChapter(pA, l.getId()).getAvgNanos() / 10000000.0f)) + "s";
                Summary += "\r\n";
                return new Dialog_Chapter_Success(primary, secondary, Summary, Library.getScaledCenteredRect(p, 0.75f, 0.75f), font, d, p); 
            }
        }
        // requires further development
        if (d.equals(Dialogs.GAME_SUCCESS)) {
            if (!(l == null)) {
                String Summary = p.getLocaleString(au.com.gman.glyph.R.string.dialog_mode_completed);
                Summary += "\r\n";
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_mode_congratulations);
                Summary += "\r\n";
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_avg_time_level) + ": " + String.format("%.2f", (ProfileAccount.getAverageTime(pA, ProfileAccount.getType(pA, l.getId())) / 10000000.0f)) + "s";
                Summary += "\r\n";
                return new Dialog_Game_Success(primary, secondary, Summary, Library.getScaledCenteredRect(p, 0.55f, 0.75f), font, d, p); 
            }
        }        
        if (d.equals(Dialogs.LEVEL_FAILURE)) {
            String Summary = p.getLocaleString(au.com.gman.glyph.R.string.dialog_level_failed);
            Summary += "\r\n";
            Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_total_clicks) + ": " + l.getClicks() + " ";
            Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_total_time) + ": " + String.format("%.2f", ((float)l.getNanos() / 10000000.0f)) + "s ";
            if (l.getNanos() == pA.getLevel(pA, l.getId()).getNanos()) { Summary += "(" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_new_record)+ ")"; }
            Summary += "\r\n";
            Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_best_time) + ": " + String.format("%.2f", ((float)(pA.getLevel(pA, l.getId()).getNanos() / 10000000.0f))) + "s ";
            if (ProfileAccount.getType(pA, l.getId()).equals(Library.LOCKING)) {
                Summary += "\r\n" + p.getLocaleString(au.com.gman.glyph.R.string.dialog_avg_time_pattern) + ": " + 
                        String.format("%.2f", ((float)(pA.getLevel(pA, l.getId()).getNanos() / 10000000.0f / (float)(Math.max(pA.getLevel(pA, l.getId()).getPatterns(), 1))))) + "s ";
            }
            return new Dialog_Level_Failure(primary, secondary, Summary, Library.getScaledCenteredRect(p, 0.65f, 0.85f), font, d, p); 
        }
        
        return getDialogFromType(d, primary, secondary, font, p, l, sc, msg);
    }
    
    public static Dialog getDialogFromType(Library.Dialogs d, ColorX primary, ColorX secondary, Typeface font, Panel p, Level l, SpriteController sc, String msg) {
        if (d.equals(Dialogs.EXIT_TO_MAIN)) {
            return new Dialog_Confirm(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_exit_to_main), Library.getScaledCenteredRect(p, 0.65f, 0.65f), font, d, p);
        }
        if (d.equals(Dialogs.EXIT_TO_MAIN_UNSAVED)) {
            return new Dialog_Confirm(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_exit_to_main_unsaved), Library.getScaledCenteredRect(p, 0.65f, 0.65f), font, d, p);
        }
        if (d.equals(Dialogs.EXIT_TO_HOME)) {
            return new Dialog_Confirm(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_exit_to_home), Library.getScaledCenteredRect(p, 0.65f, 0.65f), font, d, p);
        }
        if (d.equals(Dialogs.EXIT_TO_EDITOR)) {
            return new Dialog_Confirm(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_exit_to_editor), Library.getScaledCenteredRect(p, 0.65f, 0.65f), font, d, p);
        }
        if (d.equals(Dialogs.RESET_PROFILE_CONFIRM)) {
            return new Dialog_Confirm(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_reset_warning), Library.getScaledCenteredRect(p, 0.65f, 0.85f), font, d, p);
        }
        if (d.equals(Dialogs.RESET_LEVEL)) {
            return new Dialog_Reset_Level(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_reset_level), Library.getScaledCenteredRect(p, 0.65f, 0.65f), font, d, p);
        }
        if (d.equals(Dialogs.TOOLBOX)) {
            return new Dialog_ToolBox(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_tool_box), Library.getScaledCenteredRect(p, 0.95f, 0.75f), font, d, p, sc);
        }
        if (d.equals(Dialogs.MESSAGE_ONLY)) {
            return new Dialog_Message_Only(primary, secondary, msg, Library.getScaledCenteredRect(p, 0.65f, 0.50f), font, d, p);
        }
        if (d.equals(Dialogs.TEST_SUCCESS)) {
            return new Dialog_Test_Success(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_test_success), Library.getScaledCenteredRect(p, 0.65f, 0.35f), font, d, p);
        }
        if (d.equals(Dialogs.TEST_FAILURE)) {
            return new Dialog_Test_Failure(primary, secondary, p.getLocaleString(au.com.gman.glyph.R.string.dialog_test_failure), Library.getScaledCenteredRect(p, 0.65f, 0.35f), font, d, p);
        }
        return null;
    }
    
    public void destroy() {
        for (DialogButton d : buttons) {
            d.destroy();
        }
        buttons.clear();
        buttons = null;
        pressed = null;
        rect = null;
        sl = null;
        tp = null;
        font = null;
        c1 = null;
        c2 = null;
    }
}

