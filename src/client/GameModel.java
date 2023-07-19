package client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 8:14
 */
public class GameModel {
    Tank myTank=new Tank(200,400,Dir.DOWN,Group.GOOD,this);
    java.util.List<Bullet> bullets=new ArrayList<>();//子弹
    java.util.List<Tank> tanks=new ArrayList<>();//自动生成的敌方坦克
    List<Explode> explodes=new ArrayList<>();//爆炸效果
    public GameModel(){
        //初始化敌方坦克
        int initTankCount= Integer.parseInt((String) PropertyMgr.get("initTankCount"));//使用配置文件来改变
        for (int i = 0; i < initTankCount; i++) {
            tanks.add(new Tank(50+i*50,200,Dir.DOWN,Group.BAD,this));
        }
    }

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

    public Tank getMainTank(){
        return myTank;
    }

}
