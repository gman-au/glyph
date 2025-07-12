/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.engine;

import au.com.gman.glyph.level.Library;

/**
 *
 * @author gub
 */
public class TickThread extends Thread {
        
        private Panel _panel;
        private boolean _run = false;
 
        public TickThread() {}
        
        public void setPanel(Panel panel) {
            _panel = panel;
        }
        
        public void closePanel() {
            _panel = null;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        @Override
        public void run() {
            long nanos = 0;
            if (!(_panel == null)) {
                while (_run) {
                    try {
                        nanos = System.nanoTime();
                        _panel.tickAll();
                        try { 
                            sleep(Library.THREAD_SLEEP_TICK); 
                            nanos = System.nanoTime() - nanos;
                            _panel.setScreenNanos(nanos);
                        } catch(Exception e) { }
                    } catch(Exception e) { }
                }
            } 
        }
    }
