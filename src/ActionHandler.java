import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * Created by alutman on 30-Jan-15.
 */
public class ActionHandler implements ActionListener, WindowStateListener {

    private AppFrame appFrame;

    public ActionHandler(AppFrame af) {
        this.appFrame = af;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if ("start".equals(actionCommand)) {
            MouseMover mc;
            try {
                double timeoutSeconds = Math.abs(Double.parseDouble(appFrame.getTimeoutText()));
                int timeout = (int) (timeoutSeconds * 1000d);

                int moveRange = Math.abs(Integer.parseInt(appFrame.getMoveRangeText()));
                mc = new MouseMover(timeout, moveRange, appFrame.isAnchorChecked(), appFrame.isClickChecked());
            } catch (IllegalArgumentException ex) {
                appFrame.setStatus("invalid entry", ex.getMessage());
                return;
            }
            appFrame.startThread(new Thread(mc));
        } else if ("stop".equals(actionCommand)) {
            appFrame.stopThread();
        } else if ("exit".equals(actionCommand)) {
            appFrame.exit();
        } else if ("restore".equals(actionCommand)) {
            appFrame.restore();
        }
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        if(e.getNewState() == 1) {
            appFrame.minimizeToTray();
        }
    }
}
