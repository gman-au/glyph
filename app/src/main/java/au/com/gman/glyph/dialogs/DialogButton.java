/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.dialogs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.LinearGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.engine.ColorX;
/*
 *
 * @author gub
 */
public class DialogButton {
    
    protected String t;
    protected ColorX x;
    protected RectF o;
    protected Button b;
    protected boolean locked;
    protected TextPaint tp;
    protected StaticLayout sl;
    
    private LinearGradient g;
    
    public RectF getRect() { return o; }
    public Button getButton() { return b; }
    public String getText() { return t; }
    public boolean getLocked() { return locked; }
    
    public DialogButton(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font) {
        this(R, C1, C2, T, B, font, Library.FONT_SIZE_BUTTON);
    }
    
    public DialogButton(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font, boolean Locked) {
        this(R, C1, C2, T, B, font, Library.FONT_SIZE_BUTTON);
        locked = Locked;
    }
    
    public DialogButton(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font, float textSize ) {
        x = C2;
        b = B;   
        o = R;
        t = T;
        locked = false;
        
        int[] colors = {C1.getColor(), C2.getColor()};
        float[] positions = {0.25f, 0.75f};
        g = new LinearGradient(R.centerX(), R.top, R.centerX(), R.bottom, colors, positions, Shader.TileMode.REPEAT);
        
        tp = new TextPaint(Color.BLACK);
        tp.setColor(Color.WHITE);
        tp.setTextSize(textSize);
        tp.setAntiAlias(true);
        tp.setTypeface(font);
        tp.setShadowLayer(0.75f, 0.5f, 0.5f, Color.BLACK);
        sl = new StaticLayout(t, tp, (int)R.width(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }
    
    public void applyNewColours(ColorX C1, ColorX C2) {
        x = C2;
        int[] colors = {C1.getColor(), C2.getColor()};
        float[] positions = {0.25f, 0.75f};
        g = new LinearGradient(o.centerX(), o.top, o.centerX(), o.bottom, colors, positions, Shader.TileMode.REPEAT);
    }
    
    public void draw(Canvas c, Paint p, RectF r, boolean pressed) {
        p.setColor(Color.WHITE);
        if (!(pressed)) { 
            if (locked) {
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                p.setAlpha(40);
                p.setShader(null);
            } else {
                p.setXfermode(null);
                p.setAlpha(255);
                p.setShader(g);
            }
        } else { 
            p.setColor(x.getColor());  
        }
        c.drawRoundRect(o, 6, 6, p);
        p.setColor(Color.WHITE);
        drawDialogText(c);
        p.setShader(null);
    }
    
    private void drawDialogText(Canvas c) {
        c.save();
        c.clipRect(o);
        c.translate(o.left, o.top + ((o.height() / 2) - (Library.TEXT_OFFSET * 5 / 12)));
        sl.draw(c);
        c.translate(-(o.left), -(o.top + ((o.height() / 2)) - (Library.TEXT_OFFSET * 5 / 12)));
        c.restore();
    }
    
    public void destroy() {
        tp = null;
        sl = null;
    }
    
    public static void drawButton(Canvas c, Paint p, DialogButton button, boolean pressed) {
        if (button.getLocked()) {
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            p.setAlpha(40);
        } else {
            p.setXfermode(null);
            p.setAlpha(255);
        }
        button.draw(c, p, button.getRect(), pressed);
    }
}
