/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.screens;

import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.MotionEvent;
import au.com.gman.glyph.R;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.engine.SoundController;
import au.com.gman.glyph.dialogs.DialogButton;
import au.com.gman.glyph.level.Chapter;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Point;
import au.com.gman.glyph.profile.ProfileAccount;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author gub
 */
public class ScreenLevelOpen extends ScreenLevelSelect {
    
    private String selectedMsg;
    
    public ScreenLevelOpen(Panel xP, 
            Typeface xFont, 
            SpriteController xSprites,
            SoundController xSounds,
            ProfileAccount xProfile) throws Exception {
        super(xP, xFont, xSprites, xSounds, xProfile);
        initialiseScreen();
        scalePoint = new Point(0.50f, 0.50f);
        translatePoint = new Point(260.0f, 75.0f);
        buttonWidth = 240.0f;
        
        selectedMsg = (xP.getLocaleString(au.com.gman.glyph.R.string.level_none_chosen));
        
        // Load the directory containing the level files
        File levelDir = new File(Environment.getExternalStorageDirectory() + System.getProperty("file.separator") + Library.LEVEL_FILE_FOLDER);
        if (!(levelDir.exists())) {
            if (!(levelDir.mkdir())) {
                // throw excpetion
                throw new Exception (xP.getLocaleString(au.com.gman.glyph.R.string.editor_cannot_create_directory));
            }
        }
        
        File[] levelFiles = levelDir.listFiles();
        for (int rows = 0; rows < levelFiles.length; rows++) {
            if (levelFiles[rows].isFile()) {
                String caption;
                caption = levelFiles[rows].getName();
                buttons.add(new DialogButton(new RectF(20, 55 + (rows*50), 20 + buttonWidth, 90 + (rows*50)), primary, secondary, 
                        caption, Library.Button.NOTHING, font, false));   
                vertBottomEdge = 55 + (rows*50);
            }
        }
        
        bannerTitle = new DialogButton(new RectF(0, -5.0f, Library.REFERENCE_WIDTH, 40.0f), 
                new ColorX(0, 255, 255, 255), new ColorX(0, 255, 255, 255), xP.getLocaleString(au.com.gman.glyph.R.string.title_select_level), Library.Button.NOTHING, font, 30.0f);
        launch = new DialogButton(new RectF(460.0f, 280.0f, 520.0f, 310.0f), 
                primary, secondary, xP.getLocaleString(au.com.gman.glyph.R.string.menu_launch), Library.Button.NOTHING, font);
    }
    
    @Override
    public void navigateBack() {
        P.toEditor();
    }
    
    @Override
    public void drawObjects(Canvas c, Paint p) throws Exception { 
        super.drawObjects(c, p);
        if (selectedLevel == null) {
            p.setTextSize(20.0f);
            p.setTypeface(font);
            p.setColor(Color.WHITE);
            p.setXfermode(null);
            p.setShadowLayer(0.75f, 0.5f, 0.5f, Color.BLACK);
            c.drawText(selectedMsg, translatePoint.x + ((Library.REFERENCE_WIDTH - translatePoint.x) / 2) -  (p.measureText(selectedMsg) / 2), Library.REFERENCE_HEIGHT / 2, p);
            p.clearShadowLayer();
        }
    }
    
    @Override
    protected int previewLevel(DialogButton d) {
        BufferedReader bfr = null;;
        StringBuilder XML;
        try {
            File outDir = new File(Environment.getExternalStorageDirectory() + System.getProperty("file.separator") + Library.LEVEL_FILE_FOLDER);
            File outFile = new File(outDir.getPath(), d.getText());
            bfr = new BufferedReader(new FileReader(outFile));
            XML = new StringBuilder();
            String line;
            while ((line = bfr.readLine()) != null) {
                XML.append(line);
            }
        
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(outFile);//XML.toString());
            Node node = doc.getDocumentElement();   
            selectedLevel = new Level((Element)node, true);
        } catch (Exception e) {
            selectedLevel = null;
            selectedMsg = (P.getLocaleString(au.com.gman.glyph.R.string.level_invalid));
        } finally {
            if (!(bfr == null)) { 
                try { bfr.close(); } catch (Exception e) { }
            }
        }
            
        sounds.playSound(Library.SOUND_BEEP_I);
        return -1;        
    }
    
    @Override
    protected void navigateLevel(int level) {
        try {
            if (!(selectedLevel == null)) {
                P.loadLevelInEditor(selectedLevel);
            }
        } catch (Exception e) {
            P.exceptionDialog(e);
        }
    }
    
    @Override
    public void destroy() {
        if (!(bannerTitle == null)) { bannerTitle.destroy(); }
        bannerTitle = null;
        if (!(buttons == null)) {
            for (DialogButton button : buttons) {
                button.destroy();
            }
            buttons.clear();
        }
        buttons = null;        
        super.destroy();
    }
    
    @Override
    public Level getCurrentLevel() { return null; }
    @Override
    public Chapter getCurrentChapter() { return null; }
}


