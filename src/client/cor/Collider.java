package client.cor;

import client.GameModel;
import client.GameObject;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:46
 */
public interface Collider {
    boolean collide(GameObject o1, GameObject o2);
}
