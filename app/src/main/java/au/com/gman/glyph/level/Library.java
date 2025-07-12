/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.level;
import android.content.Context;
import au.com.gman.glyph.gate.*;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import au.com.gman.glyph.engine.ColorX;
import android.graphics.RectF;
import android.os.Environment;
import android.text.StaticLayout;
import android.view.SurfaceView;
import android.text.Layout;
import android.text.TextPaint;
import au.com.gman.glyph.R;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import au.com.gman.glyph.engine.Panel;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import android.graphics.ColorMatrix;
/**
 *
 * @author gub
 */

public class Library {
    
    public static final float SWIPE_SCREEN_TOP_LIMIT = -100.0f;
    public static final float SWIPE_SCREEN_BOTTOM_LIMIT = 800.0f;
    public static final float REFERENCE_WIDTH = 533;
    public static final float REFERENCE_HEIGHT = 320;
    
    public static final String CHAPTER = "chapter";
    public static final String LEVEL = "level";
    public static final String GATE = "gate";
    public static final String LINK = "link";
    
    public static final String ORIGINAL = "original";
    public static final String TUTORIAL = "tutorial";
    public static final String LOCKING = "locking";
    
    public static final String PROFILE_ACCOUNT_FILE = "profile.txt";
    public static final String LEVEL_FILE_PREFIX = "glyph-level-";
    public static final String LEVEL_FILE_SUFFIX = ".xml";
    public static final String LEVEL_FILE_FOLDER = "glyph_level";
    public static final String LEVEL_FILE_NAMESPACE = "";
    public static final String LEVEL_FILE_NOMEDIA = ".nomedia";
    
    public static final String TUTORIAL_GATE = "tutorialgate";
    public static final String TUTORIAL_LINK = "tutoriallink";
    public static final String TUTORIAL_EVENT = "tutorialevent";
    
    public static final String TUTORIAL_ACTION = "action";
    public static final String TUTORIAL_SOURCE = "src";
    public static final String TUTORIAL_RES = "res";
    public static final String TUTORIAL_WAIT = "wait";
        
    public static final String COLOR_PRIMARY = "primary";
    public static final String COLOR_SECONDARY = "secondary";
    
    public static final String LEVEL_ID = "id";
    public static final String LEVEL_AUTHOR = "author";
    
    public static final String GATE_ID = "id";
    public static final String GATE_X = "x";
    public static final String GATE_Y = "y";
    public static final String GATE_PINS = "pins";
    public static final String GATE_TYPE = "type";
    public static final String GATE_HELPER = "helper";
    public static final String GATE_NOBUS = "nobus";
    
    public static final String LINK_SRC = "src";
    public static final String LINK_TRG = "trg";
    public static final String LINK_PIN = "pin";
    
    public static final String TYPE_INPUT = "IN";
    public static final String TYPE_OUTPUT = "OUT";
    public static final String TYPE_AND = "AND";
    public static final String TYPE_OR = "OR";
    public static final String TYPE_NOT = "NOT";
    public static final String TYPE_XOR = "XOR";
    public static final String TYPE_BOMB = "BOMB";
    public static final String TYPE_MENU = "MENU";
    public static final String TYPE_HELPER = "HELPER";
    public static final String TYPE_FUSE = "FUSE";
    public static final String TYPE_FLIP = "FLIP";
    public static final String TYPE_NOR = "NOR";
    public static final String TYPE_NAND = "NAND";
    
    public static final String STRING_NOT_FOUND = "<ERR_LOCAL_STRING_NOT_FOUND>";
    
    public static final float WIDTH = 32f;
    public static final float HEIGHT = 32f;
    
    public static final long DEFAULT_INITIAL_TIME = 300000000;
    public static final long LOCKING_INITIAL_TIME = 300000000;
    public static final long LOCKING_TIME_BONUS = 15000000;    
    public static final int LOCKING_PATTERNS_PER_LEVEL = 10;
    public static final float WARNING_TIME = 100000000.0f;
    
    public static final float SOLVE_TIME_MAX = 1000000000.0f;
    
    public static final int PROPAGATION_FRAMES = 5;// 5;
    public static final int TRANSITION_FRAMES = 3;//5;
    public static final float TRANSITION_QUOTIENT = 30.0f;
    public static final int LINK_SPEED = 25;//30;// 12;// 8; //
    
