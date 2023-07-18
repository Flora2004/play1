package client;

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

        //初始化敌方坦克
        for (int i = 0; i < 5; i++) {
            tankFrame.tanks.add(new Tank(50+i*30,200,Dir.DOWN,tankFrame));
        }

        while (true) {
            Thread.sleep(10);
            tankFrame.repaint();
        }
    }
}
