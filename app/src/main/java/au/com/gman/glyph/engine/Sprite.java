/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import au.com.gman.glyph.level.Point;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.Rect;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Transition;
import android.graphics.PathEffect;

/**
 *
 * @author gub
 */
public class Sprite {
	
    private Bitmap bitmap;		// the animation sequence
    private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
    private Rect destRect;	// the rectangle to be drawn from the animation bitmap
    private int frameCount;		// number of frames in animation
    private int currentFrame;	// the current frame

    private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
    private int spriteHeight;	// the height of the sprite

    public Sprite(Bitmap bitmap, int frames) {
        this.bitmap = bitmap;
        currentFrame = 0;
        frameCount = frames;
        spriteWidth = bitmap.getWidth() / frameCount;
        spriteHeight = bitmap.getHeight();
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        destRect = new Rect(0, 0, spriteWidth, spriteHeight);
    }
       
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t) {
        draw(canvas, l, q, p, t, null, null);
    }
    
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t, ColorMatrixColorFilter cf) {
        draw(canvas, l, q, p, t, null, cf);
    }
    
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t, int haloTick) {
        draw(canvas, l, q, p, t, haloTick, null);
    }
    
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t, int haloTick, ColorMatrixColorFilter cf) {
        draw(canvas, l, q, p, t);
        if (haloTick > 0) {
            p.setColorFilter(null);
            Paint.Style previous = p.getStyle();
            int alpha = p.getAlpha();
            Xfermode prevXfer = p.getXfermode();
            p.setStyle(Paint.Style.STROKE);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            p.setStrokeWidth(Library.HALO_RADIUS);
            p.setAlpha(84);            
            canvas.drawCircle(l.x, l.y, (((float)haloTick / 2.0f) / ((float)Library.VICTORY_PAUSE) * ((float)Library.ORGANIC_RADIUS)), p);
            p.setStyle(previous);
            p.setAlpha(alpha);
            p.setXfermode(prevXfer);
        }
    }
    
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t, PorterDuff.Mode mode) {
        draw(canvas, l, q, p, t, mode, null);
    }
    
    // the draw method which draws the corresponding frame
    public void draw(Canvas canvas, Point l, float q, Paint p, Transition t, PorterDuff.Mode mode, ColorMatrixColorFilter cf) {
            // where to draw the sprite
            if (mode == null) {
                if (t.equals(Transition.FULL_OFF) || t.equals(Transition.UNDETERMINED)) {
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
                } else {
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
                }
            } else {
                p.setXfermode(new PorterDuffXfermode(mode));
            }   
            p.setXfermode(null);
            if (frameCount > 1) { currentFrame = (int)((q * frameCount)); } else { currentFrame = 0; }
            sourceRect.left = currentFrame * spriteWidth;
            sourceRect.right = sourceRect.left + spriteWidth;
            destRect.left = (int)(l.x - (spriteWidth / 2));
            destRect.top = (int)(l.y - (spriteHeight / 2));
            destRect.right = (int)(l.x + (spriteWidth / 2));
            destRect.bottom = (int)(l.y + (spriteHeight / 2));
            if (!(cf == null)) { p.setColorFilter(cf); }
            canvas.drawBitmap(bitmap, sourceRect, destRect, p);                                           
            if (!(cf == null)) { p.setColorFilter(null); }
    }
    
    public void destroy() {
        bitmap.recycle();
        sourceRect = null;
    }
}
