package client.cor;

import client.GameModel;
import client.GameObject;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:46
 */
public interface Collider extends Serializable {
    boolean collide(GameObject o1, GameObject o2);
}
