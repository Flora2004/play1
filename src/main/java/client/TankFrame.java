package client;

import client.net.Client;
import client.net.TankDirChangeMsg;
import client.net.TankStartMovingMsg;
import client.net.TankStopMsg;

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
    public static final TankFrame INSTANCE=new TankFrame();
    GameModel gm=new GameModel();
    public static final int GAME_WIDTH=1080;
    public static final int GAME_HEIGHT=960;
//    static final int GAME_WIDTH=Integer.parseInt((String) PropertyMgr.get("gameWidth"));//使用配置文件来改变
//    static final int GAME_HEIGHT=Integer.parseInt((String) PropertyMgr.get("gameHeight"));//使用配置文件来改变
    private TankFrame(){
        setSize(GAME_WIDTH,GAME_HEIGHT);//设置窗口大小
        setResizable(false);//禁止调整窗口大小
        setVisible(true);//使窗口可见
        setTitle("tank war");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new MyKeyListener());
    }
    public GameModel getGm(){
        return gm;
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
        gm.paint(g);
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
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_UP:{
                    bU=true;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_RIGHT:{
                    bR=true;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_DOWN:{
                    bD=true;
                    setMainTankDir();
                    break;
                }
                default:
                    break;
            }
            new Thread(()->new Audio("audio/tank_move.wav").play()).start();
        }

        @Override
        public void keyReleased(KeyEvent e) {//松开键盘
            int key=e.getKeyCode();
            switch (key){
                case KeyEvent.VK_LEFT:{
                    bL=false;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_UP:{
                    bU=false;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_RIGHT:{
                    bR=false;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_DOWN:{
                    bD=false;
                    setMainTankDir();
                    break;
                }
                case KeyEvent.VK_CONTROL:{
                    gm.getMainTank().fire();

                    break;
                }
                case KeyEvent.VK_S:{
                    gm.save();//s键存盘
                    break;
                }
                case KeyEvent.VK_L:{
                    gm.load();
                    break;
                }
                default:
                    break;
            }
        }
        private void setMainTankDir(){
            Tank myTank=gm.getMainTank();
            Dir dir=myTank.getDir();

            if(!bL&&!bU&&!bR&&!bD) {
                myTank.setMoving(false);//没有摁键的时候不动
                Client.INSTANCE.send(new TankStopMsg(myTank));
            }
            else {
                //TODO:设置斜着走
                if (bL)
                    myTank.setDir(Dir.LEFT);//设置方向
                if (bU)
                    myTank.setDir(Dir.UP);
                if (bR)
                    myTank.setDir(Dir.RIGHT);
                if (bD)
                    myTank.setDir(Dir.DOWN);
                //发出坦克移动的消息
                if(!myTank.isMoving()) {
                    Client.INSTANCE.send(new TankStartMovingMsg(myTank));
                }
                myTank.setMoving(true);//坦克开始移动

                if(dir != myTank.getDir()) {
                    Client.INSTANCE.send(new TankDirChangeMsg(myTank));
                }
            }
        }
    }
}
