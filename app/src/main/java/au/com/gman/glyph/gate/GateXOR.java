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
public class GateXOR extends Gate {
    
    public GateXOR(Element e) throws Exception {
        super(e);
        tickSpeed = 0.5f;
    }
    
    public GateXOR(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_XOR;
        tickSpeed = 0.5f;
    }
    
    public GateXOR(Gate copy) { super(copy); }
    
    @Override 
    public void evaluate() {
        
        boolean o = false;
        boolean prevState = false;
        for (int i=0; i < inputs.size(); i++) {
            if (i > 0) {
                if (inputs.get(i).getState() != prevState) {
                    o = true;
                }
            }
            prevState = inputs.get(i).getState();
        }
        output.setState(o);
        super.evaluate();
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
