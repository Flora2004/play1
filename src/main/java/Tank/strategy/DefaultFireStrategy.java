package Tank.strategy;

import Tank.Audio;
import Tank.GameObject.Bullet;
import Tank.Enum.Group;
import Tank.GameObject.Tank;
import Tank.net.AllMsg.BulletNewMsg;
import Tank.net.Client;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-18
 * Time: 14:31
 */
public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank t) {
        if(t.isLiving()){
            int bX = t.getX() + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
            int bY = t.getY() + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;

            Bullet b = new Bullet(t.getId(), bX, bY, t.getDir(), t.getGroup(), t.getGameModel());
            Client.INSTANCE.send(new BulletNewMsg(b));

            if (t.getGroup() == Group.GOOD) {
                new Thread(() -> new Audio("audio/tank_fire.wav").play()).start();
            }
        }
    }
}
