package client;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 15:18
 */
public class Explode {
    public static int WIDTH=ResourceMgr.explodes[0].getWidth();
    public static int HEIGHT=ResourceMgr.explodes[0].getHeight();

    private int x,y;
    private TankFrame tankFrame;
    private int step;

    public Explode(int x, int y, TankFrame tankFrame){
        this.x=x;
        this.y=y;
        this.tankFrame=tankFrame;
    }

    public void paint(Graphics g){
        g.drawImage(ResourceMgr.explodes[step++],x,y,null);
        if(step>=ResourceMgr.explodes.length)
            step=0;
    }
}
