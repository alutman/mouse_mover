import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by alutman on 07-Nov-14.
 */
public class AppFrame extends JFrame {

    private JTextField timeoutField = new JTextField(""+MouseMover.DEFAULT_TIMEOUT/1000);
    private JTextField moveRangeField = new JTextField(""+MouseMover.DEFAULT_MOVERANGE);
    private JButton start = new JButton("Start");
    private JLabel status = new JLabel(" Stopped");

    private TrayIcon trayIcon;


    private Thread moverThread = new Thread();

    public AppFrame() {

        status.setFont(new Font("Monospaced", Font.PLAIN, DPIController.scaleToDPI(14)));
        timeoutField.setFont(new Font("Monospaced", Font.PLAIN, DPIController.scaleToDPI(14)));
        moveRangeField.setFont(new Font("Monospaced", Font.PLAIN, DPIController.scaleToDPI(14)));

        this.setSize(DPIController.scaleToDPI(250),DPIController.scaleToDPI(120));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Mouse Mover");
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("image/mouse-pointer-64.png")).getImage());

        this.setLayout(new GridLayout(3,2));

        JLabel timeoutLabel = new JLabel("Timeout (s)");
        this.add(timeoutLabel);
        this.add(timeoutField);

        JLabel moveRangeLabel = new JLabel("Move Range (px)");
        this.add(moveRangeLabel);
        this.add(moveRangeField);
        this.add(start);
        this.add(status);

        ActionHandler ah = new ActionHandler(this);
        start.addActionListener(ah);
        this.addWindowStateListener(ah);
        createTrayIcon(ah);

        displayAsStopped();

        this.setVisible(true);
        this.setEnabled(true);
    }

    public void setStatus(String status) {
        this.status.setText(" "+status);
    }

    public String getTimeoutText() {
        return timeoutField.getText();
    }
    public String getMoveRangeText() {
        return moveRangeField.getText();
    }
    public void exit() {
        System.exit(0);
    }
    public void restore() {
        if(SystemTray.isSupported()) {
            this.setVisible(true);
            SystemTray.getSystemTray().remove(trayIcon);
            this.setState(Frame.NORMAL);
            this.toFront();
        }
    }
    public void minimizeToTray() {
        if(SystemTray.isSupported()) {
            this.setVisible(false);
            try {
                SystemTray tray = SystemTray.getSystemTray();
                tray.add(trayIcon);
            } catch (AWTException awte) {
                System.err.println(awte);
            }
        }
    }

    public void createTrayIcon(ActionListener al) {
        if(SystemTray.isSupported()) {
            Image image = new ImageIcon(getClass().getClassLoader().getResource("image/mouse-pointer-64.png")).getImage();
            PopupMenu popup = new PopupMenu();

            MenuItem open = new MenuItem("Open");
            open.setFont(new Font("default", Font.BOLD, DPIController.scaleToDPI(12)));
            open.setActionCommand("restore");
            open.addActionListener(al);
            popup.add(open);

            MenuItem start = new MenuItem("Start");
            start.setFont(new Font("default", Font.PLAIN, DPIController.scaleToDPI(12)));
            start.setActionCommand("start");
            start.addActionListener(al);
            popup.add(start);

            MenuItem stop = new MenuItem("Stop");
            stop.setFont(new Font("default", Font.PLAIN, DPIController.scaleToDPI(12)));
            stop.setActionCommand("stop");
            stop.addActionListener(al);
            popup.add(stop);


            MenuItem exit = new MenuItem("Exit");
            exit.setFont(new Font("default", Font.PLAIN, DPIController.scaleToDPI(12)));
            exit.setActionCommand("exit");
            exit.addActionListener(al);
            popup.add(exit);

            int trayIconWidth = new TrayIcon(image).getSize().width;

            trayIcon = new TrayIcon(image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "Mouse Mover", popup);
            trayIcon.setActionCommand("restore");
            trayIcon.addActionListener(al);
        }
    }

    public void startThread(Thread thread) {
        if(!(moverThread != null && moverThread.isAlive())) { // Don't start more than one mover thread
            moverThread = thread;
            moverThread.start();
        }
        displayAsStarted();
        if(SystemTray.isSupported()) {
            trayIcon.displayMessage("Mouse Mover","Started mouse movement",TrayIcon.MessageType.INFO);
        }
    }
    private void displayAsStarted() {
        if(SystemTray.isSupported()) {
            trayIcon.getPopupMenu().getItem(1).setEnabled(false); // 'Start' tray option
            trayIcon.getPopupMenu().getItem(2).setEnabled(true); // 'Stop' tray option
        }
        start.setText("Stop");
        start.setActionCommand("stop");
        setStatus("Running");
        timeoutField.setEditable(false);
        moveRangeField.setEditable(false);
    }

    public void stopThread() {
        moverThread.interrupt();
        displayAsStopped();
        if(SystemTray.isSupported()) {
            trayIcon.displayMessage("Mouse Mover","Mouse movement stopped",TrayIcon.MessageType.INFO);
        }
    }
    private void displayAsStopped() {
        if(SystemTray.isSupported()) {
            trayIcon.getPopupMenu().getItem(1).setEnabled(true); // 'Start' tray option
            trayIcon.getPopupMenu().getItem(2).setEnabled(false); // 'Stop' tray option
        }
        start.setText("Start");
        start.setActionCommand("start");
        setStatus("Stopped");
        timeoutField.setEditable(true);
        moveRangeField.setEditable(true);
    }

    public static void main(String[] args)  {
        DPIController.scaleFontsToDPI();
        new AppFrame();
    }

}
