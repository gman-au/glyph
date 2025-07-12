/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Point;

/**
 *
 * @author gub
 */
public class GUIThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
        
        public GUIThread() {
        }
 
        public void setPanel(Panel panel) {
            _panel = panel;
            _surfaceHolder = panel.getHolder();
        }
        
        public void closePanel() {
            _panel = null;
            _surfaceHolder = null;
        }
        
        public void setRunning(boolean run) {
            _run = run;
        }
 
        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }
 
        @Override
        public void run() {
            if (!(_panel == null)) {
                Canvas c;
                while (_run) {
                    c = null;
                    try {
                        c = _surfaceHolder.lockCanvas(null);
                        synchronized (_surfaceHolder) {
                            //_panel.onDraw(c);
                            _panel.onDraw(c);                            
                        }            
                        try{ 
                            sleep(Library.THREAD_SLEEP_GUI); 
                        } catch(Exception e) {
                        }
                    } finally {
                        // do this in a finally so that if an exception is thrown
                        // during the above, we don't leave the Surface in an
                        // inconsistent state
                        if (c != null) {
                            _surfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
            } 
        }
    }
