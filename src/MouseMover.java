/**
 * Created by alutman on 07-Nov-14.
 */
import java.awt.*;
import java.util.*;
public class MouseMover implements Runnable{

    public static final int MIN_TIMEOUT = 100;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final int MAX_TIMEOUT = 3600000;
    public static final int MIN_MOVERANGE = 10;
    public static final int DEFAULT_MOVERANGE = 100;
    public static final int MAX_MOVERANGE = 1000;

    private int timeout;
    private int moveRange;

    public MouseMover(int timeout, int moveRange) {
        this.timeout = timeout;
        this.moveRange = moveRange;
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Random rand = new Random();

            while (true) {
                try {
                    Point curPoint = MouseInfo.getPointerInfo().getLocation();
                    Thread.sleep(timeout);
                    Point lastPoint = MouseInfo.getPointerInfo().getLocation();
                    if (curPoint.x == lastPoint.x && curPoint.y == lastPoint.y) {
                        int newX = (int)(curPoint.x + rand.nextLong() % moveRange);
                        int newY = (int)(curPoint.y + rand.nextLong() % moveRange);

                        robot.mouseMove(newX, newY);
                    }
                }
                catch (NullPointerException npe) {
                    //Couldn't grab pointer, try again later
                    Thread.sleep(timeout);
                }
            }
        } catch(AWTException awte) {
            //Occurs when the OS doesn't support Robot
            awte.printStackTrace();
            System.exit(1);
        }
        catch (InterruptedException e) {
            //This is thrown when the user chooses to stop
        }

    }
}
