package Tank.cor;

import Tank.GameObject.GameObject;

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
        add(new BulletTankCollider());//子弹和坦克碰撞检测
        add(new HMTankTankCollider());//人机坦克间碰撞检测
        add(new BulletWallCollider());//子弹和墙的碰撞检测
        add(new TankWallCollider());//人机坦克和墙的碰撞检测
    }
    //加入碰撞检测
    public void add(Collider c){
        colliders.add(c);
    }

    /**
     * 碰撞检测的主要方法，用于检查两个游戏对象是否发生了碰撞。
     * @param o1  第一个游戏对象，参与碰撞检测的其中一个对象。
     * @param o2  第二个游戏对象，参与碰撞检测的另一个对象。
     * @return 如果两个游戏对象发生了碰撞，则返回 true，否则返回 false。
     */
    public boolean collide(GameObject o1, GameObject o2) {
        for (int i = 0; i < colliders.size(); i++) {
            if(!colliders.get(i).collide(o1,o2)){
                return false;
            }
        }
        return true;
    }
}
