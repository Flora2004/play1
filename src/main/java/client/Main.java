package client;

import client.net.Client;

import java.io.IOException;
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
        TankFrame tankFrame = TankFrame.INSTANCE;
        tankFrame.setVisible(true);

        //connect to the server

        new Thread(()->new Audio("audio//war1.wav").loop()).start();

        new Thread(()->{
            while (true){
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                tankFrame.repaint();
            }
        }).start();

        //create a thread to run
        Client.INSTANCE.connect();
    }
}
