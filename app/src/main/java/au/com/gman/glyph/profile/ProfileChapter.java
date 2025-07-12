/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.profile;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author gub
 */
public class ProfileChapter {
    private List<ProfileLevel> levels;
    
    public List<ProfileLevel> getLevels() { return levels; }
    
    public ProfileChapter() {
        levels = new ArrayList<ProfileLevel>();
    }
    
    public void destroy() {
        for (ProfileLevel profileLevel : levels) {
            profileLevel.destroy();
        }
        levels.clear();
        levels = null;
    }
    
   public boolean getLocked() {
        boolean locked = true;
        for (ProfileLevel profileLevel : levels) {
            locked = locked && (profileLevel.getLocked());
        }
        return locked;
   }
   
   public float getAvgClicks() { 
       float numerator = 0.0f;
       float denominator = 0.0f;
       for (ProfileLevel profileLevel : levels) {
           if (!(profileLevel.getLocked())) {
               denominator++;
               numerator += profileLevel.getClicks();
           }
       }
       if (denominator > 0) { return numerator / denominator; }
       return 0.0f;
   }
   
   public float getAvgNanos() { 
       float numerator = 0.0f;
       float denominator = 0.0f;
       for (ProfileLevel profileLevel : levels) {
           if (!(profileLevel.getLocked())) {
                if (profileLevel.getNanos() > 0.0f) {
                    denominator++;
                    numerator += profileLevel.getNanos();
                }
           }
       }
       if (denominator > 0) { return numerator / denominator; }
       return 0.0f;
   }
   
}
