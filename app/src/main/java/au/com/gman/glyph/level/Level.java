package au.com.gman.glyph.level;

import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.Panel;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;
import android.util.Xml;
import java.io.StringWriter;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.GateINPUT;
import au.com.gman.glyph.gate.GateFLIP;
import au.com.gman.glyph.gate.GateFUSE;
import au.com.gman.glyph.gate.GateBOMB;
import au.com.gman.glyph.level.Event;

public class Level {

   private HashMap<String, Gate> gates;
   private HashMap<String, Link> links;
   private String id;
   private String author;
   
   private ColorX primary;
   private ColorX secondary;
   
   private long nanos;
   private int clicks;
   
   private boolean tutorial;
   private HashMap<String, Gate> tutorialGates;
   private HashMap<String, Link> tutorialLinks;
   private ArrayList<Event> tutorialEvents;
   private Element e;   
   private boolean unpacked;
   
   public boolean getUnpacked() { return unpacked; }
   public boolean getTutorial() { return tutorial; }
   public int getClicks() { return clicks; }
   public long getNanos() { return nanos; }
   public String getId() { return id; }
   public String getAuthor() { return author; }
   public void setClicks(int Clicks) { clicks = Clicks; }
   public void setNanos(long Nanos) { nanos = Nanos; }
   public void setTutorial(boolean Tutorial) { tutorial = Tutorial; }
   public HashMap<String, Gate> getGates() { return gates; }
   public HashMap<String, Link> getLinks() { return links; }   
   public HashMap<String, Gate> getTutorialGates() { return tutorialGates; }
   public HashMap<String, Link> getTutorialLinks() { return tutorialLinks; }
   public ArrayList<Event> getTutorialEvents() { return tutorialEvents; }
   
   public ColorX getPrimary() { 
       if (primary == null) { primary = new ColorX(255, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY); }
       return primary; 
   }
   
   public ColorX getSecondary() { 
       if (secondary == null) { secondary = new ColorX(255, Library.DEFAULT_SECONDARY, Library.DEFAULT_SECONDARY, Library.DEFAULT_SECONDARY); }
       return secondary; 
   }
   
   public Level() {
        gates = new HashMap<String, Gate>();
        links = new HashMap<String, Link>();
        tutorialGates = new HashMap<String, Gate>();
        tutorialLinks = new HashMap<String, Link>();
        tutorialEvents = new ArrayList<Event>();
   }
   
   public Level(Element E, boolean unpack) throws Exception {
        this();
        try {                    
            e = E;
            if (unpack) { unpacked = unpackLevel(); }

        } catch (Exception x) {
            throw(x);
        }
    }
   
    public void unpackIfRequired() { 
        if (!unpacked) { unpacked = unpackLevel(); }
    }
    
    public void repack() {
        if (unpacked) { 
            for (Gate gate : gates.values()) { 
                gate.destroy();
            }
            for (Link link : links.values()) {
                link.destroy();
            }
            for (Gate gate : tutorialGates.values()) {
                gate.destroy();
            }
            for (Link link : tutorialLinks.values()) {
                link.destroy();
            }
            for (Event event : tutorialEvents) {
                event.destroy();
            }
            unpacked = false;
            clearLists();
        }
    }
    
    private void clearLists() {
        gates.clear();
        links.clear();
        tutorialGates.clear();
        tutorialLinks.clear();
        tutorialEvents.clear();
    }
   
