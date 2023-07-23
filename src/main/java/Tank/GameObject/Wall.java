package Tank.GameObject;

import Tank.GameObject.GameObject;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 13:54
 */
public class Wall extends GameObject {
    int w,h;
    public Rectangle rect;

    public Wall(int x,int y,int w,int h){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;

        this.rect=new Rectangle(x,y,w,h);
    }
    @Override
    public void paint(Graphics g){
        Color c=g.getColor();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x,y,w,h);
        g.setColor(c);
    }
}
