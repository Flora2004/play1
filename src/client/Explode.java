package client;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 15:18
 */
public class Explode extends GameObject{
    public static int WIDTH=ResourceMgr.explodes[0].getWidth();
    public static int HEIGHT=ResourceMgr.explodes[0].getHeight();

    private int step;
    GameModel gm;
    public Explode(int x, int y,GameModel gm){
        this.x=x;
        this.y=y;
        this.gm=gm;

        new Thread(()->new Audio("audio/expolde.wav").play()).start();
        gm.add(this);
    }
    public void paint(Graphics g){

        g.drawImage(ResourceMgr.explodes[step++],x,y,null);

        if(step>=ResourceMgr.explodes.length)
            gm.remove(this);
    }
}
