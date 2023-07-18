package client;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-17
 * Time: 13:40
 */
public class TankFrame extends Frame{
    Tank myTank=new Tank(200,400,Dir.DOWN,Group.GOOD,this);
    List<Bullet>bullets=new ArrayList<>();//子弹
    List<Tank>tanks=new ArrayList<>();//自动生成的敌方坦克
    List<Explode>explodes=new ArrayList<>();//爆炸效果
    Bullet b=new Bullet(300,300,Dir.DOWN,Group.GOOD,this);
    static final int GAME_WIDTH=Integer.parseInt((String) PropertyMgr.get("gameWidth"));//使用配置文件来改变
    static final int GAME_HEIGHT=Integer.parseInt((String) PropertyMgr.get("gameHeight"));//使用配置文件来改变
    public TankFrame(){
        super("tank war");
        setSize(GAME_WIDTH,GAME_HEIGHT);//设置窗口大小800x600
        setResizable(false);//禁止调整窗口大小
        setVisible(true);//使窗口可见

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new MyKeyListener());
    }

    //利用双缓冲一次性画完所有，减少闪烁
    Image offScreenImage=null;
    @Override
    public void update(Graphics g){
        if(offScreenImage==null){
            offScreenImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);//定义一张和游戏屏幕一样大的图像
        }
        Graphics gOffScreen=offScreenImage.getGraphics();
        Color c=gOffScreen.getColor();
        gOffScreen.setColor(Color.black);
        gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage,0,0,null);//把整体的图像画到屏幕
    }
    @Override
    public void paint(Graphics g){
        Color c=g.getColor();
        g.setColor(Color.white);
        g.drawString("子弹的数量"+bullets.size(),10,50);//显示子弹的数量
        g.drawString("敌人的数量"+tanks.size(),10,70);//显示敌人的数量
        g.drawString("爆炸的数量"+tanks.size(),10,90);//显示爆炸的数量

        g.setColor(c);

        myTank.paint(g);//画出主坦克
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);//画出子弹
        }
        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).paint(g);//画出敌方坦克
        }
        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);//画出爆炸效果
        }

        //检测碰撞
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }

    }
    class MyKeyListener extends KeyAdapter{
        //创建四个变量代表上下左右键是否被按下，根据按下的键控制坦克的移动方向
        boolean bL=false;
        boolean bU=false;
        boolean bR=false;
        boolean bD=false;

        @Override
        public void keyPressed(KeyEvent e) {//按下键盘
            int key=e.getKeyCode();
            switch (key){
                case KeyEvent.VK_LEFT:{
                    bL=true;
                    break;
                }
                case KeyEvent.VK_UP:{
                    bU=true;
                    break;
                }
                case KeyEvent.VK_RIGHT:{
                    bR=true;
                    break;
                }
                case KeyEvent.VK_DOWN:{
                    bD=true;
                    break;
                }
                default:
                    break;
            }
            setMainTankDir();
        }

        @Override
        public void keyReleased(KeyEvent e) {//松开键盘
            int key=e.getKeyCode();
            switch (key){
                case KeyEvent.VK_LEFT:{
                    bL=false;
                    break;
                }
                case KeyEvent.VK_UP:{
                    bU=false;
                    break;
                }
                case KeyEvent.VK_RIGHT:{
                    bR=false;
                    break;
                }
                case KeyEvent.VK_DOWN:{
                    bD=false;
                    break;
                }
                case KeyEvent.VK_CONTROL:{
                    myTank.fire();
                    break;
                }
                default:
                    break;
            }
            setMainTankDir();
        }
        private void setMainTankDir(){
            if(!bL&&!bU&&!bR&&!bD)myTank.setMoving(false);//没有摁键的时候不动
            else {
                myTank.setMoving(true);//坦克开始移动

                if (bL) myTank.setDir(Dir.LEFT);//设置方向
                if (bU) myTank.setDir(Dir.UP);
                if (bR) myTank.setDir(Dir.RIGHT);
                if (bD) myTank.setDir(Dir.DOWN);
            }
        }
    }
}
