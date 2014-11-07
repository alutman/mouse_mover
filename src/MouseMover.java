/**
 * Created by alutman on 07-Nov-14.
 */
import java.awt.*;
import java.util.*;
public class MouseMover implements Runnable{

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
            awte.printStackTrace();
            System.exit(1);
        }
        catch (InterruptedException e) {
            //LOL i dont care
        }

    }
}
