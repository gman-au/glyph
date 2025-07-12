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
public class GateNOT extends Gate {
    
    public GateNOT(Element e) throws Exception {
        super(e);
    }
    
    public GateNOT(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_NOT;
    }
    
    public GateNOT(Gate copy) { super(copy); }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = !io.getState();
        }
        output.setState(o);
        super.evaluate();
    }
}
