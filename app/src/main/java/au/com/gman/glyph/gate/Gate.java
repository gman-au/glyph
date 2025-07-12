package au.com.gman.glyph.gate;

import au.com.gman.glyph.level.*;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;

public abstract class Gate {
    
    private String id;
    private boolean busOutputs;
    protected String type;
    protected float tickSpeed;
    protected int pins;
    private float x;
    private float y;
    protected float width;
    protected float height;    
    protected List<IO> inputs;
    protected IO output;
    protected Library.Transition transition;
    protected float tick;
    protected int delay;
    protected boolean helper;
    
    public float getX() { return x - (width / 2); }
    public float getY() { return y - (height / 2); }
    public float getXc() { return x; }
    public float getYc() { return y; }    
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public boolean getHelper() { return helper; }
    public boolean getBusOutputs() { return busOutputs; }
    public int getPins() { return pins; }
    public String getType() { return type; }
    public String getId() { return id; }
    public List<IO> getInputs() { return inputs; }
    public IO getOutput() { return output; }
    public Library.Transition getTransition() { return transition; }
    public float getTick() { return (float)(tick) / (float)(Library.TRANSITION_QUOTIENT); }    
    public void setHelper(boolean Helper) { helper = Helper; }
    public void setTransition (Library.Transition Transition) { transition = Transition; }
    public void setPins(int Pins) { pins = Pins; }
    public boolean getDetonated() { return false; }
    
    public void setXc(float Xc) { 
        float offset = Xc - x;
        /*for (IO io : inputs) {
            io.setX(io.getX() + offset);            
        }
        if (!(output == null)) { output.setX(output.getX() + offset); }*/
        x = Xc; 
    }
    public void setYc(float Yc) { 
        float offset = Yc - y;
        /*for (IO io : inputs) {
            io.setY(io.getY() + offset);            
        }
        if (!(output == null)) { output.setY(output.getY() + offset); }*/
        y = Yc; 
    }
    
    public Gate(Element e) throws Exception {
        try {
            // Set tick speed to standard
            tickSpeed = 1.0f;
            id = e.getAttribute(Library.GATE_ID);
            x = Float.parseFloat(e.getAttribute(Library.GATE_X));
            y = Float.parseFloat(e.getAttribute(Library.GATE_Y));
            //x = Integer.parseInt(e.getAttribute(Library.GATE_X));
            //y = Integer.parseInt(e.getAttribute(Library.GATE_Y));
            helper = e.hasAttribute(Library.GATE_HELPER);
            width = Library.WIDTH;
            height = Library.HEIGHT;
            pins = Integer.parseInt(e.getAttribute(Library.GATE_PINS));
            type = e.getAttribute(Library.GATE_TYPE);            
            tick = 0.0f;            
            transition = Library.Transition.UNDETERMINED;
            // Assume these outputs will be bussed
            busOutputs = (!(e.hasAttribute(Library.GATE_NOBUS)));
            layoutIO();
        } catch (Exception x) {
            throw(x);
        }
    }
    
    public Gate(String Id, float X, float Y, int Pins) {
        // Set tick speed to standard
        tickSpeed = 1.0f;
        id = Id;
        x = X;
        y = Y;
        width = Library.WIDTH;
        height = Library.HEIGHT;
        pins = Pins;        
        tick = 0.0f;            
        transition = Library.Transition.UNDETERMINED;
        // Assume these outputs will be bussed
        busOutputs = true;
        layoutIO();
    }
    
    public Gate(Gate copy) {
        id = copy.id;
        type = copy.type;
        tickSpeed = copy.tickSpeed;
        pins = copy.pins;
        x = copy.x;
        y = copy.y;
        width = copy.width;
        height = copy.height;
        transition = copy.transition;
        tick = copy.tick;
        delay = copy.delay;
        helper = copy.helper;
        busOutputs = copy.busOutputs;
        layoutIO();
    }
    
    public void rebuildIO() {
        layoutIO();
    }
    
    protected void initialiseIO() {
        // If not initialised, initialise (need to preserve existing)
        if (inputs == null) { 
            inputs = new ArrayList<IO>(); 
        }
        for (int i=inputs.size(); i < pins; i++) {inputs.add(new IO(0, 0));}
        if (output == null) { output = new IO(0, 0); }
    }
    
    protected void layoutIO() {
        initialiseIO();
        float marginH = height / pins / 2;
        float marginW = width / 10;            
        
        for (int i=0; i < inputs.size(); i++) {
            inputs.get(i).setX((width / 6) - marginW);
            inputs.get(i).setY((height * i / pins) + marginH);
            for(Link link : inputs.get(i).getLinks()) {
                link.setPin(i+1);
            }
        }
        output.setX(width - marginW);
        output.setY(height / 2);
    }
    
    public void reOrderPinsAndTrim() {
        ArrayList<IO> reOrderedInputs = new ArrayList<IO>(); 
        // Re-order input pins based on height of source
        int sortIndex = 0;
        for (int i=0; i < inputs.size(); i++) {
            float highestY = 1000.0f;
            int highestIndex = -1;
            for (int j=0; j < inputs.size(); j++) {
                // Input link lists should either have a link or not (1 or 0)
                if (!(reOrderedInputs.contains(inputs.get(j)))) {
                    if (inputs.get(j).getLinks().size() == 1) {
                        if (!(inputs.get(j).getLinks().get(0).getSrcGate() == null)) {
                            if (inputs.get(j).getLinks().get(0).getSrcGate().getY() < highestY) {
                                highestIndex = j;
                                highestY = inputs.get(j).getLinks().get(0).getSrcGate().getY();
                            }
                        }
                    } else {
                        // If the IO isn't used, remove it
                    }
                }
            }
            if (highestIndex >= 0) {
                reOrderedInputs.add(inputs.get(highestIndex));
            }            
            sortIndex++;
        }
        inputs = reOrderedInputs;   
        pins = inputs.size();
        layoutIO();
    }
    
