package au.com.gman.glyph.profile;

import org.w3c.dom.Element;


public class ProfileLevel {

   private String id;
   private boolean locked;
   
   private long nanos;
   private int clicks;
   private int patterns;   
   
   public String getId() { return id; }
   public int getClicks() { return clicks; }
   public long getNanos() { return nanos; }
   public boolean getLocked() { return locked; }
   public int getPatterns() { return patterns; }
   
   public void setId(String Id) { id = Id; }
   public void setNanos(long Nanos) { nanos = Nanos; }
   public void setClicks(int Clicks) { clicks = Clicks; }
   public void setLocked(boolean Locked) { locked = Locked; }
   public void setPatterns(int Patterns) { patterns = Patterns; }
   
   public ProfileLevel() {
        nanos = 0;
        clicks = 0;
        id = null;
        locked = true;
   }
   
   public void destroy() {}
}

