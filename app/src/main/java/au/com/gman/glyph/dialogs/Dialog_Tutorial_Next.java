/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.dialogs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import au.com.gman.glyph.R;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.level.Event;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.screens.Screen;
import java.util.HashMap;
import java.util.ArrayList;
import au.com.gman.glyph.engine.Panel;
/*
 *
 * @author gub
 */
public class Dialog_Tutorial_Next extends Dialog {
    
    private HashMap<String, Gate> gates; 
    private HashMap<String, Link> links;
    private ArrayList<Event> events;
    private int page;
    private Paint paintO;
    private Panel P;
    
    private Library.SwitchingContext context = Library.SwitchingContext.DISREGARD_RESOLUTION;
            
    public Dialog_Tutorial_Next(ColorX C1, ColorX C2, String T, RectF R, Typeface font, Library.Dialogs Type,
            HashMap<String, Gate> Gates, HashMap<String, Link> Links, ArrayList<Event> Events, Panel xP) {
        super(C1, C2, T, R, font, Type);
        P = xP;
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.1f, 0.75f, 0.7f, 0.1f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_next), Button.NEXT_PAGE, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.7f, 0.75f, 0.1f, 0.1f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_cancel), Button.CANCEL, font));
        gates = (HashMap<String, Gate>)Gates.clone();
        links = (HashMap<String, Link>)Links.clone();
        for( Gate gate : gates.values()) { gate.Switch(false, context); gate.setHelper(false);}
        events = (ArrayList<Event>)Events.clone();
        page = 0;
        if (!(events == null)) {
            if (events.size() > page) {
                loadEvent(events.get(page));
            }
        }
        paintO = new Paint();       
        paintO.setStrokeWidth(2.0f);
        paintO.setStrokeCap(Paint.Cap.ROUND);
        paintO.setStrokeMiter(20.0f);   
        paintO.setFilterBitmap(true);
        paintO.setAntiAlias(true);
    }
    
    private void loadEvent(Event e) {
        if (!(e == null)) {
            if (!(e.getRes() == null)) {
                setText(P.getLocaleString(e.getRes()));
            }
            if (e.getAction() == Library.EVENT_ACTION_SWITCH) {
                if (!(e.getGate() == null)) {
                    e.getGate().Switch(context);
                }
            }
            if (e.getAction() == Library.EVENT_ACTION_HELPER) {
                if (!(e.getGate() == null)) {
                    e.getGate().setHelper(!(e.getGate().getHelper()));
                }
            }
            if (!(e.getWait())) { nextPage(); }
        }
    }
    
    @Override
    public void drawDialog(Canvas c, Paint p, SpriteController sc) {        
        super.drawDialog(c, p);
        Screen.drawLinks(c, links.values(), paintO);
        Screen.drawGates(c, gates.values(), paintO, sc, 0, P.getColorFilter());
    }
    
    @Override
    public void tick() throws Exception { 
        super.tick();
        for (Gate gate : gates.values()) {  
            gate.tickGate();
            }
        for (Link link : links.values()) {
            link.tickLink();
        }   
    }
    
    @Override
    public boolean nextPage() {
        page++;
        if (!(events == null)) {
            if (events.size() > page) {
                loadEvent(events.get(page));
            }                 
        }
        pressed = null;
        if (!(events == null)) { return (events.size() > page); }
        return false;
    }
    
    @Override
    public void destroy() {
        if (!(gates == null)) { gates.clear(); }
        gates = null;
        if (!(links == null)) { links.clear(); }
        links = null;
        if (!(events == null)) { events.clear(); }
        events = null;
        if (!(paintO == null)) { paintO.reset(); }
        paintO = null;
        P = null;
        super.destroy();
    }
}


