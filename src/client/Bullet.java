package client;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 15:18
 */
public class Bullet {
    private static final int SPEED=10;
    public static int WIDTH=ResourceMgr.bulletD.getWidth();
    public static int HEIGHT=ResourceMgr.bulletD.getHeight();

    private int x,y;
    private Dir dir;
    private boolean living=true;
    private Group group=Group.BAD;
    private TankFrame tankFrame;

    public Bullet(int x,int y,Dir dir,Group group,TankFrame tankFrame){
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.group=group;
        this.tankFrame=tankFrame;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void paint(Graphics g){
        if(!living){
            tankFrame.bullets.remove(this);//当子弹离开屏幕的时候就从数列中删除
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
    }
    private void die(){
        this.living=false;
    }

    //判断碰撞
    public void collideWith(Tank tank){
        if(this.group==tank.getGroup())return;

        //TODO:用一个rect来记录子弹的位置
        Rectangle rect1=new Rectangle(this.x,this.y,WIDTH,HEIGHT);
        Rectangle rect2=new Rectangle(tank.getX(),tank.getY(),Tank.WIDTH,Tank.HEIGHT);
        if(rect1.intersects(rect2)){
            tank.die();
            this.die();
            int eX=tank.getX()+Tank.WIDTH/2-Explode.WIDTH/2;
            int eY=tank.getY()+Tank.HEIGHT/2-Explode.HEIGHT/2;
            tankFrame.explodes.add(new Explode(eX,eY,tankFrame));
        }
    }
}