    public static final int BACKGROUND_SHAPE_MIN_SPEED = 1;
    public static final int BACKGROUND_SHAPE_MAX_SPEED = 2;
    
    public static final int BACKGROUND_SHAPE_MIN_RADIUS = 300;
    public static final int BACKGROUND_SHAPE_MAX_RADIUS = 400;
    
    public static final int DIALOG_LUMINOSITY_1 = 100;
    public static final int DIALOG_LUMINOSITY_2 = 255;
    
    public static final int LINK_LUMINOSITY_1 = 255;
    public static final int LINK_LUMINOSITY_2 = 180;
    public static final int LINK_LUMINOSITY_3 = 100;
    public static final int LINK_LUMINOSITY_4 = 30;
    public static final int LINK_LUMINOSITY_5 = 10;
    
    public static final int DEFAULT_PRIMARY = 200;
    public static final int DEFAULT_SECONDARY = 170;
    
    public static final int TEXT_OFFSET = 20;
    public static final int VICTORY_PAUSE = 200;
    public static final int NEWS_PAUSE = 600;
    
    public static final int THREAD_SLEEP_GUI = 2;
    public static final int THREAD_SLEEP_TICK = 6;
    
    public static final int ORGANIC_RADIUS = 1600;
    public static final float HALO_RADIUS = 8.0f;
    
    public static final float FONT_SIZE_DIALOG = 20.0f;
    public static final float FONT_SIZE_BUTTON = 16.0f;
    
    public static final float BACKGROUND_ANGULAR_SPEED = 0.2f;
    
    public static final int EVENT_ACTION_NOTHING = 0;
    public static final int EVENT_ACTION_SWITCH = 1;    
    public static final int EVENT_ACTION_HELPER = 2;    
    
    public static final String BANNER_TITLE = "glyph";
    
    public static final int SOUND_ALERT_A = 1;
    public static final int SOUND_ALERT_B = 2;
    public static final int SOUND_ALERT_C = 3;
    public static final int SOUND_ALERT_D = 4;
    public static final int SOUND_ALERT_E = 5;
    public static final int SOUND_ALERT_F = 6;
    
    public static final int SOUND_BEEP_A = 7; 
    public static final int SOUND_BEEP_B = 8;
    public static final int SOUND_BEEP_C = 9;
    public static final int SOUND_BEEP_D = 10;
    public static final int SOUND_BEEP_E = 11;
    public static final int SOUND_BEEP_F = 12;
    public static final int SOUND_BEEP_G = 13;
    public static final int SOUND_BEEP_H = 14;
    public static final int SOUND_BEEP_I = 15;
            
    public static final int SOUND_BOMB_EXPLODE = 16;
    
    public static final int EDITOR_PIXEL_SNAP = 10;
    
    public enum Button {
        OK,
        CANCEL,
        NEXT_LEVEL,
        NEXT_CHAPTER,
        NEXT_PAGE,
        MENU,
        ABORT,
        NOTHING,
        NEW_GAME,
        RETRY,
        EDIT_PICK_GATE,
        EDIT_VERIFY,
        EDIT_SAVE,
        EDIT_TEST,
        EDIT_OPEN,
        EDIT_RETURN
    }
    
    public enum Transition {
        FULL_ON,
        FULL_OFF,
        SWITCHING_ON,
        SWITCHING_OFF,
        UNDETERMINED,
        VICTORY,
        DETONATED
    }
    
    public enum EditingMode {
        NOTHING,
        PLACING_GATE,
        MOVING_GATE,
        PLACING_LINK
    }
    
    public enum Segment {
        ON_SEGMENT,
        OFF_SEGMENT,
        WHOLE_SEGMENT
    }
    
    public enum Dialogs {
        MESSAGE_ONLY,
        LEVEL_SUCCESS,
        LEVEL_FAILURE,
        CHAPTER_SUCCESS,
        GAME_SUCCESS,
        TEST_SUCCESS,
        TEST_FAILURE,
        EXIT_TO_MAIN,
        EXIT_TO_EDITOR,
        EXIT_TO_MAIN_UNSAVED,
        EXIT_TO_HOME,
        RESET_PROFILE_CONFIRM,
        EXCEPTION,
        TUTORIAL,
        RESET_LEVEL,
        MENU,
        TOOLBOX
    }
    
    public enum LevelState {
        TUTORIAL,
        UNSOLVED,
        SOLVED,
        SUMMARY,
        FAILED
    }
    
