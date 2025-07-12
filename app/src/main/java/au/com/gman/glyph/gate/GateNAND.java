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
public class GateNAND extends Gate {
    
    public GateNAND(Element e) throws Exception {
        super(e);
    }
    
    public GateNAND(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_NAND;
    }
    
    public GateNAND(Gate copy) { super(copy); }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = o && io.getState();
        }
        output.setState(!o);
        super.evaluate();
    }
}
