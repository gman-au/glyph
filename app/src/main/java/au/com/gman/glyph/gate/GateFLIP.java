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
public class GateFLIP extends Gate {
    
    private boolean[] priorStates;
    
    public GateFLIP(Element e) throws Exception {
        super(e);
        priorStates = new boolean[pins];
        restoreIOArray();
    }
    
    public GateFLIP(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_FLIP;
        priorStates = new boolean[pins];
        restoreIOArray();
    }
    
    public GateFLIP(Gate copy) { 
        super(copy); 
        priorStates = new boolean[pins];
        restoreIOArray();
    }
    
    @Override
    public void rebuildIO() {
        priorStates = new boolean[pins];
        restoreIOArray();
        super.rebuildIO();
    }
    
    private void restoreIOArray() {
        if (!(inputs == null)) {
            for (int i=0; ((i < priorStates.length) && (i < inputs.size())); i++) {
                priorStates[i] = inputs.get(i).getState();
            }
        }
    }
    
    private boolean compareToPrior()
    {
        boolean match = true;
        for (int i=0; ((i < priorStates.length) && (i < inputs.size())); i++) {
            match = match && (inputs.get(i).getState() == priorStates[i]);
        }
        return match;
    }
    
    @Override 
    public void evaluate() {
        boolean o = true;
        for (IO io : inputs) {
            o = o && io.getState();
        }
        if (o) { 
            if (!compareToPrior()) {
                output.setState(!(output.getState()));
            }
        }
        restoreIOArray();
        super.evaluate();
    }
    
    @Override
    protected void layoutIO() {
        initialiseIO();
        float marginH = height / pins / 2;
        float marginW = width / 6;            
        
        for (int i=0; i < inputs.size(); i++) {
            float offset = (float)(Math.sin((((float)i / pins) + (marginH / width)) * Math.PI) * width / 2);
            inputs.get(i).setX((width / 6) + marginW - (offset / 3));
            inputs.get(i).setY((height * i / pins) + marginH);
        }
        output.setX(width - marginW);
        output.setY(height / 2);
    }
}
