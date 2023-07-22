package client.strategy;

import client.Audio;
import client.Bullet;
import client.Group;
import client.Tank;
import client.net.BulletNewMsg;
import client.net.Client;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-18
 * Time: 14:31
 */
public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank t) {
        int bX=t.getX()+Tank.WIDTH/2- Bullet.WIDTH/2;
        int bY=t.getY()+Tank.HEIGHT/2-Bullet.HEIGHT/2;

       Bullet b = new Bullet(t.getId(),bX,bY,t.getDir(),t.getGroup(),t.getGameModel());
        Client.INSTANCE.send(new BulletNewMsg(b));

        if(t.getGroup()== Group.GOOD){
            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }
}
