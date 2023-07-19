package client.cor;

import client.*;

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

            if(b.getGroup()==t.getGroup())return true;

            if(b.getRect().intersects(t.getRect())){
                t.die();
                b.die();
                int eX=t.getX()+Tank.WIDTH/2- Explode.WIDTH/2;
                int eY=t.getY()+Tank.HEIGHT/2-Explode.HEIGHT/2;
                t.getGm().add(new Explode(eX,eY,t.getGm()));
                return false;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet){
            collide(o2,o1);
        }
        return true;
    }
}
