/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.gate;

import au.com.gman.glyph.level.Library;

/**
 *
 * @author gub
 */
public class GateMENU extends GateINPUT {
    
    public GateMENU(String Id, float X, float Y, int Pins) {
        super(Id, X, Y, Pins);
        type = Library.TYPE_MENU;
    }
    
    public GateMENU(Gate copy) { super(copy); }
}