    public enum Mode {
        ORIGINAL,
        LOCKING_PATTERN,
        ONE_TOUCH,
        TUTORIAL
    }
    
    public enum SwitchingContext {
        REQUIRE_RESOLUTION,
        DISREGARD_RESOLUTION
    }
    
    public static boolean inBounds(float x, float y, Gate gate) {
        return (x > (gate.getX()) && x < (gate.getX() + gate.getWidth()) && y > (gate.getY()) && y < (gate.getY() + gate.getHeight()));
    }
    
    public static boolean inBounds(float x, float y, RectF rect) {
        return (x > (rect.left) && x < (rect.right) && y > (rect.top) && y < (rect.bottom));
    }
    
    public static ColorX getAverageColor(ColorX cx1, ColorX cx2) {
        int aa = (cx1.a() + cx2.a() / 2);
        int ra = (cx1.r() + cx2.r() / 2);
        int ga = (cx1.g() + cx2.g() / 2);
        int ba = (cx1.b() + cx2.b() / 2);
        return new ColorX(aa, ra, ga, ba);
    }
    
    public static ColorX adjustBrightness(ColorX colorX, int value) {
        int a = 255;
        int r = Math.max(Math.min((colorX.r() + value), 255), 0);
        int g = Math.max(Math.min((colorX.g() + value), 255), 0);
        int b = Math.max(Math.min((colorX.b() + value), 255), 0);
        return new ColorX(a, r, g, b);
    }
    
    public static int getMaxOfRGB(ColorX colorX) {
        return Math.max(Math.max(colorX.r(), colorX.g()), colorX.b());
    }
    
    public static ColorMatrix getAdjustedMatrix(ColorX cX) {
        ColorMatrix out = new ColorMatrix();
        float divisor = 6.5f;
        float multiplier = 1.0f;
        float rAdj = (float)cX.r() / (float)getMaxOfRGB(cX) * 2.25f;
        float gAdj = (float)cX.g() / (float)getMaxOfRGB(cX) * 2.25f;
        float bAdj = (float)cX.b() / (float)getMaxOfRGB(cX) * 2.25f;
        float[] src = {rAdj, 0.0f, 0.0f, 0.0f, cX.r() / divisor,
                       0.0f, gAdj, 0.0f, 0.0f, cX.g() / divisor,
                       0.0f, 0.0f, bAdj, 0.0f, cX.b() / divisor,
                       0.0f, 0.0f, 0.0f, 1.0f, 0.0f}; // * (cX.r() / getMaxOfRGB(cX))
        out.set(src);
        return out;
    }
            
    
    public static RectF getScaledCenteredRect(SurfaceView s, float l, float h) {
        RectF r = new RectF();
        //float height = s.getHeight();
        //float width = s.getWidth();
        float height = Library.REFERENCE_HEIGHT;
        float width = Library.REFERENCE_WIDTH;
        r.left = width / 2 - (width * (l/2));
        r.top = height / 2 - (height * (h/2));
        r.right = width / 2 + (width * (l/2));
        r.bottom = height / 2 + (height * (h/2));
        
        //r.left = r.left * (float)s.getScaleX();
        //r.top = r.top * (float)s.getScaleY();
        //r.right = r.right * (float)s.getScaleX();
        //r.bottom = r.bottom * (float)s.getScaleY();
        return r;
    }
    
    public static RectF getScaledRect(RectF R, float l, float t, float r, float b) {
        RectF o = new RectF();
        if ((l + r <= 1.0f) && (t + b <= 1.0f)) {
            o.left = R.left + (R.width() * l);
            o.top = R.top + (R.height() * t);
            o.right = R.right - (R.width() * r);
            o.bottom = R.bottom - (R.height() * b);
        }
        return o;
    }
    
    public static ColorX stringToColorX(String string) {
        try {
            int color = Integer.parseInt(string, 16);
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = (color >> 0) & 0xFF;
            return new ColorX(255, r, g, b);
        } catch (NumberFormatException e) {
            return new ColorX(255, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY, Library.DEFAULT_PRIMARY);
        }
        
    }
    
