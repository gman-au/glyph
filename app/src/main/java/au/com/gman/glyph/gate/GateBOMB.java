/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.gate;

import au.com.gman.glyph.level.Library;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author gub
 */
public class GateBOMB extends Gate {
    
    private boolean detonated;
    
    @Override 
    public boolean getDetonated() { return detonated; }
    public void setDetonate(boolean Detonated) { detonated = Detonated; }
        
    public GateBOMB(Element e) throws Exception {
        super(e);
        detonated = false;
    }
    
    public GateBOMB(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_BOMB;
    }
    
    public GateBOMB(Gate copy) { 
        super(copy); 
        detonated = ((GateBOMB)copy).detonated;
    }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = o && io.getState();
        }
        output.setState(o);
        super.evaluate();
    }
    
    @Override
    public void tickGate() {        
        tick+=tickSpeed;
        propagateDelay();
        if (tick >= Library.TRANSITION_QUOTIENT) {            
            // For bombs, don't reset the tick
            if (transition.equals(Library.Transition.SWITCHING_ON)) { transition = Library.Transition.FULL_ON; }
            if (transition.equals(Library.Transition.SWITCHING_OFF)) { transition = Library.Transition.FULL_OFF; tick = 0; }
        }
    }

    @Override
    protected void layoutIO() {
        initialiseIO();
        float marginH = height / pins / 2;
        float marginW = width / 5;            
        
        for (int i=0; i < inputs.size(); i++) {
            float offset = (float)(Math.sin((((float)i / pins) + (marginH / width)) * Math.PI) * width / 2);
            inputs.get(i).setX((width / 6) + marginW - (offset / 3));
            inputs.get(i).setY((height * i / pins) + marginH);
        }
        output.setX(width - marginW);
        output.setY(height / 2);
    }
}
