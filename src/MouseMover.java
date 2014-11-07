/**
 * Created by alutman on 07-Nov-14.
 */
import java.awt.*;
import java.util.*;
public class MouseMover implements Runnable{

    private int timeout;
    private int moveRange;
    private AppFrame appFrame;

    public MouseMover(int timeout, int moveRange, AppFrame appFrame) {
        this.timeout = timeout;
        this.moveRange = moveRange;
        this.appFrame = appFrame;
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Random rand = new Random();

            while (true) {
                Point curPoint = MouseInfo.getPointerInfo().getLocation();
                Thread.sleep(timeout);
                Point lastPoint = MouseInfo.getPointerInfo().getLocation();

                if (curPoint.x == lastPoint.x && curPoint.y == lastPoint.y) {
                    int newX = (int)(curPoint.x + rand.nextLong() % moveRange);
                    int newY = (int)(curPoint.y + rand.nextLong() % moveRange);
                    robot.mouseMove(newX, newY);
                }
            }
        } catch(AWTException awte) {
            //Occurs when the OS doesn't support Robot
            awte.printStackTrace();
            System.exit(1);
        } catch (NullPointerException npe) {
            //Probably couldn't get the location (locked), show that the thread has stopped in the GUI
            appFrame.showStopped();
        }
        catch (InterruptedException e) {
            //This is thrown when the user chooses to stop
            appFrame.showStopped();
        }

    }
}
