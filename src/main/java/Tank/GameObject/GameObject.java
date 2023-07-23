package Tank.GameObject;

import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 9:17
 */
public abstract class GameObject implements Serializable {
    int x,y;

    public abstract void paint(Graphics g);

}
