/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import android.R;
import android.graphics.ColorMatrixColorFilter;
import au.com.gman.glyph.dialogs.Dialog;
import au.com.gman.glyph.dialogs.Dialog_Exception;
import au.com.gman.glyph.dialogs.Dialog_Tutorial_Next;
import au.com.gman.glyph.dialogs.Dialog_ToolBox;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Link;
import au.com.gman.glyph.level.Event;
import au.com.gman.glyph.level.Library;
import android.graphics.BlurMaskFilter;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.Glyph;
import au.com.gman.glyph.gate.Gate;
import au.com.gman.glyph.level.Point;
import au.com.gman.glyph.screens.Screen;
import au.com.gman.glyph.screens.ScreenGame;
import au.com.gman.glyph.screens.ScreenOptions;
import au.com.gman.glyph.screens.ScreenLevelSelect;
import au.com.gman.glyph.screens.ScreenLevelOpen;
import au.com.gman.glyph.screens.ScreenChapterSelect;
import au.com.gman.glyph.screens.ScreenMenu;
import au.com.gman.glyph.screens.ScreenTutorial;
import au.com.gman.glyph.screens.ScreenLocking;
import au.com.gman.glyph.screens.ScreenGameTestBed;
import au.com.gman.glyph.screens.ScreenEditor;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.level.Library.Dialogs;
import java.util.ArrayList;
import au.com.gman.glyph.profile.ProfileAccount;
import android.graphics.PixelFormat;
import java.util.HashMap;
/**
 *
 * @author gub
 */
public class Panel extends SurfaceView implements SurfaceHolder.Callback {
    
    private boolean screenReady;
    protected boolean interaction;
    protected boolean destroying;
    
    protected GUIThread _GUI;
    protected TickThread _Tick;
    
    protected Paint paintO;
    protected Paint paintD;
    
    protected SpriteController sprites;
    
    protected Dialog dialog;
    
    private BlurMaskFilter bm;
    protected Typeface font;
    
    protected ColorX primary;
    protected ColorX secondary;

    private float canvasFade;
    private Point scaleRatio;
    protected Glyph G;
    
    protected HashMap<String, Integer> localeStrings;
    
    protected SoundController sounds;
    protected OrganicSound music;
    protected OrganicBackground background;
    protected ProfileAccount profile;
    
    private ScreenGame screenGame;
    private Screen screenMenu;
    private ScreenEditor screenEdit;
    private Screen screenCurrent;
    private Rect screenSize;
    
    protected ColorMatrixColorFilter lcf;
    
    // This point contains the (portrait) dimensions
    public float getAWidth() { return (float)screenSize.width(); }
    public float getAHeight() { return (float)screenSize.height(); }
    
    public ColorMatrixColorFilter getColorFilter() { return lcf;}
    
    public long getScreenNanos() { return screenNanos; }
    public void setScreenNanos(long ScreenNanos) { screenNanos = ScreenNanos; }
    public HashMap<String, Integer> getLocaleStrings() { return localeStrings; }
    
    protected long screenNanos = 0;
    
    public Panel(Glyph g) throws Exception {                
        super(g);
        initialiseStringMap();
        getHolder().setFormat(PixelFormat.RGBA_8888);

        G = g;
        screenSize = new Rect();
        
        G.getWindowManager().getDefaultDisplay().getRectSize(screenSize);
        
        scaleRatio = new Point(screenSize.width() / Library.REFERENCE_WIDTH, screenSize.height() / Library.REFERENCE_HEIGHT);
        
        //this.setScaleX(1/scaleRatio.x);
        //this.setScaleY(1/scaleRatio.y);
        
        screenReady = false;
        
        getHolder().addCallback(this);
        _GUI = new GUIThread();
        _GUI.setPanel(this);
        _Tick = new TickThread();
        _Tick.setPanel(this);
        setFocusable(true);
        
        paintO = new Paint();       
        paintO.setStrokeWidth(2.0f);
        paintO.setStrokeCap(Paint.Cap.ROUND);
        paintO.setStrokeMiter(20.0f); 
        paintO.setAntiAlias(true);
        paintO.setFilterBitmap(true);
        
        font = Typeface.createFromAsset(G.getAssets(), "fonts/forgotbi.ttf");
                
        paintD = new Paint();        
        paintD.setTextSize(Library.FONT_SIZE_BUTTON);                
        paintD.setAntiAlias(true);
        paintD.setFilterBitmap(true);
        
        sprites = new SpriteController(getResources());        
        
        dialog = null;
        
        primary = new ColorX(255, 229, 180, 139);
        secondary = new ColorX(255, 114, 185, 131);
        lcf = new ColorMatrixColorFilter(Library.getAdjustedMatrix(secondary));
        bm = new BlurMaskFilter(5.0f, BlurMaskFilter.Blur.SOLID);
        applyColors(primary, secondary);
     
        sounds = G.getSounds();
        music = G.getMusic();
        profile = ProfileAccount.initialiseProfile(this);
        
        setFocusableInTouchMode(true);
        requestFocus();
                
        screenMenu = new ScreenMenu(this, font, sprites, sounds, profile, false);
        
        canvasFade = 0.0f;
        
        screenCurrent = screenMenu;
        screenReady = true;
    }
    
