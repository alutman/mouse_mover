import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class MouseMover implements Runnable{

    public static final int MIN_TIMEOUT = 100;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final int MAX_TIMEOUT = 3600000;

    public static final int MIN_MOVERANGE = 0;
    public static final int DEFAULT_MOVERANGE = 100;
    public static final int MAX_MOVERANGE = 1000;

    // User interaction accommodation time
    // if user moves mouse, the thread pauses for this time to allow users to recover
    private static final int USER_MOVE_DELAY = 1000;

    // Mouse press/release timing
    private static final int MIN_PRESS_TIME = 50;
    private static final int MAX_PRESS_TIME = 80;

    private final int timeout;
    private final int moveRange;
    private final boolean anchor;
    private final boolean click;

    public MouseMover(int timeout, int moveRange, boolean anchor, boolean click) {
        if (timeout < MIN_TIMEOUT || timeout > MAX_TIMEOUT) {
            throw new IllegalArgumentException("timeout must be [" + MIN_TIMEOUT + "," + MAX_TIMEOUT + "]");
        }
        if (moveRange < MIN_MOVERANGE || moveRange > MAX_MOVERANGE) {
            throw new IllegalArgumentException("moverange must be [" + MIN_MOVERANGE + "," + MAX_MOVERANGE + "]");
        }
        this.timeout = timeout;
        this.moveRange = moveRange;

        this.anchor = anchor;
        this.click = click;
    }


    // Wait until the mouse stops moving ( user interaction)
    private void waitTilIdle() throws InterruptedException {
        while(true) {
            Point curPoint = MouseInfo.getPointerInfo().getLocation();
            Thread.sleep(USER_MOVE_DELAY);
            Point lastPoint = MouseInfo.getPointerInfo().getLocation();

            if (curPoint.equals(lastPoint)) {
                return;
            }
        }
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Random rand = new Random();

            Point initPoint = MouseInfo.getPointerInfo().getLocation();
            int anchorX = initPoint.x;
            int anchorY = initPoint.y;

            Point nextPoint = initPoint;

            waitTilIdle();
            while (true) {
                try {
                    Thread.sleep(timeout + rand.nextLong() % (timeout/10)); // TODO Param timeout random  +
                    Point lastPoint = MouseInfo.getPointerInfo().getLocation();

                    // If the mouse is where we expect, move again
                    if (nextPoint.x == lastPoint.x && nextPoint.y == lastPoint.y) {
                        int newX = (int)((this.anchor ? anchorX : lastPoint.x) + (moveRange > 0 ? rand.nextLong() % moveRange : 0));
                        int newY = (int)((this.anchor ? anchorY : lastPoint.y) + (moveRange > 0 ? rand.nextLong() % moveRange : 0));

                        nextPoint = new Point(newX, newY);
                        robot.mouseMove(newX, newY);

                        if (this.click){
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            // Slightly randomize the click press/release timing
                            Thread.sleep(MIN_PRESS_TIME + rand.nextLong() % (MAX_PRESS_TIME - MIN_PRESS_TIME));
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        }

                    }
                    else {
                        // Otherwise it's user interaction so wait until they stop
                        waitTilIdle();
                        lastPoint = MouseInfo.getPointerInfo().getLocation();
                        nextPoint = lastPoint;
                        anchorX = lastPoint.x;
                        anchorY = lastPoint.y;
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
