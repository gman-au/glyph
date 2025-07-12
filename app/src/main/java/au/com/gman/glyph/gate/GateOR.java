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
public class GateOR extends Gate {
    
    public GateOR(Element e) throws Exception {
        super(e);
        tickSpeed = 0.5f;
    }
    
    public GateOR(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_OR;
        tickSpeed = 0.5f;
    }
    
    public GateOR(Gate copy) { super(copy); }
    
    @Override 
    public void evaluate() {
        boolean o = false;
        for (IO io : inputs) {
            o = o || io.getState();
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
