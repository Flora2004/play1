package client;

import client.cor.BulletTankCollider;
import client.cor.Collider;
import client.cor.ColliderChain;
import client.cor.TankTankCollider;

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
    ColliderChain chain=new ColliderChain();

    private List<GameObject> objects=new ArrayList<>();//所有的物体

    public GameModel(){
        //初始化敌方坦克
        int initTankCount= Integer.parseInt((String) PropertyMgr.get("initTankCount"));//使用配置文件来改变
        for (int i = 0; i < initTankCount; i++) {
            add(new Tank(Integer.parseInt((String) PropertyMgr.get("badTankX"))
                    +i*Integer.parseInt((String) PropertyMgr.get("badTankSpace")),
                    Integer.parseInt((String) PropertyMgr.get("badTankY")),Dir.DOWN,Group.BAD,this));
        }

        //初始化墙
        add(new Wall(150,150,200,50));
        add(new Wall(550,150,200,50));
        add(new Wall(300,300,50,200));
        add(new Wall(550,300,50,200));
    }


    public void add(GameObject go){
        this.objects.add(go);
    }
    public void remove(GameObject go){
        this.objects.remove(go);
    }

    public void paint(Graphics g){
        Color c=g.getColor();
        g.setColor(Color.white);
//        g.drawString("子弹的数量"+bullets.size(),10,50);//显示子弹的数量
//        g.drawString("敌人的数量"+tanks.size(),10,70);//显示敌人的数量
//        g.drawString("爆炸的数量"+tanks.size(),10,90);//显示爆炸的数量

        g.setColor(c);

        myTank.paint(g);//画出主坦克
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);//画出物体
        }

        //互相碰撞
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i+1; j < objects.size(); j++) {
                GameObject o1=objects.get(i);
                GameObject o2=objects.get(j);
                chain.collide(o1,o2);
            }
        }


//        for (int i = 0; i < bullets.size(); i++) {
//            bullets.get(i).paint(g);//画出子弹
//        }
//        for (int i = 0; i < tanks.size(); i++) {
//            tanks.get(i).paint(g);//画出敌方坦克
//        }
//        for (int i = 0; i < explodes.size(); i++) {
//            explodes.get(i).paint(g);//画出爆炸效果
//        }

        //检测碰撞
//        for (int i = 0; i < bullets.size(); i++) {
//            for (int j = 0; j < tanks.size(); j++) {
//                bullets.get(i).collideWith(tanks.get(j));
//            }
//        }
    }

    public Tank getMainTank(){
        return myTank;
    }

}
