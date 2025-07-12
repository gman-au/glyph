/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.level;

import java.util.ArrayList;
import java.util.List;
import au.com.gman.glyph.level.Level;
/**
 *
 * @author gub
 */
public class Chapter {
    private List<Level> levels;
    
    public List<Level> getLevels() { return levels; }
    
    public Chapter() {
        levels = new ArrayList<Level>();
    }
    
    public void destroy() {
        for (Level level: levels) {
            level.destroy();
        }
        levels = null;
    }
}
