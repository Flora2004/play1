package Tank.cor;

import Tank.GameObject.GameObject;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:46
 */
public interface Collider extends Serializable {
    //返回一个boolean表示是否可以进行下一次碰撞
    boolean collide(GameObject o1, GameObject o2);
}
