import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

/**
 * Created by Alex on 02/04/2016.
 */
public class DPIController {
    private static int  DEFAULT_DPI = 96;

    private static int DPI = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

    public static int getDefaultDPI() {
        return DEFAULT_DPI;
    }

    public static void setDefaultDPI(int i) {
        DEFAULT_DPI = i;
    }

    public static int getDpi() {
        return DPI;
    }

    public static int scaleToDPI(int input) {

        int factor =  DPI / DEFAULT_DPI;

        return factor * input;
    }

    public static Dimension scaleToDPI(Dimension input) {

        int factor =  DPI / DEFAULT_DPI;

        return new Dimension(factor * input.width, factor * input.height);
    }

    public static void scaleFontsToDPI() {

        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                Font f = ((FontUIResource) value);
                UIManager.put(key, f.deriveFont(scaleToDPI(f.getSize())*1f));
            }
        }
    }
}