    public static List<Chapter> getChaptersFromType(Panel P, String Type) throws Exception {
        int chapterIndex = 0;
        int levelIndex = 0 ;
        List<Chapter> chapters = new ArrayList<Chapter>();          
        try {
            InputStream inStr = P.getResources().openRawResource(R.raw.level);
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(inStr, null);
            
            Node node = doc.getDocumentElement();   
            NodeList typeNodes = node.getChildNodes();  
            for (int i=0; i < typeNodes.getLength(); i++) {      
                if (typeNodes.item(i).getNodeName().equals(Type)) {
                    NodeList chapterNodes = typeNodes.item(i).getChildNodes();
                    for (int j=0; j < chapterNodes.getLength(); chapterIndex = j++) {  
                        if (chapterNodes.item(j).getNodeName().equals(Library.CHAPTER)) {
                            Chapter chapter = new Chapter();
                            NodeList levelNodes = chapterNodes.item(j).getChildNodes();
                            for (int k=0; k < levelNodes.getLength(); levelIndex = k++) {  
                                if (levelNodes.item(k).getNodeName().equals(Library.LEVEL)) {
                                    chapter.getLevels().add(new Level((Element)levelNodes.item(k), false));
                                }
                            }
                            chapters.add(chapter);
                        }
                    }    
                }
            }
            inStr.close();
            return chapters;
        } catch (Exception e) {
            throw new Exception (String.format(P.getLocaleString(au.com.gman.glyph.R.string.editor_saved_as), chapterIndex, levelIndex));
        }
    }
    
    public static float scaleX(Panel xP, float X) { return (xP.getAWidth() / Library.REFERENCE_WIDTH * X); }
    public static float scaleY(Panel xP, float Y) { return (xP.getAHeight() / Library.REFERENCE_HEIGHT * Y); }
    
    public static int maxInputsPerGate(Gate gate) {
        if (gate.getClass().equals(GateNOT.class) || gate.getClass().equals(GateINPUT.class) || gate.getClass().equals(GateOUTPUT.class)) { return 1; }
        return 4;
    }
    
    public static int maxOutputsPerGate(Gate gate) {
        if (gate.getClass().equals(GateOUTPUT.class) || gate.getClass().equals(GateBOMB.class)) { return 0; }
        return 4;
    }
    
    public static int verifyLevel(Level level) {
        int possibles = 0;
        int outputs = 0;
        double combinations = 0;
        ArrayList<GateINPUT> inputList = new ArrayList<GateINPUT>();
        
        // Assemble list of input switches
        for(Gate gate : level.getGates().values()) {
            if (gate.getClass().equals(GateINPUT.class)) {
                inputList.add(GateINPUT.class.cast(gate));
            }
            if (gate.getClass().equals(GateOUTPUT.class)) {
                outputs++;
            }
        }
        
        if (outputs > 0) {
            if (inputList.size() > 0) {
                combinations = Math.pow(2.0f, inputList.size());
                for (int i=0; i < combinations; i++) {
                    for (int j=0; j < inputList.size(); j++)
                    {
                        inputList.get(j).Switch((i & 1 << j) > 0, Library.SwitchingContext.DISREGARD_RESOLUTION);
                    }
                    if (evaluatePuzzle(level).equals(LevelState.SOLVED)) {
                        possibles++;
                    }
                    // Need to restore fuses and bombs
                    level.restore();
                }
            } else {
                // throw an exception
            }
        } else {
            // throw an exception
        }
        level.restore();
        resetLinks(level);
        
        return possibles;
    }
    
    public static Library.LevelState evaluatePuzzle(Level level) {
        for (Gate gate : level.getGates().values()) {
            if (gate.getClass().equals(GateBOMB.class)) {
                if (gate.getOutput().getState()) {
                    return LevelState.FAILED;
                }                
            }
        }
        // Then check outputs
        boolean s = true;
        for (Gate gate : level.getGates().values()) {
            if (gate.getClass().equals(GateOUTPUT.class)) {
                s = s && gate.getOutput().getState();                
            }
        }
        if (s) {
            return LevelState.SOLVED;
        }
        return LevelState.UNSOLVED;
    }
    
