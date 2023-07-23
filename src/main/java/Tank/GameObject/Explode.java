package Tank.GameObject;

import Tank.Audio;
import Tank.GameModel;
import Tank.Mgr.MovingMgr;
import Tank.Mgr.ResourceMgr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 15:18
 */
public class Explode extends GameObject {
    public static int WIDTH= ResourceMgr.explodes[0].getWidth();
    public static int HEIGHT=ResourceMgr.explodes[0].getHeight();
    private int step=0;
    private UUID playerId;
    private UUID id=UUID.randomUUID();
    GameModel gm;
    MovingMgr movingMgr=new MovingMgr();


    public Explode(UUID playerId,int x, int y,GameModel gm){
        this.playerId=playerId;
        this.x=x;
        this.y=y;
        this.gm=gm;

        new Thread(()->new Audio("audio/explode.wav").play()).start();
        gm.add(this);
    }
    public void paint(Graphics g){
        if(step<=15) {
            g.drawImage(movingMgr.out(step), x, y, null);
            step++;
        }
        if(step>16)
            gm.remove(this);
    }
    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public UUID getId() {
        return id;
    }
    public UUID getPlayerId() {
        return playerId;
    }

}
