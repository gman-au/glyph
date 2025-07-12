/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import android.graphics.Color;

/**
 *
 * @author gub
 */
public class ColorX {
    private int r;
    private int g;
    private int b;
    private int a;
    
    public int r() {return r;}
    public int g() {return g;}    
    public int b() {return b;}
    public int a() {return a;}
    
    public ColorX(int A, int R, int G, int B) {
        a = A;
        r = R;
        g = G;
        b = B;
    }
    
    public int getColor() { return Color.argb(a,r,g,b); }
    public void setA(int A) { a = Math.max(0, (Math.min(255, A))); }
    public void setR(int R) { r = Math.max(0, (Math.min(255, R))); }
    public void setG(int G) { g = Math.max(0, (Math.min(255, G))); }
    public void setB(int B) { b = Math.max(0, (Math.min(255, B))); }
    
}
