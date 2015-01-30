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
        if(e.getActionCommand().equals("start")) {
            int timeout;
            int moveRange;
            try {
                timeout = Math.abs(Integer.parseInt(appFrame.getTimeoutText())*1000);
                if(timeout < MouseMover.MIN_TIMEOUT || timeout > MouseMover.MAX_TIMEOUT) throw new Exception();
                moveRange = Math.abs(Integer.parseInt(appFrame.getMoveRangeText()));
                if(moveRange < MouseMover.MIN_MOVERANGE || moveRange > MouseMover.MAX_MOVERANGE) throw new Exception();
            } catch (Exception ex) {
                appFrame.setStatus(" Invalid entry");
                return;
            }
            appFrame.startThread(new Thread(new MouseMover(timeout,moveRange)));

        }
        else if(e.getActionCommand().equals("stop")) {
            appFrame.stopThread();
        }
        else if(e.getActionCommand().equals("exit")) {
            appFrame.exit();
        }
        else if(e.getActionCommand().equals("restore")) {
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
