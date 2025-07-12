/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.profile;

import au.com.gman.glyph.R;
import au.com.gman.glyph.level.Level;
import au.com.gman.glyph.level.Library;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.FileOutputStream;
import android.content.Context;
import au.com.gman.glyph.engine.Panel;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 *
 * @author gub
 */
public class ProfileAccount {
    
    private final static String PROFILE_TYPE_DELIMITER = "&";
    private final static String PROFILE_LEVEL_PROPERTY_DELIMITER = ",";
    private final static String PROFILE_LEVEL_DELIMITER = "#";
    private final static String PROFILE_CHAPTER_PROPERTY_DELIMITER = "%";
    private final static String PROFILE_CHAPTER_DELIMITER = "|";
    
    private List<ProfileChapter> originalChapters;
    private List<ProfileChapter> tutorialChapters;
    private List<ProfileChapter> lockingChapters;
    
    public List<ProfileChapter> getChapters() {
        List<ProfileChapter> profileChapters = new ArrayList<ProfileChapter>();
        profileChapters.addAll(originalChapters);
        profileChapters.addAll(tutorialChapters);
        profileChapters.addAll(lockingChapters);
        return profileChapters;
    }
    
    public List<ProfileChapter> getChapters(String Type) {
        if (Type.equals(Library.ORIGINAL)) { return originalChapters; }
        if (Type.equals(Library.TUTORIAL)) { return tutorialChapters; }
        if (Type.equals(Library.LOCKING)) { return lockingChapters; }
        return null;
    }
    
    public void setChapters(String Type, List<ProfileChapter> Chapters) {
        if (Type.equals(Library.ORIGINAL)) { originalChapters = Chapters; }
        if (Type.equals(Library.TUTORIAL)) { tutorialChapters = Chapters; }
        if (Type.equals(Library.LOCKING)) { lockingChapters = Chapters; }
    }
    
    public boolean getLocked(String Type) {
        boolean locked = true;
        for (ProfileChapter profileChapter : getChapters(Type)) {
            locked = locked && (profileChapter.getLocked());
        }
        return locked;
    }
    
    public ProfileAccount() {
        originalChapters = new ArrayList<ProfileChapter>();
        tutorialChapters = new ArrayList<ProfileChapter>();
        lockingChapters = new ArrayList<ProfileChapter>();
    }
    
    public static ProfileAccount initialiseProfile(Panel xP) throws Exception {
        ProfileAccount profileAccount;
        try {
            profileAccount = loadProfileFromFile(xP);
        } catch (Exception e) {
            profileAccount = generateNewProfile(xP);
        }
        return profileAccount;
    }

    public static ProfileAccount resetProfile(Panel xP) throws Exception {
        xP.getContext().deleteFile(Library.PROFILE_ACCOUNT_FILE);
        ProfileAccount profileAccount;
        profileAccount = generateNewProfile(xP);
        writeProfileToFile(profileAccount, xP);
        return profileAccount;
    }
    
    public void setLevelProperties(Level level, int clicks, long nanos) {
        ProfileLevel profileLevel = getLevel(this, level.getId());
        if (!(profileLevel == null)) {
            if (profileLevel.getNanos() > 0) { profileLevel.setNanos(Math.min(nanos, profileLevel.getNanos())); } else { profileLevel.setNanos(nanos); }
            if (profileLevel.getClicks() > 0) { profileLevel.setClicks(Math.min(clicks, profileLevel.getClicks())); } else { profileLevel.setClicks(clicks); }
        }
    }
    
    public void setLevelPatterns(Level level, int patternsSolved) {
        ProfileLevel profileLevel = getLevel(this, level.getId());
        if (!(profileLevel == null)) {
            if (profileLevel.getPatterns() > 0) { profileLevel.setPatterns(Math.min(patternsSolved, profileLevel.getPatterns())); } else { profileLevel.setPatterns(patternsSolved); }
        }
    }
        
