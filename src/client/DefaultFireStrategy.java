package client;

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
        int bX=t.x+Tank.WIDTH/2-Bullet.WIDTH/2;
        int bY=t.y+Tank.HEIGHT/2-Bullet.HEIGHT/2;

        new Bullet(bX,bY,t.dir,t.group,t.tankFrame);

        if(t.group==Group.GOOD){
            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }
}
