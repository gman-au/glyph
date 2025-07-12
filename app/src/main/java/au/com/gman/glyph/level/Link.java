package au.com.gman.glyph.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.gate.IO;
import java.util.HashMap;
import java.util.Collection;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Link {
    
    private Gate srcGate;
    private Gate trgGate;
    private IO srcIO;
    private IO trgIO;
    private int pin;
    private Library.Transition transition;
    
    private float[] segmentAll;
    private float[] segmentFirst;
    private float[] segmentLast;
    private float[] segmentNone;
    
    private int tick;
    private int lag;
    private List<Point> points;
    private Point transitionPoint;
    
    private float[] pointsArray;
    
    public int getPin() { return pin; }
    public IO getSrcIO() { return srcIO; }
    public IO getTrgIO() { return trgIO; }
    public Gate getSrcGate() { return srcGate; }
    public Gate getTrgGate() { return trgGate; }
    public void setPin(int Pin) { pin = Pin; }
    
    public Link(Element e, HashMap<String, Gate> gates) throws Exception {
        try {
            pin = Integer.parseInt(e.getAttribute(Library.LINK_PIN));
            srcGate = gates.get(e.getAttribute(Library.LINK_SRC));
            trgGate = gates.get(e.getAttribute(Library.LINK_TRG));
            srcIO = srcGate.getOutput();
            trgIO = trgGate.getInputs().get(pin - 1);
            srcIO.getLinks().add(this); 
            trgIO.getLinks().add(this);
            transition = Library.Transition.FULL_OFF;
            tick = 0;
            lag = 0;
            points = new ArrayList<Point>();
            segmentNone = new float[0];
            routePath();
        } catch (Exception x) {
            throw(x);
        }
    }
    
    public Link(int Pin, Gate Src, Gate Trg) {
        pin = Pin;
        srcGate = Src;
        trgGate = Trg;
        srcIO = srcGate.getOutput();
        trgIO = trgGate.getInputs().get(pin - 1);
        srcIO.getLinks().add(this);   
        trgIO.getLinks().add(this);
        transition = Library.Transition.FULL_OFF;
        tick = 0;
        points = new ArrayList<Point>();
        segmentNone = new float[0];
        routePath();
    }
    
    public Link(Gate Src, Gate Trg) {
        srcGate = Src;
        trgGate = Trg;
        transition = Library.Transition.FULL_OFF;
        tick = 0;
        lag = 0;
        points = new ArrayList<Point>();
        segmentNone = new float[0];
    }
    
    private void resetTransitionPoint() { transitionPoint = new Point(srcGate.getX() + srcIO.getX(), srcGate.getY() + srcIO.getY()); }
    public void reset() { transition = Library.Transition.FULL_OFF; }
    
    public void routePath() {
        float iosX = srcGate.getX() + srcIO.getX();
        float iosY = srcGate.getY() + srcIO.getY();
        float iotX = trgGate.getX() + trgIO.getX();
        float iotY = trgGate.getY() + trgIO.getY();
        float mid = iotX - iosX;        
        points.clear();
        resetTransitionPoint();
        if (iosX < iotX) {
            if (iosY != iotY) {                
                points.add(new Point(iosX, iosY));
                points.add(transitionPoint);
                points.add(new Point(iosX + (mid / 2), iosY));
                points.add(new Point(iotX - (mid / 2), iotY));
                points.add(new Point(iotX, iotY));
                //path = new float[]{iosX, iosY, iosX + (mid / 2), iosY, iosX + (mid / 2), iosY, iotX - (mid / 2), iotY,  iotX - (mid / 2), iotY, iotX, iotY};
            } else {                
                points.add(new Point(iosX, iosY));
                points.add(transitionPoint);
                points.add(new Point(iotX, iotY));
            }
        } else {
            // Routing and clearance required
            float clearance = Library.WIDTH;
            float pYm = (iosY - iotY);
            points.add(new Point(iosX, iosY));
            points.add(transitionPoint);
            points.add(new Point(iosX + clearance, iosY));
            points.add(new Point(iosX + clearance, iosY - (pYm / 2)));
            points.add(new Point(iotX - clearance, iotY + (pYm / 2)));
            points.add(new Point(iotX - clearance, iotY));
            points.add(new Point(iotX, iotY));
        }
        pointsArray = new float[(points.size()) * 4];
        recomputePath();
    }
    
    public float[] getPath(Library.Segment s) {
        if (transition == Library.Transition.SWITCHING_OFF) {
            if (s == (Library.Segment.OFF_SEGMENT)) { return segmentFirst; }
            if (s == (Library.Segment.ON_SEGMENT)) { return segmentLast; }
        }
        if (transition == Library.Transition.SWITCHING_ON) {
            if (s == (Library.Segment.OFF_SEGMENT)) { return segmentLast; }
            if (s == (Library.Segment.ON_SEGMENT)) { return segmentFirst; }
        }
        if (transition == Library.Transition.FULL_OFF) {
            if (s == (Library.Segment.OFF_SEGMENT)) { return segmentAll; }
        }
        if (transition == Library.Transition.FULL_ON) {
            if (s == (Library.Segment.ON_SEGMENT)) { return segmentAll; }
        }
        return segmentNone; 
    }
    
    public void recomputePath() {
        int transIndex = points.indexOf(transitionPoint);
        for (int i=0; i < points.size(); i++) {
            pointsArray[(i * 4) + 0] = points.get(i).x;
            pointsArray[(i * 4) + 1] = points.get(i).y;
            pointsArray[(i * 4) + 2] = points.get(i).x;
            pointsArray[(i * 4) + 3] = points.get(i).y;
        }
        segmentFirst = Arrays.copyOfRange(pointsArray, 2, Math.max((transIndex * 4) + 2, 2));
        segmentLast = Arrays.copyOfRange(pointsArray, Math.max((transIndex * 4) + 2, 2), (pointsArray.length - 2));
        segmentAll = Arrays.copyOfRange(pointsArray, 2, (pointsArray.length - 2));
    }
    
    public void tickLink() throws Exception {
        if (tick >= Library.TRANSITION_FRAMES) {
            tick = 0;

            for (Point p : points) {
                if ((!(transitionPoint.equals(p))) && (transitionPoint.isOn(p)) && (points.indexOf(p) > 0)) {
                    int index = points.indexOf(p);
                    points.remove(transitionPoint);
                    points.add(index, transitionPoint); 
                    break;
                }
            }
            if (points.indexOf(transitionPoint) == points.size() - 1) {
                points.remove(transitionPoint);
                points.add(1, transitionPoint);     
                transitionPoint.x = points.get(0).x;
                transitionPoint.y = points.get(0).y;
                if (transition.equals(Library.Transition.SWITCHING_OFF)) { transition = Library.Transition.FULL_OFF ;}
                if (transition.equals(Library.Transition.SWITCHING_ON)) { transition = Library.Transition.FULL_ON ;}
            }

            Point nextPoint = points.get(points.indexOf(transitionPoint) + 1);
            if (!(transitionPoint.isOn(nextPoint))) {
                if ((int)transitionPoint.y == (int)nextPoint.y) {
                    if (nextPoint.x > transitionPoint.x) { 
                        transitionPoint.x += Library.LINK_SPEED;
                        if (transitionPoint.x > (nextPoint.x)) { transitionPoint.x = (nextPoint.x);}
                    } else {
                        transitionPoint.x -= Library.LINK_SPEED;
                        if (transitionPoint.x < (nextPoint.x)) { transitionPoint.x = (nextPoint.x);}
                    }
                }                
            }
            if (!(transitionPoint.isOn(nextPoint))) {
                if ((int)transitionPoint.x == (int)nextPoint.x) {
                    if (nextPoint.y > transitionPoint.y) { 
                        transitionPoint.y += Library.LINK_SPEED;
                        if (transitionPoint.y > (nextPoint.y)) { transitionPoint.y = (nextPoint.y);}
                    } else {
                        transitionPoint.y -= Library.LINK_SPEED;
                        if (transitionPoint.y < (nextPoint.y)) { transitionPoint.y = (nextPoint.y);}
                    }
                } else {
                    if (!(transitionPoint.y == nextPoint.y)) {
                        // This should not occur
                    }
                }
            }
            // Assuming that the size of the points list hasn't changed
            recomputePath();
        } else {
            if ((transition.equals(Library.Transition.SWITCHING_OFF)) || (transition.equals(Library.Transition.SWITCHING_ON))) { 
                tick++; 
                // Assuming that the size of the points list hasn't changed
                recomputePath();
            } else { tick = 0; }
        }
    }
    
    public void transmit() {
        if (trgIO.getState() != srcIO.getState()) {            
            if (srcIO.getState()) { transition = Library.Transition.SWITCHING_ON ; } 
            else { transition = Library.Transition.SWITCHING_OFF ; } 
        }
        trgIO.setState(srcIO.getState());
        // Set the target IO to 'resolved' - this IO will not change its state this cycle.
        // Only when all the target gate IOs are resolved should the target gate evaluate and transmit.
        trgIO.setResolved(true);
        if (trgGate.allInputsResolved()) {
            trgGate.evaluate();
        }
    }
    
    public void destroy() {
        srcGate = null;
        trgGate = null;;
        srcIO = null;
        trgIO = null;
    
        segmentAll = null;
        segmentFirst = null;
        segmentLast = null;
        segmentNone = null;
    
        if (!(points == null)) {
            points.clear();
            points = null;
        }
        transitionPoint = null;
    }
    
    public boolean hasMidpoints() { return (points.size() == 5); }
    
    public float getMidpoint() {
        if (hasMidpoints()) {
            return points.get(2).x;
        }
        return 0.0f;
    }
    
    public void moveMidpoints(float X) {
        if (points.size() == 5) {
            points.get(2).x = X;
            points.get(3).x = X;
            // Assuming that the size of the points list hasn't changed
            recomputePath();
        }
    }
    
    public static void drawLink(Canvas c, Paint p, Link link) throws Exception {
        drawLink(c, p, link, Library.Segment.WHOLE_SEGMENT);
    }
    
    public static void drawLink(Canvas c, Paint p, Link link, Library.Segment s) throws Exception {
        try {            
            float[] line;
            Xfermode pdfm = p.getXfermode();
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            
            if (s.equals(Library.Segment.OFF_SEGMENT) || s.equals(Library.Segment.WHOLE_SEGMENT)) {
                line = link.getPath(Library.Segment.OFF_SEGMENT);
                p.setStrokeWidth(3.0f);
                p.setColor(Color.argb(20, Library.LINK_LUMINOSITY_4, Library.LINK_LUMINOSITY_4, Library.LINK_LUMINOSITY_4));
                c.drawLines(line, p);            
                p.setStrokeWidth(2.0f);
                p.setColor(Color.argb(80, Library.LINK_LUMINOSITY_5, Library.LINK_LUMINOSITY_5, Library.LINK_LUMINOSITY_5));
                c.drawLines(line, p);                           
            }
            
            if (s.equals(Library.Segment.ON_SEGMENT) || s.equals(Library.Segment.WHOLE_SEGMENT)) {
                line = link.getPath(Library.Segment.ON_SEGMENT);

                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));//(Mode.LIGHTEN));
                p.setColor(Color.argb(20, Library.LINK_LUMINOSITY_4, Library.LINK_LUMINOSITY_4, Library.LINK_LUMINOSITY_4));
                p.setStrokeWidth((float)Library.HEIGHT / 3.0f);// (15.0f);
                c.drawLines(line, p);
                p.setColor(Color.argb(40, Library.LINK_LUMINOSITY_3, Library.LINK_LUMINOSITY_3, Library.LINK_LUMINOSITY_3));
                p.setStrokeWidth((float)Library.HEIGHT / 4.8f);//(10.0f);
                c.drawLines(line, p);
                p.setColor(Color.argb(80, Library.LINK_LUMINOSITY_2, Library.LINK_LUMINOSITY_2, Library.LINK_LUMINOSITY_2));
                p.setStrokeWidth((float)Library.HEIGHT / 8.0f);//(6.0f);
                c.drawLines(line, p);
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                p.setColor(Color.argb(255, Library.LINK_LUMINOSITY_1, Library.LINK_LUMINOSITY_1, Library.LINK_LUMINOSITY_1));
                p.setStrokeWidth(2.0f);
                c.drawLines(line, p);
            }
            p.setXfermode(pdfm);
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    public static void busLinks(Collection<Link> links) {
        for (Link lS : links) {
            for (Link lT : links ) {
                if (!(lT.equals(lS))) {                    
                    if ((!(lT.srcIO == null)) && (!(lS.srcIO == null))) {
                        if (lT.srcIO.equals(lS.srcIO)) {
                            if (!(lT.srcGate == null)) {
                                if (lT.srcGate.getBusOutputs()) {
                                    // Are we bussing this link?
                                    Link.busLink(lS, lT);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void busLink(Link l1, Link l2) {
        float x = 0.0f;
        if (l1.hasMidpoints() && l2.hasMidpoints()) {
            x = Math.min(l1.getMidpoint(), l2.getMidpoint());
            l1.moveMidpoints(x);
            l2.moveMidpoints(x);
        }
    }
    
    public static Link dynamicLink(Gate source, Gate target) 
            throws tooManyInputsException, tooManyOutputsException, circularLinksException, existingLinksException {
        if (testForExisting(source, target)) { throw new existingLinksException(); }
        if (testForCircularity(target, source)) { throw new circularLinksException(); }
        // Create the link object
        Link newLink = new Link(source, target);
        // Outputs cannot have an output        
        newLink.srcIO = source.getOutput();
        if (newLink.srcIO.getLinks().size() >= Library.maxOutputsPerGate(source)) { throw new tooManyOutputsException(); }
        while (newLink.trgIO == null) {            
            for (IO io : target.getInputs()) {
                // Find a suitable IO
                if (io.getLinks().isEmpty()) {
                    newLink.trgIO = io;
                    break;
                }
            }
            if (newLink.trgIO == null) {
                if (target.getPins() >= Library.maxInputsPerGate(target)) { throw new tooManyInputsException(); }
                target.setPins(target.getPins() + 1);
                target.rebuildIO();                
            }
        }
        if (!(newLink.trgIO == null)) {
            newLink.setPin(newLink.trgGate.getInputs().indexOf(newLink.trgIO) + 1);
            newLink.srcIO.getLinks().add(newLink);   
            newLink.trgIO.getLinks().add(newLink);
            newLink.routePath();
            return newLink;
        } else {
            return null;
        }
    }
    
    public static boolean testForCircularity(Gate gate, Gate reference) {
        boolean circular = false;
        if (gate.equals(reference)) { return true; }
        if (!(gate.getOutput() == null)) {
            if (!(gate.getOutput().getLinks() == null)) {
                for (Link link : gate.getOutput().getLinks()) {
                    circular = circular || testForCircularity(link.trgGate, reference);
                }
            }
        }
        return circular;
    }
    
    public static boolean testForExisting(Gate source, Gate target) {
        if (!(source.getOutput() == null)) {
            if (!(source.getOutput().getLinks() == null)) {
                for (Link link : source.getOutput().getLinks()) {
                    if (link.getTrgGate().equals(target)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static class tooManyInputsException extends Exception { }
    public static class tooManyOutputsException extends Exception { }
    public static class circularLinksException extends Exception { }
    public static class existingLinksException extends Exception { }
}