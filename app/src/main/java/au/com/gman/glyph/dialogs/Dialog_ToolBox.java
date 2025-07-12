/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.gman.glyph.dialogs;

import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import au.com.gman.glyph.level.Library;
import au.com.gman.glyph.level.Library.Button;
import au.com.gman.glyph.engine.ColorX;
import au.com.gman.glyph.engine.Panel;
import au.com.gman.glyph.engine.SpriteController;
import au.com.gman.glyph.gate.*;
/*
 *
 * @author gub
 */
public class Dialog_ToolBox extends Dialog {
    
    private Gate selectedGate;
    public Gate getSelectedGate() { return selectedGate; }
    
    public Dialog_ToolBox(ColorX C1, ColorX C2, String T, RectF R, Typeface font, Library.Dialogs Type, Panel xP, SpriteController sC) {
        super(C1, C2, T, R, font, Type);
        selectedGate = null;
        // First row
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.050f, 0.2f, 0.850f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateINPUT("input", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.175f, 0.2f, 0.725f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateOUTPUT("output", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.300f, 0.2f, 0.600f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateOR("or", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.425f, 0.2f, 0.475f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateAND("and", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.550f, 0.2f, 0.350f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateNOT("not", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.675f, 0.2f, 0.225f, 0.65f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateXOR("xor", 0.0f, 0.0f, 0)));
        // Second row
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.050f, 0.4f, 0.850f, 0.45f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateBOMB("bomb", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.175f, 0.4f, 0.725f, 0.45f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateFUSE("fuse", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.300f, 0.4f, 0.600f, 0.45f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateFLIP("flip", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.425f, 0.4f, 0.475f, 0.45f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateNOR("nor", 0.0f, 0.0f, 0)));
        buttons.add(new DialogButtonGate(Library.getScaledRect(rect, 0.550f, 0.4f, 0.350f, 0.45f), C1, C2, "text", Button.EDIT_PICK_GATE, font, sC, new GateNAND("nand", 0.0f, 0.0f, 0)));
        // Buttons
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.825f, 0.8f, 0.025f, 0.05f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_cancel), Button.CANCEL, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.625f, 0.8f, 0.225f, 0.05f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_save), Button.EDIT_SAVE, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.425f, 0.8f, 0.425f, 0.05f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_verify), Button.EDIT_VERIFY, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.225f, 0.8f, 0.625f, 0.05f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_test), Button.EDIT_TEST, font));
        buttons.add(new DialogButton(Library.getScaledRect(rect, 0.025f, 0.8f, 0.825f, 0.05f), C1, C2, xP.getLocaleString(au.com.gman.glyph.R.string.button_open), Button.EDIT_OPEN, font));
    }
    
    @Override
    public Button handleDialogEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (DialogButton d : buttons) {
                if (Library.inBounds(event.getX(), event.getY(), d.getRect())) {
                    pressed = d;
                    break;
                }
            }
        }
        if (!(pressed == null)) {
            if (pressed.getClass().equals(DialogButtonGate.class)) {
                selectedGate = ((DialogButtonGate)pressed).getGate();
            }
        } else { selectedGate = null; }
        return super.handleDialogEvent(event);
    }
}

