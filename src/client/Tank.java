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
    int x,y;
     Dir dir;
    private static final int SPEED=Integer.parseInt((String) PropertyMgr.get("tankSpeed"));
    public static int WIDTH=ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT=ResourceMgr.goodTankU.getHeight();
    private Random random=new Random();
    private boolean moving=true;
    private boolean living=true;
     Group group=Group.BAD;
     TankFrame tankFrame;
    Rectangle rect=new Rectangle();

    FireStrategy fs;

    public Tank(int x, int y, Dir dir,Group group,TankFrame tankFrame){
        super();
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.group=group;
        this.tankFrame=tankFrame;

        rect.x=this.x;
        rect.y=this.y;
        rect.width= WIDTH;
        rect.height=HEIGHT;

        if(group==Group.GOOD) {//设置子弹的发射策略
            String goodFSName=(String) PropertyMgr.get("goodFS");
            try {
                fs=(FireStrategy) Class.forName(goodFSName).getDeclaredConstructor().newInstance();//把名字代表的名字load到内存
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if(group==Group.BAD) {//设置子弹的发射策略
            String badFSName=(String) PropertyMgr.get("badFS");
            try {
                fs=(FireStrategy) Class.forName(badFSName).getDeclaredConstructor().newInstance();//把名字代表的名字load到内存
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
                g.drawImage(this.group==Group.GOOD?ResourceMgr.goodTankL:ResourceMgr.badTankL,x,y,null);
                break;
            case UP:
                g.drawImage(this.group==Group.GOOD?ResourceMgr.goodTankU:ResourceMgr.badTankU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(this.group==Group.GOOD?ResourceMgr.goodTankR:ResourceMgr.badTankR,x,y,null);
                break;
            case DOWN:
                g.drawImage(this.group==Group.GOOD?ResourceMgr.goodTankD:ResourceMgr.badTankD,x,y,null);
                break;
            default:
                break;
        }

        move();
    }
    private void move(){
        if(!moving) {
            return;
        }
        switch (dir){
            case LEFT :{
                x-=SPEED;
                break;
            }
            case UP:{
                y-=SPEED;
                break;
            }
            case RIGHT:{
                x+=SPEED;
                break;
            }
            case DOWN:{
                y+=SPEED;
                break;
            }
            default:
                break;
        }



        if(this.group==Group.BAD&&random.nextInt(100)>95) {
            this.fire();
        }

        if(this.group==Group.BAD&&random.nextInt(100)>90) {
            randomDir();
        }

        boundsCheck();//边界检测，让坦克在屏幕中移动

        // 更新rect
        rect.x=this.x;
        rect.y=this.y;
    }
    private void boundsCheck(){//边界检测，让坦克在屏幕中移动
        if(this.x<2) {
            x=2;
        }
        if (this.y<28) {
            y=28;
        }
        if(this.x>TankFrame.GAME_WIDTH-Tank.WIDTH-2) {
            x=TankFrame.GAME_WIDTH-Tank.WIDTH-2;
        }
        if(this.y>TankFrame.GAME_HEIGHT-Tank.HEIGHT-2) {
            y=TankFrame.GAME_HEIGHT-Tank.HEIGHT-2;
        }
    }
    private void randomDir(){
        this.dir=Dir.values()[random.nextInt(4)];
    }

    public void fire(){
        fs.fire(this);
    }
    public void die(){
        this.living=false;
    }
}
