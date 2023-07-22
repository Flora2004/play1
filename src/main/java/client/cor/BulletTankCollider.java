package client.cor;

import client.*;
import client.net.Client;
import client.net.ExplodeNewMsg;
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
            System.out.println("打出子弹的坦克id: "+b.getPlayerId());
            System.out.println("被打中的坦克id: "+t.getId());

            if(b.getPlayerId().equals(t.getId())) {
                System.out.println("子弹是自己的");
                return true;
            }

            if(b.rect.intersects(t.rect)){
                t.die();
                b.die();
                //向服务器传送子弹打中坦克
                Client.INSTANCE.send(new TankDieMsg(b.getId(),t.getId()));

                int eX=t.getX()+Tank.WIDTH/2- Explode.WIDTH/2;
                int eY=t.getY()+Tank.HEIGHT/2-Explode.HEIGHT/2;
                Explode explode=new Explode(t.getId(),eX,eY,t.getGameModel());
                //向服务器传送爆炸发生
                Client.INSTANCE.send(new ExplodeNewMsg(explode));

                return false;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet){
            return collide(o2,o1);
        }
        return true;
    }
}
