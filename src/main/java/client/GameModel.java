package client;

import client.cor.BulletTankCollider;
import client.cor.Collider;
import client.cor.ColliderChain;
import client.cor.TankTankCollider;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-19
 * Time: 8:14
 */
public class GameModel implements Serializable{
    Random r=new Random();
    Tank myTank=new Tank(r.nextInt(TankFrame.GAME_WIDTH),r.nextInt(TankFrame.GAME_HEIGHT),Dir.values()[r.nextInt(4)],Group.GOOD,this);
    ColliderChain chain=new ColliderChain();//碰撞的责任链

    private List<GameObject> objects=new ArrayList<>();//所有的物体

    public GameModel(){
        //初始化敌方坦克
//        int initTankCount= Integer.parseInt((String) PropertyMgr.get("initTankCount"));//使用配置文件来改变
//        for (int i = 0; i < initTankCount; i++) {
//            add(new Tank(Integer.parseInt((String) PropertyMgr.get("badTankX"))
//                    +i*Integer.parseInt((String) PropertyMgr.get("badTankSpace")),
//                    Integer.parseInt((String) PropertyMgr.get("badTankY")),Dir.DOWN,Group.BAD,this));
//        }

//        //初始化墙
//        add(new Wall(150,150,200,50));
//        add(new Wall(550,150,200,50));
//        add(new Wall(300,300,50,200));
//        add(new Wall(550,300,50,200));
    }


    public void add(GameObject go){
        this.objects.add(go);
    }
    public void remove(GameObject go){
        this.objects.remove(go);
    }
    public Tank findTankByUUID(UUID id) {
        for (GameObject go : objects) {
            if (go instanceof Tank && ((Tank) go).getId().equals(id)) {
                return (Tank) go;
            }
        }
        return null;
    }
    public Bullet findBulletByUUID(UUID id) {
        for (GameObject go : objects) {
            if (go instanceof Bullet && ((Bullet) go).getId().equals(id)) {
                return (Bullet) go;
            }
        }
        return null;
    }

    public void paint(Graphics g){
//        Color c=g.getColor();
//        g.setColor(Color.white);
//        g.drawString("子弹的数量"+bullets.size(),10,50);//显示子弹的数量
//        g.drawString("敌人的数量"+tanks.size(),10,70);//显示敌人的数量
//        g.drawString("爆炸的数量"+tanks.size(),10,90);//显示爆炸的数量

//        g.setColor(c);
            myTank.paint(g);//画出主坦克
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).paint(g);//画出物体
            }

            //互相碰撞
            for (int i = 0; i < objects.size(); i++) {
                for (int j = i + 1; j < objects.size(); j++) {
                    GameObject o1 = objects.get(i);
                    GameObject o2 = objects.get(j);
                    chain.collide(o1, o2);
                }
            }
    }

    public Tank getMainTank(){
        return myTank;
    }

    //本地存储
    public void save(){
        File f =new File("D://Test//tank.data");
        ObjectOutputStream oos=null;
        try{
        oos=new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(myTank);
        oos.writeObject(objects);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(oos!=null){
                try{
                    oos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //加载本地数据
    public void load() {
        File f =new File("D://Test//tank.data");
        ObjectInputStream ois=null;
        try{
            ois=new ObjectInputStream(new FileInputStream(f));
            myTank=(Tank) ois.readObject();
            objects=(List)ois.readObject() ;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(ois!=null){
                try{
                    ois.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