    public void setMusicEnabled(boolean value) {
        if (value != music.getEnabled()) {
            music.setEnabled(value);
        }
    }
    
    public void setSoundsEnabled(boolean value) {
        if (value != sounds.getEnabled()) {
            sounds.setEnabled(value);
        }
    }
    
    public void applyColors(ColorX CXP, ColorX CXS) {
        primary = CXP;
        secondary = CXS;
        background = new OrganicBackground(CXP, CXS, new Point(Library.REFERENCE_WIDTH, Library.REFERENCE_HEIGHT));
        lcf = new ColorMatrixColorFilter(Library.getAdjustedMatrix(CXS));
    }
    
    @Override
    public void onDraw(Canvas c) {
        if (screenReady) {
            try {
                // Rescale the whole screen based on the device dimensions
                c.scale(scaleRatio.x, scaleRatio.y);
                
                // Background
                if (!(background == null)) {
                    Screen.drawOrganicBackground(c, background, this);
                }
                // Screen Objects
                if (!(screenCurrent == null)) {
                    screenCurrent.drawObjects(c, paintO);
                }
                // Dialog if applicable
                if (!(dialog == null)) {            
                    dialog.drawDialog(c, paintD, sprites);
                    paintO.setAlpha(50);
                    blurOn();
                } else {
                    paintO.setAlpha(255);
                    blurOff();
                } 
                // Fade if applicable
                if (canvasFade < 0.98f) {            
                    c.drawColor(new ColorX((int)((1.0f - canvasFade) * 255), 0, 0, 0).getColor(), PorterDuff.Mode.DARKEN);
                }   
                // Rescale the whole screen based on the device dimensions
                c.scale(1/scaleRatio.x, 1/scaleRatio.y);
            } catch(Exception e) {}
        }
    }
    
    public void blurOn() {
        if (!(bm == null)) {paintO.setMaskFilter(bm); }   
    }
    
    public void blurOff() {
        paintO.setMaskFilter(null);
    }
    
    public void exceptionDialog(Exception e) {
        screenReady = true;
        dialog = new Dialog_Exception(primary, secondary, e.getMessage(), Library.getScaledCenteredRect(this, 0.85f, 0.85f), font, Library.Dialogs.EXCEPTION, this);
    }
    
    public void displayDialog(Library.Dialogs d) {        
        displayDialog(d, null);
    }
    
    public void displayDialog(Library.Dialogs d, String msg) {        
        if (!(screenCurrent == null)) {
            sounds.playSound(Library.SOUND_BEEP_G);
            dialog = Dialog.getDialogFromType(d, primary, secondary, font, this, screenCurrent.getCurrentLevel(), profile, sprites, msg);
        }
    }
    
    public void displayTutorial(HashMap<String, Gate> gates, HashMap<String, Link> links, ArrayList<Event> events) {
        if (!(screenGame == null)) {
            // Only display the tutorial if the puzzle is still unsolved
            if (screenGame.getLevelState().equals(Library.LevelState.UNSOLVED)) {
                screenGame.setLevelState(Library.LevelState.TUTORIAL);
                sounds.playSound(Library.SOUND_BEEP_E);
                dialog = new Dialog_Tutorial_Next(primary, secondary, "test", Library.getScaledCenteredRect(this, 0.85f, 0.85f), font, Library.Dialogs.TUTORIAL,
                    gates, links, events, this);
            }
        }
    }
    
