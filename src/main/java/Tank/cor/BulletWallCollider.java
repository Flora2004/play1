package Tank.cor;

import Tank.GameObject.Bullet;
import Tank.GameObject.GameObject;
import Tank.GameObject.Wall;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:48
 */
public class BulletWallCollider implements Collider{
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if(o1 instanceof Bullet && o2 instanceof Wall){
            Bullet b=(Bullet) o1;
            Wall w=(Wall) o2;

            if(b.rect.intersects(w.rect)){
                b.die();
            }
        } else if (o1 instanceof Wall && o2 instanceof Bullet){
            return collide(o2,o1);
        }
        return true;
    }
}
