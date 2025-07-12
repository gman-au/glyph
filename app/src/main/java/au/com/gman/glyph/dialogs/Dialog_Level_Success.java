/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.dialogs;

import android.graphics.RectF;
import android.graphics.Typeface;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.Panel;
/*
 *
 * @author gub
 */
public class Dialog_Level_Success extends Dialog {
    
    public Dialog_Level_Success(ColorX C1, ColorX C2, String T, RectF R, Typeface font, Library.Dialogs Type, Panel xP) {
        super(C1, C2, T, R, font, Type);
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.1f, 0.75f, 0.7f, 0.1f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_next), Button.NEXT_LEVEL, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.7f, 0.75f, 0.1f, 0.1f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_menu), Button.MENU, font));
    }
}

