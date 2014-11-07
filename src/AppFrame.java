import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alutman on 07-Nov-14.
 */
public class AppFrame extends JFrame implements ActionListener{

    private final int MIN_TIMEOUT = 100;
    private final int DEFAULT_TIMEOUT = 60000;
    private final int MAX_TIMEOUT = 3600000;
    private final int MIN_MOVERANGE = 10;
    private final int DEFAULT_MOVERANGE = 100;
    private final int MAX_MOVERANGE = 1000;

    private JTextField timeoutField = new JTextField(""+DEFAULT_TIMEOUT);
    private JTextField moveRangeField = new JTextField(""+DEFAULT_MOVERANGE);
    private JButton start = new JButton("Start");
    private JLabel status = new JLabel(" Stopped");


    private Thread currentThread = new Thread();

    public AppFrame() {
        status.setFont(new Font("Monospaced", Font.PLAIN,14));
        timeoutField.setFont(new Font("Monospaced", Font.PLAIN,14));
        moveRangeField.setFont(new Font("Monospaced", Font.PLAIN,14));

        this.setSize(250,120);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Mouse Mover");
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("image/mouse-pointer-64.png")).getImage());

        this.setLayout(new GridLayout(3,2));

        JLabel timeoutLabel = new JLabel("Timeout (ms)");
        this.add(timeoutLabel);
        this.add(timeoutField);

        JLabel moveRangeLabel = new JLabel("Move Range (px)");
        this.add(moveRangeLabel);
        this.add(moveRangeField);
        this.add(start);
        this.add(status);

        start.setActionCommand("start");
        start.addActionListener(this);

        this.setVisible(true);
        this.setEnabled(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")) {
            int timeout;
            int moveRange;
            try {
                timeout = Math.abs(Integer.parseInt(timeoutField.getText()));
                if(timeout < MIN_TIMEOUT || timeout > MAX_TIMEOUT) throw new Exception();
                moveRange = Math.abs(Integer.parseInt(moveRangeField.getText()));
                if(moveRange < MIN_MOVERANGE || moveRange > MAX_MOVERANGE) throw new Exception();
            } catch (Exception ex) {
                status.setText(" Invalid entry");
                return;
            }
            currentThread = new Thread(new MouseMover(timeout,moveRange, this));
            currentThread.start();
            showStarted();
        }
        else if(e.getActionCommand().equals("stop")) {
            currentThread.interrupt();
        }
    }
    public void showStarted()
    {
        start.setText("Stop");
        start.setActionCommand("stop");
        status.setText(" Running");
        timeoutField.setEditable(false);
        moveRangeField.setEditable(false);
        //timeoutField.setBackground(new Color(255,255,255));
    }
    public void showStopped() {
        start.setText("Start");
        start.setActionCommand("start");
        status.setText(" Stopped");
        timeoutField.setEditable(true);
        moveRangeField.setEditable(true);
    }
    public static void main(String[] args)  {
        new AppFrame();
    }
}