    public static ProfileLevel getLevel(ProfileAccount profileAccount, String Id) {
        for (ProfileChapter profileChapter : profileAccount.getChapters()) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                if (profileLevel.getId().equals(Id)) { return profileLevel; }
            }
        }
        return null;
    }
    
    public static ProfileChapter getChapter(ProfileAccount profileAccount, String Id) {
        for (ProfileChapter profileChapter : profileAccount.getChapters()) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                if (profileLevel.getId().equals(Id)) { return profileChapter; }
            }
        }
        return null;
    }
    
    public static String getType(ProfileAccount profileAccount, String Id) {
        ProfileLevel profileLevel = getLevel(profileAccount, Id);
        if (!(profileLevel == null)) {
            for(ProfileChapter profileChapter : profileAccount.getChapters(Library.ORIGINAL)) { if (profileChapter.getLevels().contains(profileLevel)) { return Library.ORIGINAL; } }
            for(ProfileChapter profileChapter : profileAccount.getChapters(Library.LOCKING)) { if (profileChapter.getLevels().contains(profileLevel)) { return Library.LOCKING; } }
            for(ProfileChapter profileChapter : profileAccount.getChapters(Library.TUTORIAL)) { if (profileChapter.getLevels().contains(profileLevel)) { return Library.TUTORIAL; } }
        }
        return null;
    }
    
    public static List<ProfileChapter> getChapters(ProfileAccount profileAccount, String Id) {
        for (ProfileChapter profileChapter : profileAccount.getChapters()) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                if (profileLevel.getId().equals(Id)) { return profileAccount.getChapters(); }
            }
        }
        return null;
    }
    
    public static ProfileAccount generateNewProfile(Panel xP) throws Exception {
        ProfileAccount profileAccount = new ProfileAccount();
        try {
            InputStream inStr = xP.getResources().openRawResource(R.raw.level);
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(inStr, null);
            Node xNode = doc.getDocumentElement();   
            NodeList xTypeNodes = xNode.getChildNodes();  
            for (int i=0; i < xTypeNodes.getLength(); i++) {      
                NodeList xChapterNodes = xTypeNodes.item(i).getChildNodes();
                for (int j=0; j < xChapterNodes.getLength(); j++) {  
                    if (xChapterNodes.item(j).getNodeName().equals(Library.CHAPTER)) {
                        ProfileChapter pC = new ProfileChapter();
                        NodeList xLevelNodes = xChapterNodes.item(j).getChildNodes();
                        for (int k=0; k < xLevelNodes.getLength(); k++) {  
                            if (xLevelNodes.item(k).getNodeName().equals(Library.LEVEL)) {
                                ProfileLevel pL = new ProfileLevel();
                                pC.getLevels().add(pL);
                                pL.setId(((Element)xLevelNodes.item(k)).getAttribute(Library.LEVEL_ID));
                                // First level of first chapter is unlocked                                
                                pL.setLocked(!((i == 0) && (k == 0)));
                            }
                        }
                        profileAccount.getChapters(xTypeNodes.item(i).getNodeName()).add(pC);
                    }
                }    
            }
            inStr.close();
            writeProfileToFile(profileAccount, xP);
        } catch (Exception e) {
        } finally {
        }
        return profileAccount;
    }
    
    public static void writeProfileToFile(ProfileAccount p, Panel xP) throws Exception {
        String profileString = "";
        profileString += writeProfileChaptersToString(p, Library.ORIGINAL);
        profileString += writeProfileChaptersToString(p, Library.TUTORIAL);
        profileString += writeProfileChaptersToString(p, Library.LOCKING);

        FileOutputStream fOut = xP.getContext().openFileOutput(Library.PROFILE_ACCOUNT_FILE, Context.MODE_PRIVATE);
        OutputStreamWriter osw = new OutputStreamWriter(fOut); 
        try {
            osw.write(profileString);
            osw.flush();
        } catch (Exception e) {
        } finally {
            osw.close();
            fOut.close();
        }
    }
    
    public static float getPercentageCompleted(ProfileAccount p, String Type) {
        float completed = 0.0f;
        float total = 0.0f;
        List<ProfileChapter> profileChapters = p.getChapters(Type);
        for (ProfileChapter profileChapter : profileChapters) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                if (!(profileLevel.getLocked())) { completed++; }
                total++;
            }
        }
        if (total > 0) { return completed / total;}
        return 0.0f;
    }
    
    public static float getAverageTime(ProfileAccount p, String Type) {
        float avgTime = 0.0f;
        float chapters = 0;
        List<ProfileChapter> profileChapters = p.getChapters(Type);
        for (ProfileChapter profileChapter : profileChapters) {
            if (!(profileChapter.getLocked())) {
                avgTime += profileChapter.getAvgNanos();
                chapters++;
            }
        }
        if (chapters > 0) { return avgTime / chapters; }
        return 0.0f;
    }
    
    public static String getQuickestTime(ProfileAccount p, String Type, Panel P) {
        float bestTime = Library.SOLVE_TIME_MAX;
        int bestChapter = 0;
        int bestLevel = 0;
        
        List<ProfileChapter> profileChapters = p.getChapters(Type);
        for (ProfileChapter profileChapter : profileChapters) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                if (!(profileLevel.getLocked())) { 
                    if (profileLevel.getNanos() > 0) {
                        if (profileLevel.getNanos() < bestTime) {
                            bestTime = profileLevel.getNanos();
                            bestChapter = profileChapters.indexOf(profileChapter) + 1;
                            bestLevel = profileChapter.getLevels().indexOf(profileLevel) + 1;
                        }
                    }
                }
            }
        }
        if ((bestChapter > 0) && (bestLevel > 0)) { return P.getLocaleString(R.string.news_best_level_time) + 
            ": " + String.format("%.2f", bestTime / 10000000.0f) + "s (" + 
                P.getLocaleString(R.string.chapter_prefix) + " " + bestChapter + ", " + 
                P.getLocaleString(R.string.level_prefix) + " " + bestLevel + ")"; }
        return null;
    }
    
    public static String writeProfileChaptersToString(ProfileAccount p, String Type) {
        List<ProfileChapter> profileChapters = p.getChapters(Type);
        String output = "";
        output += Type;
        output += PROFILE_CHAPTER_PROPERTY_DELIMITER;
        for (ProfileChapter profileChapter : profileChapters) {
            for (ProfileLevel profileLevel : profileChapter.getLevels()) {
                output += profileLevel.getId();
                output += PROFILE_LEVEL_PROPERTY_DELIMITER;
                output += profileLevel.getClicks();
                output += PROFILE_LEVEL_PROPERTY_DELIMITER;
                output += profileLevel.getNanos();
                output += PROFILE_LEVEL_PROPERTY_DELIMITER;
                output += profileLevel.getLocked();
                output += PROFILE_LEVEL_DELIMITER;
            }
            output += PROFILE_CHAPTER_DELIMITER;
        }
        output += PROFILE_TYPE_DELIMITER;
        return output;
    }
    
    public static ProfileAccount loadProfileFromFile(Panel xP) throws Exception {
        ProfileAccount profileAccount = new ProfileAccount();
        InputStreamReader isr = new InputStreamReader(xP.getContext().openFileInput(Library.PROFILE_ACCOUNT_FILE));
        BufferedReader r = new BufferedReader(isr);
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
        } finally {
            r.close();
            isr.close();
        }
        
        String string = total.toString();
        String[] types = string.split("\\" + PROFILE_TYPE_DELIMITER);
        for (int i=0; i < types.length; i++) {   
            String Type = "";
            String[] chapterProperties = types[i].split("\\" + PROFILE_CHAPTER_PROPERTY_DELIMITER);
            for (int j=0; j < chapterProperties.length; j++) {
                if (j == 0) {
                    Type = chapterProperties[j];
                }
                if (j == 1) {
                    String[] chapters = chapterProperties[j].split("\\" + PROFILE_CHAPTER_DELIMITER);
                    for (int k=0; k < chapters.length; k++) {                        
                        ProfileChapter profileChapter = new ProfileChapter();
                        String[] levels = chapters[k].split("\\" + PROFILE_LEVEL_DELIMITER);
                        for (int l=0; l < levels.length; l++) {
                            ProfileLevel profileLevel = new ProfileLevel();
                            String[] levelProperties = levels[l].split("\\" + PROFILE_LEVEL_PROPERTY_DELIMITER);
                            for (int m=0; m < levelProperties.length; m++) {
                                if (m==0) { profileLevel.setId(levelProperties[m]);}
                                if (m==1) { profileLevel.setClicks(Integer.parseInt(levelProperties[m]));}
                                if (m==2) { profileLevel.setNanos(Long.parseLong(levelProperties[m]));}
                                if (m==3) { profileLevel.setLocked(Boolean.parseBoolean(levelProperties[m]));}
                            }
                            profileChapter.getLevels().add(profileLevel);
                        }
                        if (Type.length() > 0) { profileAccount.getChapters(Type).add(profileChapter); }
                    }
                }
            }
        }
        return profileAccount;
    }
   
    public void unlockThisLevel(Level level) {
        ProfileLevel profileLevel = getLevel(this, level.getId());
        if (!(profileLevel == null)) { profileLevel.setLocked(false); }
    }
    
    public void unlockNextLevel(Level level) {
        List<ProfileChapter> profileChapters = getChapters(this, level.getId());
        ProfileChapter profileChapter = getChapter(this, level.getId());
        ProfileLevel profileLevel = getLevel(this, level.getId());
        if (!((profileLevel == null) && (profileChapter == null) && (profileChapters == null))) {
            // If not the last level of the chapter, unlock next level
            if (profileChapter.getLevels().indexOf(profileLevel) < profileChapter.getLevels().size() - 1) {
                profileChapter.getLevels().get(profileChapter.getLevels().indexOf(profileLevel) + 1).setLocked(false);
            } else {
                // If not the last chapter, unlock first level of next chapter
                if (profileChapters.indexOf(profileChapter) < profileChapters.size() - 1) {
                    if (profileChapters.get(profileChapters.indexOf(profileChapter) + 1).getLevels().size() > 0) {
                        profileChapters.get(profileChapters.indexOf(profileChapter) + 1).getLevels().get(0).setLocked(false);
                    }
                } else { 
                    // You win!
                }
            }
        }
    }
    
    public void destroy() {
        for (ProfileChapter profileChapter : getChapters()) {
            profileChapter.destroy();
        }
        originalChapters.clear();
        tutorialChapters.clear();
        lockingChapters.clear();
        originalChapters = null;
        tutorialChapters = null;
        lockingChapters = null;
    }
    
}