    public void Switch(Library.SwitchingContext context) { }
    public void Switch(boolean state, Library.SwitchingContext context) { }
    
    public void evaluate() {          
        output.transmit();
    }
    
    public void tickGate() {        
        tick+=tickSpeed;
        propagateDelay();
        if (tick >= Library.TRANSITION_QUOTIENT) {
            tick = 0; 
            if (transition.equals(Library.Transition.SWITCHING_ON)) { transition = Library.Transition.FULL_ON; }
            if (transition.equals(Library.Transition.SWITCHING_OFF)) { transition = Library.Transition.FULL_OFF; }
        }
    }
    
    public static Gate LoadGate(Element e) throws Exception {
        try {
            String type = e.getAttribute(Library.GATE_TYPE);
            if (type.equals(Library.TYPE_INPUT)) { return new GateINPUT(e); }
            if (type.equals(Library.TYPE_OUTPUT)) { return new GateOUTPUT(e); }
            if (type.equals(Library.TYPE_AND)) { return new GateAND(e); }
            if (type.equals(Library.TYPE_OR)) { return new GateOR(e); }
            if (type.equals(Library.TYPE_NOT)) { return new GateNOT(e); }
            if (type.equals(Library.TYPE_XOR)) { return new GateXOR(e); }
            if (type.equals(Library.TYPE_BOMB)) { return new GateBOMB(e); }
            if (type.equals(Library.TYPE_FUSE)) { return new GateFUSE(e); }
            if (type.equals(Library.TYPE_FLIP)) { return new GateFLIP(e); }
            if (type.equals(Library.TYPE_NAND)) { return new GateNAND(e); }
            if (type.equals(Library.TYPE_NOR)) { return new GateNOR(e); }
            throw (new Exception());
        } catch (Exception x) {
            throw(x);
        }
    }
    
    public static Gate LoadGate(String type, String Id, float X, float Y, int Pins) throws Exception {
        try {
            if (type.equals(Library.TYPE_INPUT)) { return new GateINPUT(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_OUTPUT)) { return new GateOUTPUT(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_AND)) { return new GateAND(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_OR)) { return new GateOR(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_NOT)) { return new GateNOT(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_XOR)) { return new GateXOR(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_BOMB)) { return new GateBOMB(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_FUSE)) { return new GateFUSE(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_FLIP)) { return new GateFLIP(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_NAND)) { return new GateNAND(Id, X, Y, Pins); }
            if (type.equals(Library.TYPE_NOR)) { return new GateNOR(Id, X, Y, Pins); }
            throw (new Exception());
        } catch (Exception x) {
            throw(x);
        }
    }
    
    public static Gate LoadGate(Gate gate) throws Exception {
        try {
            String type = gate.type;
            if (type.equals(Library.TYPE_INPUT)) { return new GateINPUT(gate.getClass().cast(gate)); }
            if (type.equals(Library.TYPE_OUTPUT)) { return new GateOUTPUT(gate.getClass().cast(gate)); }
            if (type.equals(Library.TYPE_AND)) { return new GateAND(gate.getClass().cast(gate)); }
            if (type.equals(Library.TYPE_OR)) { return new GateOR(gate); }
            if (type.equals(Library.TYPE_NOT)) { return new GateNOT(gate); }
            if (type.equals(Library.TYPE_XOR)) { return new GateXOR(gate); }
            if (type.equals(Library.TYPE_BOMB)) { return new GateBOMB(gate); }
            if (type.equals(Library.TYPE_FUSE)) { return new GateFUSE(gate); }
            if (type.equals(Library.TYPE_FLIP)) { return new GateFLIP(gate); }
            if (type.equals(Library.TYPE_NAND)) { return new GateNAND(gate); }
            if (type.equals(Library.TYPE_NOR)) { return new GateNOR(gate); }
            throw (new Exception());
        } catch (Exception x) {
            throw(x);
        }
    }
    
    protected void propagateDelay() {
        delay++;
        if (delay >= Library.PROPAGATION_FRAMES) {
            delay = 0;
            if (!(transition.equals(Library.Transition.VICTORY))) {
                if (output.getState()) {                 
                    if ((!(transition.equals(Library.Transition.SWITCHING_ON))) && (!(transition.equals(Library.Transition.FULL_ON))) && (!(transition.equals(Library.Transition.DETONATED)))) {
                        transition = Library.Transition.SWITCHING_ON ; 
                        tick = 0;
                    }
                } else { 
                    if ((!(transition.equals(Library.Transition.SWITCHING_OFF))) && (!(transition.equals(Library.Transition.FULL_OFF))) && (!(transition.equals(Library.Transition.DETONATED)))) {
                        transition = Library.Transition.SWITCHING_OFF ; 
                        tick = 0;
                    }
                }
            }
        }
    }
    
    public void destroy() {
        if (!(inputs == null)) {
            for (IO input : inputs) {
                input.destroy();
            }
            inputs.clear();
            inputs = null;
        }
        if (!(output == null)) {
            output.destroy();
            output = null;
        }
    }
    
    public static boolean isOn(Gate g) {
        return g.getTransition().equals(Library.Transition.FULL_ON);
    }
    
    public boolean allInputsResolved() {
        if (!(inputs == null)) {
            for (IO input : inputs) {
                if (!(input.getResolved())) { return false; }
            }
        }
        return true;
    }
    
    public void unResolve() {
        if (!(inputs == null)) {
            for (IO input : inputs) {
                input.setResolved(false);
            }
        }
    }
}