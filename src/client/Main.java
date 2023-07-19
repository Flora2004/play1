package client;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 13:38
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tankFrame = new TankFrame();

//        new Thread(()->new Audio("audio//war1.wav").loop()).start();

        while (true) {
            Thread.sleep(25);
            tankFrame.repaint();
        }
    }
}
