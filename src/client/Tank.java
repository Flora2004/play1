package client;

import client.strategy.FireStrategy;

import java.awt.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 14:42
 */
public class Tank extends GameObject{
    int x,y;
    int oldX,oldY;
     Dir dir;
    private static final int SPEED=Integer.parseInt((String) PropertyMgr.get("tankSpeed"));
    public static int WIDTH=ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT=ResourceMgr.goodTankU.getHeight();
    private Random random=new Random();
    private boolean moving=true;
    private boolean living=true;
    Group group=Group.BAD;
    Rectangle rect=new Rectangle();
    GameModel gm;
    FireStrategy fs;//开火模式


    public Tank(int x, int y, Dir dir,Group group,GameModel gm){
        super();
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.group=group;
        this.gm=gm;

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
    public Rectangle getRect(){
        return this.rect;
    }
    public GameModel getGm(){
        return this.gm;
    }

    public GameModel getGameModel(){
        return gm;
    }
    public void paint(Graphics g){
        if(!living) {
            gm.remove(this);
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
        if(!living){
            return;
        }

        if(!moving) {
            return;
        }

        //坦克碰撞后复位坐标
        oldX=this.x;
        oldY=this.y;


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
        if(this.x<10) {
            x=10;
        }
        if (this.y<38) {
            y=38;
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

    //坦克复位
    public void goBack(){
        switch (dir){
            case LEFT :{
                x=oldX+7;
                break;
            }
            case UP:{
                y=oldY+7;
                break;
            }
            case RIGHT:{
                x=oldX-7;
                break;
            }
            case DOWN:{
                y=oldY-7;
                break;
            }
            default:
                break;
    }
    }

    //坦克停止
    public void stop(){
        this.moving=false;
    }
}
