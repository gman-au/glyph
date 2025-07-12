/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.gate;

import au.com.gman.glyph.level.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author gub
 */
public class IO {
    private float x;
    private float y;
    private boolean state;
    private boolean resolved;
    private List<Link> links;
    
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float X) { x = X; }
    public void setY(float Y) { y = Y; }
    public void setResolved(boolean Resolved) { resolved = Resolved; }
    public boolean getState() { return state; }
    public boolean getResolved() { return resolved; }
    public List<Link> getLinks() { return links; }
    
    public void setState(boolean State) {
        state = State;
    }
    
    public IO(float X, float Y) {
        x = X;
        y = Y;
        links = new ArrayList<Link>();
        resolved = false;
    }
    
    public void transmit() {
        for (Link l : links) {
            l.transmit();
        }
    }
    
    public void destroy() {
        if (!(links == null)) {
            links.clear();
            links = null;
        }
    }
}
