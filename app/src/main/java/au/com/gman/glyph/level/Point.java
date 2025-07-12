/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.level;

/**
 *
 * @author gub
 */
public class Point {
    public float x;
    public float y;
    
    public Point(float X, float Y) {
        x = X;
        y = Y;
    }
    
    public boolean isOn(Point p) {
        return ((p.x == x) && (p.y == y));
    }
    
    public boolean isFloatOn(Point p) {
        int a = (int)p.x;
        int b = (int)p.y;
        int c = (int)x + 1;
        int d = (int)y + 1;
        return (((int)p.x == ((int)x + 1)) && ((int)p.y == ((int)y + 1)));
    }
    
}
