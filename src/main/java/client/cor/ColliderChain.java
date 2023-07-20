package client.cor;

import client.GameObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 11:03
 */
public class ColliderChain implements Collider{
    private List<Collider> colliders=new LinkedList<>();//动态添加且不需要根据下标访问

    public ColliderChain(){
        add(new BulletTankCollider());
        add(new TankTankCollider());
        add(new BulletWallCollider());
        add(new TankWallCollider());
    }
    public void add(Collider c){
        colliders.add(c);
    }

    public boolean collide(GameObject o1, GameObject o2) {
        for (int i = 0; i < colliders.size(); i++) {
            if(!colliders.get(i).collide(o1,o2)){
                return false;
            }
        }
        return true;
    }
}
