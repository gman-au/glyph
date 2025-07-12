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
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Point;
/*
 *
 * @author gub
 */
public class DialogButtonGate extends DialogButton {
    
    private LinearGradient g;
    private SpriteController sprites;
    private Gate gate;
    
    public Gate getGate() { return gate; }
    
    public DialogButtonGate(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font, SpriteController Sprites, Gate sourceGate) {
        this(R, C1, C2, T, B, font, Library.FONT_SIZE_BUTTON, Sprites, sourceGate);
    }
    
    public DialogButtonGate(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font, boolean Locked, SpriteController Sprites, Gate sourceGate) {
        this(R, C1, C2, T, B, font, Library.FONT_SIZE_BUTTON, Sprites, sourceGate);
        locked = Locked;
    }
    
    public DialogButtonGate(RectF R, ColorX C1, ColorX C2, String T, Button B, Typeface font, float textSize, SpriteController Sprites, Gate sourceGate ) {
        super(R, C1, C2, T, B, font, textSize);        
        sl = null;
        tp.reset();
        tp = null;
        
        sprites = Sprites;
        gate = sourceGate;
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
        sprites.getGateSprite(gate).draw(c, new Point(o.centerX(), o.centerY()), 0.0f, p, Library.Transition.FULL_OFF);
        p.setShader(null);
    }
    
    @Override
    public void destroy() {
        if (!(gate == null)) { gate.destroy(); } 
        gate = null;
        sprites = null;
        super.destroy();
    }
}