    public static String saveLevelToFile(Panel xP, Level level) throws Exception {
        Library.tidyGates(level);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateAndTime = sdf.format(new Date());
        String fileName = Library.LEVEL_FILE_PREFIX + currentDateAndTime + Library.LEVEL_FILE_SUFFIX;
        
        String levelXml = Level.RenderXML(level, xP);                
        File outDir = new File(Environment.getExternalStorageDirectory() + System.getProperty("file.separator") + Library.LEVEL_FILE_FOLDER);
        if (!(outDir.exists())) {
            if (!(outDir.mkdir())) {
                // throw excpetion
                throw new Exception (xP.getLocaleString(au.com.gman.glyph.R.string.editor_cannot_create_directory));
            }
        }
        // Write the level data to the file
        File outFile = new File(outDir.getPath(), fileName);
        FileWriter fOut = new FileWriter(outFile);
        try {
            fOut.write(levelXml);
        } catch (Exception e) {
        } finally {
            fOut.close();
        }
        
        // Write a .nomedia file to the same directory
        outFile = new File(outDir.getPath(), Library.LEVEL_FILE_NOMEDIA);
        fOut = new FileWriter(outFile);
        try {
            fOut.write("");
        } catch (Exception e) {
        } finally {
            fOut.close();
        }

        return fileName;
    }
    
    public static void tidyGates(Level level) {
        if (!(level == null)) {
            for (Gate gate : level.getGates().values()) {
                gate.reOrderPinsAndTrim();
                for (IO io : gate.getInputs()) {
                    for (Link link : io.getLinks()) {
                        link.routePath();
                    }
                }
                if (!(gate.getOutput() == null)) {
                    for(Link link : gate.getOutput().getLinks()) {
                        link.routePath();
                    }
                }
            }
        }
    }
    
    public static Point checkAlignments(float x, float y, Gate source, Level level, List<Point> lines) {
        Point alignedPoint = new Point(x, y);
        if (!(level == null)) {
            if (!(lines == null)) { lines.clear(); }
            // Check X positions with other gates to snap to
            for (Gate gate : level.getGates().values()) {
                if (!(gate.equals(source))) {
                    if (Math.abs(gate.getXc() - x) < Library.EDITOR_PIXEL_SNAP) {
                        alignedPoint.x = gate.getXc();
                        lines.add(new Point(source.getXc(), source.getYc()));
                        lines.add(new Point(gate.getXc(), gate.getYc()));
                        break;
                    }
                }
            }
            
            // Check Y positions with other IOs to snap to
            // Check source output alignments
            for (Gate gate : level.getGates().values()) {
                if (!(gate.equals(source))) {
                    if (gate.getXc() > source.getXc()) {
                        for (IO io : gate.getInputs()) {
                            for (Link link : io.getLinks()) {
                                if ((link.getSrcGate().equals(gate) || link.getTrgGate().equals(source)) || (link.getSrcGate().equals(source) || link.getTrgGate().equals(gate))) {
                                    if (Math.abs((gate.getY() + io.getY()) - (((y - (source.getHeight() / 2)) + source.getOutput().getY()))) < Library.EDITOR_PIXEL_SNAP) {
                                        alignedPoint.y = (gate.getY() + io.getY()) - source.getOutput().getY() + (source.getHeight() / 2);
                                        lines.add(new Point(source.getX() + source.getOutput().getX(), source.getY() + source.getOutput().getY()));
                                        lines.add(new Point(gate.getX() + io.getX(), gate.getY() + io.getY()));
                                        return alignedPoint;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Check source input alignments
            for (Gate gate : level.getGates().values()) {
                if (!(gate.equals(source))) {
                    for (IO io : source.getInputs()) {
                        for (Link link : io.getLinks()) {
                            if ((link.getSrcGate().equals(gate) || link.getTrgGate().equals(source)) || (link.getSrcGate().equals(source) || link.getTrgGate().equals(gate))) {
                                if (Math.abs((((y - (source.getHeight() / 2)) + io.getY())) - ((gate.getY() + gate.getOutput().getY()))) < Library.EDITOR_PIXEL_SNAP) {
                                    alignedPoint.y = (gate.getY() + gate.getOutput().getY()) - io.getY() + (source.getHeight() / 2);
                                    lines.add(new Point(source.getX() + io.getX(), source.getY() + io.getY()));
                                    lines.add(new Point(gate.getX() + gate.getOutput().getX(), gate.getY() + gate.getOutput().getY()));
                                    return alignedPoint;
                                }
                            }
                        }
                    }
                }
            }
        }
        return alignedPoint;
    }
    
    public static void resetLinks(Level level) {
        if (!(level == null)) {
            for (Link link : level.getLinks().values()) {
                link.reset();
            }
        }
    }
}