    protected void tickAll() {
        if (screenReady) {
            try {            
                if (!(music == null)) { music.tick(); }            
                if (!(screenCurrent == null)) {
                    if (dialog == null) {
                        screenCurrent.tickObjects();
                    }
                    else {
                        dialog.tick();
                    }
                }
                if (!(background == null)) { background.tick(); }
                if (canvasFade < 0.98f) {            
                    canvasFade += 0.005f;
                }
            } catch (Exception e) {
                //exceptionDialog(e);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenReady = true;
        if (!(_GUI.isAlive())) {
            _GUI.setRunning(true);
            _GUI.start();
        }
        if (!(_Tick.isAlive())) {
            _Tick.setRunning(true);
            _Tick.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        screenReady = false;
    }
    
    protected void exit() {
        try {
            screenReady = false;
            if (!(screenGame == null)) { screenGame.destroy(); }
            if (!(screenMenu == null)) { screenMenu.destroy(); }
            if (!(screenEdit == null)) { screenEdit.destroy(); }
            boolean retry = true;
            _GUI.setRunning(false);
            _Tick.setRunning(false);
            while (retry) {
                try {
                    _GUI.join();
                    _Tick.join();
                    retry = false;                
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        } catch (Exception e) { 
        } finally {
            G.terminate();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) { 
        event.setLocation(event.getX() / scaleRatio.x, event.getY() / scaleRatio.y);
                
        if (!(dialog == null)) {
            handleButtonEvent(dialog.handleDialogEvent(event));
            return true;
        }
        if (!(screenCurrent == null)) {
            screenCurrent.handleTouchEvent(event);            
            return true;
        }
        return true;
    }

    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dialog == null) {
                if (screenCurrent.equals(screenMenu)) { 
                    if (screenMenu.getClass().equals(ScreenMenu.class)) { displayDialog(Dialogs.EXIT_TO_HOME); return true; }
                    if (screenMenu.getClass().equals(ScreenChapterSelect.class)) { toMain(); return true; } 
                    if (screenMenu.getClass().equals(ScreenOptions.class)) { toMain(); return true; } 
                    if (screenMenu.getClass().equals(ScreenLevelSelect.class)) { ((ScreenLevelSelect)screenMenu).navigateBack(); return true; }
                    if (screenMenu.getClass().equals(ScreenLevelOpen.class)) { toEditor(); return true; }
                }
                if (screenCurrent.equals(screenGame)) {
                    if (screenGame.getClass().equals(ScreenGameTestBed.class)) { displayDialog(Dialogs.EXIT_TO_EDITOR); return true; }
                    displayDialog(Dialogs.EXIT_TO_MAIN); 
                }
                if (screenCurrent.equals(screenEdit)) { displayDialog(Dialogs.EXIT_TO_MAIN_UNSAVED); }
            } else {
                if (dialog.getType().equals(Dialogs.EXIT_TO_MAIN) || 
                        dialog.getType().equals(Dialogs.EXIT_TO_HOME) || 
                        dialog.getType().equals(Dialogs.EXIT_TO_EDITOR) ||
                        dialog.getType().equals(Dialogs.TOOLBOX)) { handleButtonEvent(Button.CANCEL); }
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (screenCurrent.getClass().equals(ScreenEditor.class)) { displayDialog(Dialogs.TOOLBOX); }
        }
        return super.onKeyDown(keyCode, event);
    }   
    
    private void resetProfile() throws Exception {
        profile = ProfileAccount.resetProfile(this);
        if (!(dialog == null)) { dialog.destroy(); }
        dialog = null; 
        // Because we have reset the profile we can't continue or reset
        if (!(screenGame == null)) { screenGame.destroy(); }
        screenGame = null;
    }
    
    public void launchEditor() { 
        try {
            screenReady = false;
            if (!(dialog == null)) { dialog.destroy(); }
            dialog = null; 
            // Destroy the game screen to free memory
            if (!(screenGame == null)) { screenGame.destroy(); }
            if (!(screenEdit == null)) { screenEdit.destroy(); }
            screenEdit = new ScreenEditor(this, font, sprites, sounds, profile);
            screenCurrent = screenEdit;
            screenReady = true;
        } catch (Exception e) {
            exceptionDialog(e);
        }
    }
    
    protected void handleButtonEvent(Button button) {   
        try {
            if (button.equals(Button.OK)) {            
                sounds.playSound(Library.SOUND_BEEP_G);
                if (dialog.getType().equals(Dialogs.EXIT_TO_MAIN)) { toMain(); return; }
                if (dialog.getType().equals(Dialogs.EXIT_TO_MAIN_UNSAVED)) { toMain(); return; }
                if (dialog.getType().equals(Dialogs.EXIT_TO_EDITOR)) { toEditor(); return; }
                if (dialog.getType().equals(Dialogs.EXIT_TO_HOME)) { exit(); return; }
                if (dialog.getType().equals(Dialogs.RESET_PROFILE_CONFIRM)) { resetProfile(); return; }
            }
            if (button.equals(Button.ABORT)) {
                sounds.playSound(Library.SOUND_BEEP_E);
                exit();
                return; 
            }
            if (button.equals(Button.NEXT_LEVEL)) {
                sounds.playSound(Library.SOUND_BEEP_E);
                if (!(screenGame.nextLevel())) { return; }
                if (!(dialog == null)) { dialog.destroy(); }
                dialog = null;
                // Load tutorial if applicable
                screenGame.loadTutorial();
                return; 
            }
            if (button.equals(Button.NEXT_CHAPTER)) {
                sounds.playSound(Library.SOUND_BEEP_E);
                if (!(screenGame.nextChapter())) { return; }           
                if (!(dialog == null)) { dialog.destroy(); }
                dialog = null;
                // Load tutorial if applicable
                screenGame.loadTutorial();
                return; 
            }
            if (button.equals(Button.NEXT_PAGE)) {
                sounds.playSound(Library.SOUND_BEEP_E);
                if (!(dialog == null)) {
                    // If tutorial is finished, return to game
                    if (dialog.getType().equals(Dialogs.TUTORIAL)) {
                        if (!(dialog.nextPage())) { 
                            dialog.destroy(); 
                            dialog = null; 
                            screenGame.setLevelState(Library.LevelState.UNSOLVED);
                        }
                    }
                }
                return; 
            }
            if (button.equals(Button.CANCEL)) {
                sounds.playSound(Library.SOUND_BEEP_E);
                if (!(dialog == null)) { dialog.destroy(); }
                dialog = null;
                if ((!(screenGame == null)) && (screenCurrent.equals(screenGame))) {
                    if (screenGame.getLevelState().equals(Library.LevelState.TUTORIAL)) { screenGame.setLevelState(Library.LevelState.UNSOLVED); }
                }
                return; 
            }
            if (button.equals(Button.MENU)) {
                sounds.playSound(Library.SOUND_BEEP_E);                        
                if (!(dialog == null)) {
                    if (dialog.getType().equals(Dialogs.GAME_SUCCESS)) {
                        toMain(false);
                        return;
                    }
                }
                toMain();                            
                return;
            }
            if (button.equals(Button.RETRY)) {
                screenReady = false;
                if (!(dialog == null)) { dialog.destroy(); }
                dialog = null;
                // Locking pattern resets from the start
                if (!(screenGame == null)) {
                    if (screenGame.getClass().equals(ScreenLocking.class)) {
                        newGame(Library.Mode.LOCKING_PATTERN);
                        return;
                    } else {
                        // Otherwise just retry the level
                        screenGame.restartLevel();
                        screenReady = true;
                    }
                }
                continueGame();
                screenReady = true;
                return;
            }
            if (button.equals(Button.EDIT_PICK_GATE)) {
                if (screenCurrent.equals(screenEdit)) {
                    if (!(dialog == null)) {
                        if (dialog.getClass().equals(Dialog_ToolBox.class)) {
                            Gate selectedGate = ((Dialog_ToolBox)dialog).getSelectedGate();
                            screenEdit.pickGateToPlace(selectedGate);
                            dialog.destroy();
                            dialog = null;
                        }
                    }
                }
            }
            if (button.equals(Button.EDIT_VERIFY)) {
                if (screenCurrent.equals(screenEdit)) {
                    if (!(dialog == null)) {
                        if (dialog.getClass().equals(Dialog_ToolBox.class)) {
                            Level workingLevel = screenEdit.getCurrentLevel();
                            if (!(workingLevel == null)) { 
                                int solutions = Library.verifyLevel(workingLevel);                                 
                                dialog.destroy();
                                dialog = null;
                                displayDialog(Dialogs.MESSAGE_ONLY, String.format(getLocaleString(au.com.gman.glyph.R.string.editor_solutions), solutions));
                            }
                            
                        }
                    }
                }
            }
            if (button.equals(Button.EDIT_SAVE)) {
                if (screenCurrent.equals(screenEdit)) {
                    if (!(dialog == null)) {
                        if (dialog.getClass().equals(Dialog_ToolBox.class)) {
                            Level workingLevel = screenEdit.getCurrentLevel();
                            if (!(workingLevel == null)) { 
                                String fileName = Library.saveLevelToFile(this, workingLevel);
                                dialog.destroy();
                                dialog = null;
                                displayDialog(Dialogs.MESSAGE_ONLY, String.format(getLocaleString(au.com.gman.glyph.R.string.editor_saved_as), fileName));
                            }
                        }
                    }
                }
            }
            if (button.equals(Button.EDIT_TEST)) {
                if (screenCurrent.equals(screenEdit)) {
                    if (!(dialog == null)) {
                        if (dialog.getClass().equals(Dialog_ToolBox.class)) {
                            Level workingLevel = screenEdit.getCurrentLevel();
                            if (!(screenGame == null)) {screenGame.destroy(); }
                            dialog.destroy();
                            dialog = null;
                            screenGame = new ScreenGameTestBed(this, font, sprites, sounds, profile, workingLevel);
                            screenCurrent = screenGame;
                        }
                    }
                }
            }
            if (button.equals(Button.EDIT_RETURN)) {
                toEditor();              
            }
            if (button.equals(Button.EDIT_OPEN)) {
                if (screenCurrent.equals(screenEdit)) {
                    if (!(dialog == null)) {
                        if (dialog.getClass().equals(Dialog_ToolBox.class)) {
                            if (!(screenMenu == null)) {screenMenu.destroy(); }
                            dialog.destroy();
                            dialog = null;
                            screenMenu = new ScreenLevelOpen(this, font, sprites, sounds, profile); 
                            screenCurrent = screenMenu;
                        }
                    }
                }
            }
        } catch (Exception e) { exceptionDialog(e); }
    }
    
    public void toEditor() {
        screenReady = false;
        if (!(dialog == null)) { dialog.destroy(); }
        dialog = null;
        if (!(screenGame == null)) { screenGame.destroy(); }
        screenGame = null;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenMenu = null;
        if (!(screenEdit == null)) {
            screenCurrent = screenEdit; 
            // Reset components to undetermined / stateless
            screenEdit.restore();
        } else {
            launchEditor();
        }
        screenReady = true;    
    }

    public void toMain() {        
        toMain((!(screenGame == null)));
    }
    
    public void toMain(boolean ContinueOption) {
        // Write profile back to file
        try { ProfileAccount.writeProfileToFile(profile, this); } catch (Exception e) { this.exceptionDialog(e); }
        // Destroy if editing
        if (!(screenCurrent == null)) {
            if (screenCurrent.equals(screenEdit)) { 
                screenCurrent.destroy(); 
                screenEdit = null;
                screenCurrent = null;
            }
        }
        screenReady = false;
        if (!(dialog == null)) { dialog.destroy(); }
        dialog = null; 
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenMenu = new ScreenMenu(this, font, sprites, sounds, profile, ContinueOption);
        screenCurrent = screenMenu;
        screenReady = true;
    }
    
    private static ScreenGame getScreenFromType(Panel xP, Typeface xFont, SpriteController xSprites, SoundController xSounds, ProfileAccount xPa, String xMode) throws Exception {
        return getScreenFromType(xP, xFont, xSprites, xSounds, xPa, xMode, 1, 1);
    }
    
    private static ScreenGame getScreenFromType(Panel xP, Typeface xFont, SpriteController xSprites, SoundController xSounds, ProfileAccount xPa, String xMode, int xChapter, int xLevel) throws Exception {
        if (xMode.equals(Library.ORIGINAL)) { return new ScreenGame(xP, xFont, xSprites, xSounds, xPa, xChapter, xLevel); }
        else if (xMode.equals(Library.TUTORIAL)) { return new ScreenTutorial(xP, xFont, xSprites, xSounds, xPa, xChapter, xLevel); }
        else if (xMode.equals(Library.LOCKING)) { return new ScreenLocking(xP, xFont, xSprites, xSounds, xPa, xChapter, xLevel); }   
        return null;
    }
    
    private void launchNew(String mode) throws Exception {
        // If all the levels are locked, start at chapter 1, level 1
        if (profile.getLocked(mode)) {
            if (!(screenGame == null)) { screenGame.destroy(); }
            screenGame = Panel.getScreenFromType(this, font, sprites, sounds, profile, mode);
            screenCurrent = screenGame;
            screenReady = true;
            screenGame.loadTutorial();
        } else if (mode.equals(Library.TUTORIAL)) { navigateToLevelSelect(mode, 1); } 
        else { navigateToChapterSelect(mode); }
    }
    
    public void newGame(Library.Mode mode) {
        try { 
            music.setBeatX(false);
            screenReady = false;
            sounds.playSound(Library.SOUND_ALERT_B);        
            if (mode.equals(Library.Mode.ORIGINAL)) {
                launchNew(Library.ORIGINAL);            
                return;
            }
            if (mode.equals(Library.Mode.TUTORIAL)) {
                launchNew(Library.TUTORIAL);
                return;
            }
            // Locking pattern is a progress-based challenge, 
            // so you always have to start from the start
            if (mode.equals(Library.Mode.LOCKING_PATTERN)) {
                // gp delete this section
                launchNew(Library.LOCKING);
                return;
                // end of delete section
                /*
                music.setBeatX(true);
                if (!(screenGame == null)) { screenGame.destroy(); }
                screenGame = Panel.getScreenFromType(this, font, sprites, sounds, profile, Library.LOCKING);
                screenCurrent = screenGame;
                screenReady = true;
                screenGame.loadTutorial();
                return;*/
            }
            screenCurrent = screenMenu;//screenGame;
            screenReady = true;
            // Load tutorial if applicable
            //screenGame.loadTutorial();
        } catch (Exception e) {
            exceptionDialog(e);
        }
    }
    
    public void navigateToOptions() {
        screenReady = false;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenMenu = new ScreenOptions(this, font, sprites, sounds, profile); 
        screenCurrent = screenMenu;
        screenReady = true;
    }
    
    public void navigateToChapterSelect(String mode) {
        screenReady = false;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenMenu = new ScreenChapterSelect(this, font, sprites, sounds, profile, mode); 
        screenCurrent = screenMenu;
        screenReady = true;
    }
    
    public void navigateToLevelSelect(String mode, int chapter) {
        screenReady = false;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenMenu = new ScreenLevelSelect(this, font, sprites, sounds, profile, mode, chapter); 
        screenCurrent = screenMenu;
        screenReady = true;
    }
    
    public void loadLevel(String mode, int chapter, int level) throws Exception {
        screenReady = false;        
        if (!(screenGame == null)) { screenGame.destroy(); }
        screenGame = Panel.getScreenFromType(this, font, sprites, sounds, profile, mode, chapter, level);
        screenCurrent = screenGame;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        screenReady = true;
        screenGame.loadTutorial();
    }
    
    public void loadLevelInEditor(Level level) {
        screenReady = false;
        if (!(screenMenu == null)) { screenMenu.destroy(); }
        if (!(screenEdit == null)) {
            screenEdit.loadLevel(level);
            screenCurrent = screenEdit;    
        }
        screenReady = true;
    }
    
    public void continueGame() {
        if (!(screenGame == null)) {
            if (!(screenGame.getCurrentLevel() == null)) {
                applyColors(screenGame.getCurrentLevel().getPrimary(), screenGame.getCurrentLevel().getSecondary());
            }
            screenReady = false;
            screenCurrent = screenGame;
            screenGame.evaluatePuzzle();
            screenReady = true;
        }
    }
    
    public void destroy() { 
        screenReady = false;
        destroying = true;
        // Clean up
        if (!(dialog == null)) { dialog.destroy(); }
        dialog = null;
        paintO.reset();
        paintO = null;
        paintD.reset();
        paintD = null;
        sprites.destroy();
        sprites = null;
        sounds = null;
        music = null;
        profile.destroy();
        profile = null;
        background.destroy();
        background = null;
        font = null;
        bm = null;
        G = null;
        localeStrings.clear();
        localeStrings = null;
    }
    
    private void initialiseStringMap() {
        localeStrings = new HashMap<String, Integer>();
        localeStrings.put("app_name", au.com.gman.glyph.R.string.app_name);
        localeStrings.put("switch_01", au.com.gman.glyph.R.string.switch_01);
        localeStrings.put("switch_02", au.com.gman.glyph.R.string.switch_02);
        localeStrings.put("switch_03", au.com.gman.glyph.R.string.switch_03);
        localeStrings.put("switch_04", au.com.gman.glyph.R.string.switch_04);
        localeStrings.put("locking_01", au.com.gman.glyph.R.string.locking_01);
        localeStrings.put("locking_02", au.com.gman.glyph.R.string.locking_02);
        localeStrings.put("locking_03", au.com.gman.glyph.R.string.locking_03);
        localeStrings.put("locking_04", au.com.gman.glyph.R.string.locking_04);
        localeStrings.put("locking_05", au.com.gman.glyph.R.string.locking_05);
        localeStrings.put("output_01", au.com.gman.glyph.R.string.output_01);
        localeStrings.put("output_02", au.com.gman.glyph.R.string.output_02);
        localeStrings.put("output_03", au.com.gman.glyph.R.string.output_03);
        localeStrings.put("or_01", au.com.gman.glyph.R.string.or_01);
        localeStrings.put("or_02", au.com.gman.glyph.R.string.or_02);
        localeStrings.put("and_01", au.com.gman.glyph.R.string.and_01);
        localeStrings.put("and_02", au.com.gman.glyph.R.string.and_02);
        localeStrings.put("not_01", au.com.gman.glyph.R.string.not_01);
        localeStrings.put("not_02", au.com.gman.glyph.R.string.not_02);
        localeStrings.put("xor_01", au.com.gman.glyph.R.string.xor_01);
        localeStrings.put("xor_02", au.com.gman.glyph.R.string.xor_02);
        localeStrings.put("xor_03", au.com.gman.glyph.R.string.xor_03);
        localeStrings.put("bomb_01", au.com.gman.glyph.R.string.bomb_01);
        localeStrings.put("bomb_02", au.com.gman.glyph.R.string.bomb_02);
        localeStrings.put("fuse_01", au.com.gman.glyph.R.string.fuse_01);
        localeStrings.put("fuse_02", au.com.gman.glyph.R.string.fuse_02);
    }
    
    public String getLocaleString(int Actual) {
        try {
            return getResources().getString(Actual);
        } catch (Exception ex) {
            return Library.STRING_NOT_FOUND;
        }
    }
    
    public String getLocaleString(String Key) {
        try {
            int max = getLocaleStrings().get(Key);
            return getResources().getString(max);
        } catch (Exception ex) {
            return Library.STRING_NOT_FOUND;
        }
    }
    
    public String translateTitle(String mode) {
        if (mode.equals(Library.TUTORIAL)) { return getLocaleString(au.com.gman.glyph.R.string.menu_tutorial); }
        if (mode.equals(Library.ORIGINAL)) { return getLocaleString(au.com.gman.glyph.R.string.menu_original); }
        if (mode.equals(Library.LOCKING)) { return getLocaleString(au.com.gman.glyph.R.string.menu_locking); }
        return Library.STRING_NOT_FOUND;
    }
    
        
}
