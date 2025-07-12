/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.view.MotionEvent;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.profile.ProfileAccount;
import au.com.gman.glyph.gate.*;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.level.Link.circularLinksException;
import au.com.gman.glyph.level.Link.existingLinksException;
import au.com.gman.glyph.level.Link.tooManyInputsException;
import au.com.gman.glyph.level.Link.tooManyOutputsException;
import au.com.gman.glyph.level.Point;
import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author gub
 */
public class ScreenEditor extends Screen {
    
    private boolean pressedAndHeld = false;
    private long holdTime = 0;
    private Level workingLevel;
    private Library.EditingMode workingMode;
    private Gate workingGate;
    private Gate targetGate;
    private Point linkPoint;
    private RectF trashRect;
    private ArrayList<Point> snapLines;
    private Point snappedPoint;
    
    public ScreenEditor(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile);
        snapLines = new ArrayList<Point>();
        snappedPoint = new Point(0, 0);
        workingLevel = null;
        workingMode = Library.EditingMode.NOTHING;
        workingGate = null;
        linkPoint = null;
        font = xFont;
        primary = new ColorX(255, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY);
        secondary = new ColorX(255, Library.DEFAULT_SECONDARY, Library.DEFAULT_SECONDARY, Library.DEFAULT_SECONDARY);
        trashRect = new RectF(Library.REFERENCE_WIDTH - 80.0f, Library.REFERENCE_HEIGHT - 80.0f, 
                Library.REFERENCE_WIDTH - 20.0f, Library.REFERENCE_HEIGHT - 20.0f);
        P.applyColors(primary, secondary);
        // New level by default
        newLevel();
    }
    
    public Level getCurrentLevel() { return workingLevel; }
    public Chapter getCurrentChapter() { return null; }
    
    @Override
    public void tickObjects() throws Exception{
        if (pressedAndHeld) {
            holdTime += P.getScreenNanos();
            if (holdTime >= 750000000) {
                pressedAndHeld = false;
                holdTime = 0;
                workingMode = Library.EditingMode.PLACING_LINK;
                linkPoint = null;
            }
        }
    }
    
    public void loadLevel(Level level) {
        if (!(workingLevel == null)) { workingLevel.destroy(); }
        if (!(workingGate == null)) { workingGate.destroy(); }
        workingLevel = level;
        restore();
    }
    
    public void newLevel() {
        workingLevel = new Level();        
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        try {                     
            if (!(workingLevel == null)) {                
                ScreenEditor.drawLinks(c, workingLevel, p);
                drawEditorGates(c, workingLevel, p, sprites);
                if (!(workingGate == null)) {
                    sprites.getGateSprite(workingGate.getType(), Library.Transition.FULL_ON).draw(c, 
                        new Point(workingGate.getXc(), workingGate.getYc()), 0.0f, p, Library.Transition.FULL_ON, PorterDuff.Mode.ADD);
                }
                p.setXfermode(null);
                p.setTextSize(17.5f);
                p.setTypeface(font);
                p.setColor(Color.BLACK);
                p.setShader(null);
                c.drawText(P.getLocaleString(au.com.gman.glyph.R.string.editor_mode_editing), Library.WIDTH / 2, Library.REFERENCE_HEIGHT - (Library.HEIGHT / 2), p);
                if (workingMode.equals(Library.EditingMode.PLACING_LINK)) {
                    if (!(workingGate == null)) {
                        p.setColor(Color.WHITE);
                        p.setStrokeWidth(3.0f);
                        if (!(linkPoint == null)) {
                            c.drawLine(workingGate.getXc(), workingGate.getYc(), linkPoint.x, linkPoint.y, p);
                        }
                    }
                }
                if (workingMode.equals(Library.EditingMode.MOVING_GATE)) {
                    p.setColor(Color.WHITE);      
                    String caption = P.getLocaleString(au.com.gman.glyph.R.string.editor_delete);
                    c.drawText(caption, trashRect.centerX() - (p.measureText(caption) / 2), trashRect.top - 5.0f, p);
                    p.setStrokeWidth(3.0f);
                    p.setStyle(Paint.Style.STROKE);
                    c.drawRect(trashRect, p);
                    p.setStyle(Paint.Style.FILL);
                    // Draw snap lines if applicable                               
                    if ((snapLines.size() > 0) && (snapLines.size() % 2 == 0)) {
                        for (int i=0; i < snapLines.size(); i+=2) {
                            p.setStrokeWidth(2.0f);         
                            c.drawLine(snapLines.get(i).x, snapLines.get(i).y, snapLines.get(i+1).x, snapLines.get(i+1).y, p);
                        }
                    }
                }                
            }
        } catch (Exception e) {
            throw(e); 
        }
    }
    
    private boolean isHighlighted(Gate gate) {
        return (gate.equals(workingGate) || gate.equals(targetGate));
    }
    
    public void drawEditorGates(Canvas c, Level l, Paint p, SpriteController sc) {    
        if (!(l.getGates().values() == null)) {
            if (!(sc == null)) {
                for (Gate gate : l.getGates().values()) {
                    try {
                        sc.getGateSprite(gate.getType(), isHighlighted(gate) ? Library.Transition.FULL_ON : Library.Transition.FULL_OFF).draw(c, 
                            new Point(gate.getXc(), gate.getYc()), 
                            isHighlighted(gate) ? 0.0f : 0.0f, p, 
                            isHighlighted(gate) ? Library.Transition.FULL_ON : Library.Transition.FULL_OFF,
                            isHighlighted(gate) ? PorterDuff.Mode.ADD : PorterDuff.Mode.DARKEN);
                    }
                    catch(Exception e) {}
                }
            }
        }
    }
    
    public void restore() {
        P.applyColors(primary, secondary);
        if (!(workingLevel == null)) { 
            workingLevel.restore(); 
            Library.resetLinks(workingLevel);            
        }
        
    }
    
    @Override
    public void destroy() {
        if (!(workingLevel == null)) { workingLevel.destroy(); }
        workingLevel = null;
        snapLines.clear();
        snapLines = null;
        super.destroy();
    }
    
    public void pickGateToPlace(Gate selectedGate) throws Exception {
        if (!(selectedGate == null)) {
            workingGate = Gate.LoadGate(selectedGate.getType(), generateID(), -50.0f, -50.0f, 0);
            workingMode = Library.EditingMode.PLACING_GATE;
        }
    }
    
    private String generateID() {
        int counter = 0;
        String key = null;
        do {
            key = "o" + counter;
            counter++;
        }
        while(workingLevel.getGates().get(key) != null);
        return key;
    }
    
    private void deleteGate(Gate gate) {
        ArrayList<Link> linksToDelete = new ArrayList<Link>();
        if (!(gate == null)) {
            // Input IO List
            for (IO io : gate.getInputs()) {  
                linksToDelete.clear();
                for (Link link : io.getLinks()) {
                    linksToDelete.add(link);
                    // Remove the source gate connection
                    link.getSrcGate().getOutput().getLinks().remove(link);
                }
                io.getLinks().removeAll(linksToDelete);
                workingLevel.getLinks().values().removeAll(linksToDelete);
            }
            linksToDelete.clear();
            // Output IO
            for (Link link : gate.getOutput().getLinks()) {
                linksToDelete.add(link);
                // Remove the target gate IO completely (one to one inputs)
                link.getTrgGate().getInputs().remove(link.getTrgIO());
                link.getTrgIO().destroy();
            }
            gate.getOutput().getLinks().removeAll(linksToDelete);
            workingLevel.getLinks().values().removeAll(linksToDelete);
            workingLevel.getGates().remove(gate.getId());
            gate.destroy();
        }
    }
    
    @Override
    public void handleTouchEvent(MotionEvent event) {
        try {
            snapLines.clear();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {                
                // If placing a gate, then down action places it on point
                if (workingMode.equals(Library.EditingMode.PLACING_GATE)) {
                    if (!(workingGate == null)) {
                        workingGate.setXc(event.getX());
                        workingGate.setYc(event.getY());
                        Gate newGate = Gate.LoadGate(workingGate);
                        workingLevel.getGates().put(newGate.getId(), newGate);
                        workingGate = newGate;
                        // Once placed, user can move it into exact position
                        workingMode = Library.EditingMode.MOVING_GATE;                            
                    }
                    return;
                }         
                if (workingMode.equals(Library.EditingMode.NOTHING)) {
                    workingGate = null;
                    if (!(workingLevel == null)) {
                        for (Gate gate : workingLevel.getGates().values()) {
                            if (Library.inBounds(event.getX(), event.getY(), gate)) {
                                workingGate = gate;
                                break;
                            }
                        }
                    }                    
                }
                pressedAndHeld = true;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!(workingGate == null)) {
                    // If we moved within the time frame then its a move mode
                    if (workingMode.equals(Library.EditingMode.NOTHING)) {
                        workingMode = Library.EditingMode.MOVING_GATE;
                        holdTime = 0;
                        pressedAndHeld = false;
                    }
                }
                if (workingMode.equals(Library.EditingMode.MOVING_GATE)) {
                    if (!(workingGate == null)) {
                        // Check alignments and snaps
                        snappedPoint = Library.checkAlignments(event.getX(), event.getY(), workingGate, workingLevel, snapLines);
                        workingGate.setXc(snappedPoint.x);
                        workingGate.setYc(snappedPoint.y);
                    }
                }
                if (workingMode.equals(Library.EditingMode.PLACING_LINK)) {
                    if (linkPoint == null) { linkPoint = new Point(0, 0); }
                    linkPoint.x = event.getX();
                    linkPoint.y = event.getY();
                    if (!(workingLevel == null)) {
                        targetGate = null;
                        for (Gate gate : workingLevel.getGates().values()) {
                            // Gate can't link to itself
                            if (!(gate.equals(workingGate))) {
                                if (Library.inBounds(event.getX(), event.getY(), gate)) {
                                    targetGate = gate;
                                    break;
                                }
                            }
                        }
                    }
                }
                Library.tidyGates(workingLevel);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (workingMode.equals(Library.EditingMode.PLACING_LINK)) {
                    if (((!(workingGate == null)) && (!(targetGate == null))) && (!(workingGate.equals(targetGate)))) {
                        // Link typically goes from working gate output to target gate input
                        try {
                            Link newLink = Link.dynamicLink(workingGate, targetGate);
                            if (!(newLink == null)) {
                                workingLevel.getLinks().put(newLink.getSrcIO() + "-" + newLink.getTrgIO(), newLink);
                            }
                        } catch (tooManyInputsException e) {
                            P.displayDialog(Library.Dialogs.MESSAGE_ONLY, P.getLocaleString(au.com.gman.glyph.R.string.editor_too_many_inputs));
                        } catch (tooManyOutputsException e) {
                            P.displayDialog(Library.Dialogs.MESSAGE_ONLY, P.getLocaleString(au.com.gman.glyph.R.string.editor_too_many_outputs));
                        } catch (circularLinksException e) {
                            P.displayDialog(Library.Dialogs.MESSAGE_ONLY, P.getLocaleString(au.com.gman.glyph.R.string.editor_circular_logic));
                        } catch (existingLinksException e) {
                            P.displayDialog(Library.Dialogs.MESSAGE_ONLY, P.getLocaleString(au.com.gman.glyph.R.string.editor_existing_link));
                        }
                    }
                }
                if (workingMode.equals(Library.EditingMode.MOVING_GATE)) {
                    if (!(workingGate == null)) {
                        if (Library.inBounds(workingGate.getXc(), workingGate.getYc(), trashRect)) {
                            deleteGate(workingGate);
                        }
                    }
                }
                workingGate = null;
                targetGate = null;
                workingMode = Library.EditingMode.NOTHING;
                holdTime = 0;
                pressedAndHeld = false;                
            }
        } catch (Exception e) {
            P.exceptionDialog(e);
        } finally {
            Library.tidyGates(workingLevel);
        }
        
    }
}
