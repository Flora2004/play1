package Tank.GameObject;

import Tank.Enum.Dir;
import Tank.Enum.Group;
import Tank.net.AllMsg.*;
import Tank.net.Client;
import Tank.strategy.FireStrategy;
import Tank.*;
import Tank.Mgr.*;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 14:42
 */
public class Tank extends GameObject {
    private int oldX,oldY;
    private Dir dir;
    private static final int SPEED=Integer.parseInt((String) PropertyMgr.get("tankSpeed"));
    public static int WIDTH= ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT= ResourceMgr.goodTankU.getHeight();

    private UUID id= UUID.randomUUID();

    private Random random=new Random();
    private boolean moving=false;
    private boolean living=true;
    private Group group=Group.BAD;
    //坦克复活时间
    private int secondsRemaining;
    public Rectangle rect=new Rectangle();
    private GameModel gm;
    private FireStrategy fs;//开火模式
    public Tank(TankJoinMsg msg, GameModel gm) {
        this.x = msg.x;
        this.y = msg.y;
        this.dir = msg.dir;
        this.moving = msg.moving;
        this.group = msg.group;
        this.id = msg.id;
        this.gm=gm;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }
    public Tank(TankReliveMsg msg, GameModel gm) {
        this.x = msg.x;
        this.y = msg.y;
        this.dir = msg.dir;
        this.moving = msg.moving;
        this.group = msg.group;
        this.id = msg.id;
        this.gm=gm;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public Tank(TankExitMsg msg, GameModel gm) {
        this.x = msg.x;
        this.y = msg.y;
        this.dir = msg.dir;
        this.moving = msg.moving;
        this.group = msg.group;
        this.id = msg.id;
        this.gm=gm;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public Tank(int x, int y, Dir dir, Group group, GameModel gm){
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

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    public GameModel getGameModel() {
        return gm;
    }
    public UUID getId(){
        return id;
    }

    public void paint(Graphics g){
        if(!living) {
            gm.remove(this);
            moving = false;

            Color cc = g.getColor();
            g.setColor(Color.WHITE);
            g.drawRect(x, y, WIDTH, HEIGHT);
            g.setFont(new Font("仿宋",Font.BOLD,20));
            String str="还有 "+secondsRemaining+" 秒复活";
            g.drawString(str, TankFrame.GAME_WIDTH/2, TankFrame.GAME_HEIGHT/2);
            g.setColor(cc);
        }
        //name on head
        Color c=g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString(),this.x,this.y-10);
        g.setColor(c);
        switch (dir){
            case LEFT:
                g.drawImage(this.group==Group.GOOD? ResourceMgr.goodTankL: ResourceMgr.badTankL,x,y,null);
                break;
            case UP:
                g.drawImage(this.group==Group.GOOD? ResourceMgr.goodTankU: ResourceMgr.badTankU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(this.group==Group.GOOD? ResourceMgr.goodTankR: ResourceMgr.badTankR,x,y,null);
                break;
            case DOWN:
                g.drawImage(this.group==Group.GOOD? ResourceMgr.goodTankD: ResourceMgr.badTankD,x,y,null);
                break;
            default:
                break;
        }

        move();
    }

    private void move(){
        //记录移动之前的位置
        oldX=x;
        oldY=y;

        if(!living){
            return;
        }

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
        //人机坦克自动走位+射击
//        if(this.group==Group.BAD&&random.nextInt(100)>95) {
//            this.fire();
//        }
//
//        if(this.group==Group.BAD&&random.nextInt(100)>90) {
//            randomDir();
//        }

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
        if(this.x> TankFrame.GAME_WIDTH-Tank.WIDTH-2) {
            x= TankFrame.GAME_WIDTH-Tank.WIDTH-2;
        }
        if(this.y> TankFrame.GAME_HEIGHT-Tank.HEIGHT-2) {
            y= TankFrame.GAME_HEIGHT-Tank.HEIGHT-2;
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
        // 启动一个新线程来处理延迟和更新
        secondsRemaining=5;
        new Thread(() -> {
            try {
                while (secondsRemaining > 0) {
                    System.out.println("还有 " + secondsRemaining + " 秒坦克将复活。");
                    Thread.sleep(1000); // 等待 1 秒
                    secondsRemaining--;
                }

                // 5 秒后，将 living 属性设置为 true，表示坦克已复活
                this.living = true;
                System.out.println("坦克已复活！");
                if(this.living){
                    Client.INSTANCE.send(new TankReliveMsg(this));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //坦克接触到墙就停下
    public void back(){
        x=oldX;
        y=oldY;
    }
}
