/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.gate;

import au.com.gman.glyph.level.Library;
import org.w3c.dom.Element;

/**
 *
 * @author gub
 */
public class GateFUSE extends Gate {
    
    private boolean blown = false;
    public void setBlown(boolean Blown) { blown = Blown; }
    
    public GateFUSE(Element e) throws Exception {
        super(e);
    }
    
    public GateFUSE(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_FUSE;
    }
    
    public GateFUSE(Gate copy) { 
        super(copy); 
        blown = ((GateFUSE)copy).blown;
    }
    
    @Override
    public void setTransition (Library.Transition Transition) {
        super.setTransition(Transition);
    }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = o && io.getState();
        }
        if (o) { blown = true; }
        output.setState(blown);
        super.evaluate();
    }
}
