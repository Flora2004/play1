package client;

import java.awt.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 14:42
 */
public class Tank {
    private int x,y;
    private Dir dir;
    private static final int SPEED=5;
    public static int WIDTH=ResourceMgr.tankU.getWidth();
    public static int HEIGHT=ResourceMgr.tankU.getHeight();
    private Random random=new Random();
    private boolean moving=true;
    private TankFrame tankFrame;
    private boolean living=true;
    private Group group=Group.BAD;
    public Tank(int x, int y, Dir dir,Group group,TankFrame tankFrame){
        super();
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.group=group;
        this.tankFrame=tankFrame;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving){
        this.moving=moving;
    }


    public void paint(Graphics g){
        if(!living) {
            tankFrame.tanks.remove(this);
        }
        switch (dir){
            case LEFT:
                g.drawImage(ResourceMgr.tankL,x,y,null);
                break;
            case UP:
                g.drawImage(ResourceMgr.tankU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.tankR,x,y,null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.tankD,x,y,null);
                break;
        }

        move();
    }
    private void move(){
        if(!moving)return;
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
        if(this.group==Group.BAD&&random.nextInt(100)>95)
            this.fire();
        if(this.group==Group.BAD&&random.nextInt(100)>90)
            randomDir();

    }
    private void randomDir(){
        this.dir=Dir.values()[random.nextInt(4)];
    }

    public void fire(){
        int bX=this.x+Tank.WIDTH/2-Bullet.WIDTH/2;
        int bY=this.y+Tank.HEIGHT/2-Bullet.HEIGHT/2;
        tankFrame.bullets.add(new Bullet(bX,bY,this.dir,this.group,this.tankFrame));
    }
    public void die(){
        this.living=false;
    }
}
