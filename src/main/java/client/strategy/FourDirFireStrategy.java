package client.strategy;

import client.*;

/**
 * Created with IntelliJ IDEA.
 * Description:吃到道具后在一段时间内可以发射四个子弹
 * User: angel
 * Date: 2023-07-18
 * Time: 14:51
 */
public class FourDirFireStrategy implements FireStrategy{
    @Override
    public void fire(Tank t) {
        int bX=t.getX()+Tank.WIDTH/2- Bullet.WIDTH/2;
        int bY=t.getY()+Tank.HEIGHT/2-Bullet.HEIGHT/2;

        Dir[] dirs=Dir.values();
        for(Dir dir:dirs){
            new Bullet(t.getId(),bX,bY,dir,t.getGroup(),t.getGameModel());
        }

        if(t.getGroup()== Group.GOOD){
            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }
}
