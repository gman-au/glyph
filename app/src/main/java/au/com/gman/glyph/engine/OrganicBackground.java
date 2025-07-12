/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import au.com.gman.glyph.level.Point;
import au.com.gman.glyph.level.Library;
import android.graphics.RadialGradient;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
/**
 *
 * @author gub
 */
public class OrganicBackground {
    
    private RadialGradient g;
    private Paint p;
    private Matrix m;
    private float a;
    
    public OrganicBackground(ColorX cx1, ColorX cx2, Point size) {
        a = 0.0f;
        p = new Paint();
        m = new Matrix();
        
        ColorX cxa = Library.getAverageColor(cx1, cx2);
        
        int[] cols = new int[3];
        float[] pos = new float[3];
        cols[0] = cx1.getColor();
        cols[1] = Library.adjustBrightness(cxa, -60).getColor();
        cols[2] = cx2.getColor();
        pos[0] = 0.1f;
        pos[1] = 0.4f;
        pos[2] = 0.9f;
        
        g = new RadialGradient(size.x * 2, size.y / 2, Library.ORGANIC_RADIUS, cols, pos, Shader.TileMode.MIRROR);
        p.setShader(g);
    }

    public void tick() {
        a += Library.BACKGROUND_ANGULAR_SPEED;
        m.setRotate(a);
        g.setLocalMatrix(m);
        p.setShader(g);
    }
    
    public void draw(Canvas c, int width, int height) {
        if (!(p == null)) {
            c.drawRect(0, 0, width, height, p);
        }
    }
    
    public void destroy() {
        g = null;
        p = null;
        m = null;
    }
}