    private boolean unpackLevel() {
        if (!(e == null)) {
            try {
                // clear the lists
                clearLists();
                
                NodeList nodes;
                // add gates to list 
                nodes = e.getElementsByTagName(Library.GATE);
                for (int i=0; i < nodes.getLength(); i++) {                
                    Gate gate = Gate.LoadGate((Element)nodes.item(i));
                    gates.put(gate.getId(), gate);
                } 
                // add links to list 
                nodes = e.getElementsByTagName(Library.LINK);
                for (int i=0; i < nodes.getLength(); i++) {                
                     Link link = new Link((Element)nodes.item(i), gates);
                     links.put(link.getSrcIO() + "-" + link.getTrgIO(), link);
                } 
                id = e.getAttribute(Library.LEVEL_ID);
                author = e.getAttribute(Library.LEVEL_AUTHOR);
                primary = Library.stringToColorX(e.getAttribute(Library.COLOR_PRIMARY));
                secondary = Library.stringToColorX(e.getAttribute(Library.COLOR_SECONDARY));
                nodes = e.getElementsByTagName(Library.TUTORIAL);
                for (int i=0; i < nodes.getLength(); i++) {       
                    NodeList tutorialElements = nodes.item(i).getChildNodes();
                    for (int j=0; j < tutorialElements.getLength(); j++) {
                        // Tutorial gate
                        if (tutorialElements.item(j).getNodeName().matches(Library.TUTORIAL_GATE)) {
                            Gate gate = Gate.LoadGate((Element)tutorialElements.item(j));
                            tutorialGates.put(gate.getId(), gate);
                        }
                        // Tutorial link
                        if (tutorialElements.item(j).getNodeName().matches(Library.TUTORIAL_LINK)) {
                            Link link = new Link((Element)tutorialElements.item(j), tutorialGates);
                            tutorialLinks.put(link.getSrcIO() + "-" + link.getTrgIO(), link);
                        }                    
                    }
                    for (int j=0; j < tutorialElements.getLength(); j++) {
                        // Tutorial event
                        if (tutorialElements.item(j).getNodeName().matches(Library.TUTORIAL_EVENT)) {
                            Event event = new Event((Element)tutorialElements.item(j), tutorialGates);
                            tutorialEvents.add(event);
                        }
                    }
                }
                // Bus the links
                Link.busLinks(links.values());
                return true;
            } catch (Exception e) {
                clearLists();
                return false;
            }
        }
        return true;
   }
   
   public void reEvaluate() {        
        for (Gate gate : getGates().values()) {
            if (gate.getClass().equals(GateINPUT.class)) {
                gate.evaluate();
            }
        }
   }
   
   public void unResolve() {
       for (Gate gate : getGates().values()) {
            gate.unResolve();
        }
   }
   
   public void restore() {
       for (Gate gate : getGates().values()) {
            gate.setTransition(Library.Transition.UNDETERMINED);
            if (gate.getClass().equals(GateINPUT.class)) {
                 gate.Switch(false, Library.SwitchingContext.REQUIRE_RESOLUTION);
            }
            if (gate.getClass().equals(GateBOMB.class)) {
                 ((GateBOMB)gate).setDetonate(false);
            }
            if (gate.getClass().equals(GateFUSE.class)) {
                 ((GateFUSE)gate).setBlown(false);
            }
       }
   }
   
   public static String RenderXML(Level level, Panel xP) throws Exception {
        StringWriter XML = new StringWriter();
        XmlSerializer sz = Xml.newSerializer();
        sz.setOutput(XML);
        sz.startDocument("UTF-8", true);
        sz.startTag(Library.LEVEL_FILE_NAMESPACE, Library.LEVEL);
            sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.LEVEL_ID, "XXXX");
            sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.LEVEL_AUTHOR, xP.getLocaleString(au.com.gman.glyph.R.string.level_default_author));
            for (Gate gate : level.getGates().values()) {
                sz.startTag(Library.LEVEL_FILE_NAMESPACE, Library.GATE);
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.GATE_ID, gate.getId());
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.GATE_PINS, String.valueOf(gate.getPins()));
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.GATE_TYPE, gate.getType());
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.GATE_X, String.valueOf((int)gate.getXc()));
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.GATE_Y, String.valueOf((int)gate.getYc()));
                sz.endTag(Library.LEVEL_FILE_NAMESPACE, Library.GATE);
            }
            for (Link link : level.getLinks().values()) {
                sz.startTag(Library.LEVEL_FILE_NAMESPACE, Library.LINK);
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.LINK_SRC, link.getSrcGate().getId());
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.LINK_TRG, link.getTrgGate().getId());
                    sz.attribute(Library.LEVEL_FILE_NAMESPACE, Library.LINK_PIN, String.valueOf(link.getPin()));
                sz.endTag(Library.LEVEL_FILE_NAMESPACE, Library.LINK);
            }
        sz.endTag(Library.LEVEL_FILE_NAMESPACE, Library.LEVEL);    
        sz.endDocument();        
        return XML.toString();
   }
   
   public void destroy() {
       repack();       
       gates = null;
       links = null;
       tutorialGates = null;
       tutorialLinks = null;
       tutorialEvents = null;
   }
}

