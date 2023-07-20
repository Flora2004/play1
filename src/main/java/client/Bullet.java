package client;

import java.awt.*;
import java.security.PublicKey;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 15:18
 */
public class Bullet extends GameObject{
    private static final int SPEED=Integer.parseInt((String) PropertyMgr.get("bulletSpeed"));//使用配置文件来改变
    public static int WIDTH=ResourceMgr.bulletD.getWidth();
    public static int HEIGHT=ResourceMgr.bulletD.getHeight();

    private Dir dir;
    private boolean living=true;
    private Group group=Group.BAD;
    GameModel gm=null;
    public Rectangle rect=new Rectangle();

    public Bullet(int x,int y,Dir dir,Group group,GameModel gm){
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.group=group;
        this.gm=gm;

        rect.x=this.x;
        rect.y=this.y;
        rect.width= WIDTH;
        rect.height=HEIGHT;

        gm.add(this);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public Rectangle getRect(){
        return this.rect;
    }
    public void paint(Graphics g){
        if(!living){
            gm.remove(this);//当子弹离开屏幕的时候就从数列中删除
        }
        switch (dir){
            case LEFT:
                g.drawImage(ResourceMgr.bulletL,x,y,null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR,x,y,null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD,x,y,null);
                break;
            default:
                break;
        }

        move();
    }
    private void move(){
        switch (dir){
            case LEFT :{
                x-=SPEED;
                break;}
            case UP:{
                y-=SPEED;
                break;}
            case RIGHT:{
                x+=SPEED;
                break;}
            case DOWN:{
                y+=SPEED;
                break;}
            default:
                break;
        }

        if(x<0||y<0||x>TankFrame.GAME_WIDTH||y>TankFrame.GAME_HEIGHT)living=false;//如果离开屏幕则子弹状态为false

        //更新rect
        rect.x=this.x;
        rect.y=this.y;
    }
    public void die(){
        this.living=false;
    }

    //判断碰撞
//    public void collideWith(Tank tank){
//        if(this.group==tank.getGroup())return;
//
//        if(rect.intersects(tank.rect)){
//            tank.die();
//            this.die();
//            int eX=tank.getX()+Tank.WIDTH/2-Explode.WIDTH/2;
//            int eY=tank.getY()+Tank.HEIGHT/2-Explode.HEIGHT/2;
//            gm.add(new Explode(eX,eY,gm));
//        }
//    }
}
