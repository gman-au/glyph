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
public class GateOUTPUT extends Gate {
    
    private boolean lockState;
    
    public boolean getLockState() { return lockState; }
    public void setLockState(boolean LockState) { lockState = LockState; }
    
    public GateOUTPUT(Element e) throws Exception {
        super(e);
        lockState = false;
    }
    
    public GateOUTPUT(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_OUTPUT;
        lockState = false;
    }
    
    public GateOUTPUT(Gate copy) { 
        super(copy); 
        lockState = ((GateOUTPUT)copy).lockState;
    }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = o && io.getState();
        }
        output.setState(o);
    }
    
    public void victory() {
        transition = Library.Transition.VICTORY;
    }
    
        @Override
    protected void layoutIO() {
        initialiseIO();
        
        for (int i=0; i < inputs.size(); i++) {
            inputs.get(i).setX((width / 2));
            inputs.get(i).setY((height / 2));
        }
        output.setX(width / 2);
        output.setY(height / 2);
    }
}
