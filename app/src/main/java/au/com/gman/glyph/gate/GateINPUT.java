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
public class GateINPUT extends Gate {
    
    boolean switched;
    
    public boolean getSwitched() { return switched; }

    public GateINPUT(Element e) throws Exception {
        super(e);
        switched = false;
    }
    
    public GateINPUT(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_INPUT;
    }
    
    public GateINPUT(Gate copy) { 
        super(copy); 
        switched = ((GateINPUT)copy).switched;
    }
    
    // The switching context is important. For game levels, we don't want
    // to automatically trigger evaluation as the IOs in the whole level
    // must first all be unresolved. For menus and tutorials this is not required.
    @Override
    public void Switch(Library.SwitchingContext context) {
        switched = !switched;
        if (context.equals(Library.SwitchingContext.DISREGARD_RESOLUTION)) {
            evaluate();    
        }
    }
    
    @Override
    public void Switch(boolean state, Library.SwitchingContext context) {
        switched = state;
        if (context.equals(Library.SwitchingContext.DISREGARD_RESOLUTION)) {
            evaluate();
        }
    }
    
    @Override 
    public void evaluate() {
        boolean state = true;
        for(IO io : inputs) {
            state = state && io.getState();
        }
        if (!state) { switched = false; }
        state = state && switched;
        output.setState(state);
        super.evaluate();
    }
    
    public boolean getEnabled() {
        boolean state = true;
        for(IO io : inputs) {
            state = state && io.getState();
        }
        return (state);
    }
}
