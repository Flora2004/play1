package client.cor;

import client.*;
import client.net.Client;
import client.net.TankDieMsg;
import client.net.TankStopMsg;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:48
 */
public class BulletTankCollider implements Collider{
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if(o1 instanceof Bullet && o2 instanceof Tank){
            Bullet b=(Bullet) o1;
            Tank t=(Tank) o2;

            if(b.getGroup()==t.getGroup()) return true;

            if(b.rect.intersects(t.rect)){
                t.die();
                b.die();
                int eX=t.getX()+Tank.WIDTH/2- Explode.WIDTH/2;
                int eY=t.getY()+Tank.HEIGHT/2-Explode.HEIGHT/2;
                new Explode(t.getId(),eX,eY,t.getGameModel());

                //向服务器传送子弹打中坦克
                Client.INSTANCE.send(new TankDieMsg(b.getId(),t.getId()));
                //向服务器传送爆炸发生
                Client.INSTANCE.send(new TankDieMsg(b.getId(),t.getId()));

                return false;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet){
            return collide(o2,o1);
        }
        return true;
    }
}
