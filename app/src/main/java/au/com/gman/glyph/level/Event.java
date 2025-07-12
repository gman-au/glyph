/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.level;
import au.com.gman.glyph.gate.Gate;
import java.util.HashMap;
import org.w3c.dom.Element;
/**
 *
 * @author gub
 */
public class Event {
    
    private int action;
    private Gate gate;
    private String res;
    private boolean wait;
    
    public int getAction() { return action; }
    public Gate getGate() { return gate; }
    public String getRes() { return res; }
    public boolean getWait() { return wait; }
    
    public Event(int Action, Gate G, String Res) {
        this(Action, G, Res, true);
    }
    
    public Event(int Action, Gate G, String Res, boolean Wait) {
        action = Action;
        res = Res;
        gate = G;
        wait = Wait;
    }
    
    public Event(Element e, HashMap<String, Gate> gates) throws Exception {
        try {
            action = Integer.parseInt(e.getAttribute(Library.TUTORIAL_ACTION));
            res = e.getAttribute(Library.TUTORIAL_RES);
            gate = gates.get(e.getAttribute(Library.TUTORIAL_SOURCE));
            wait = e.hasAttribute(Library.TUTORIAL_WAIT);
        } catch (Exception x) {
            throw(x);
        }
    }
    
    public void destroy() {
        gate = null;
        res = null;
    }
}
