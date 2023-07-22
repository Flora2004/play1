package client.net;

import client.Bullet;
import client.Tank;
import client.TankFrame;

import java.io.*;
import java.security.spec.ECField;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 22:16
 */
public class TankDieMsg extends Msg{
    UUID bulletId;//打死我的子弹
    UUID id;
    public TankDieMsg(UUID playerId,UUID id){
        this.bulletId=playerId;
        this.id=id;
    }
    public TankDieMsg(){}

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos=null;
        DataOutputStream dos=null;
        byte[] bytes=null;
        try {
            baos=new ByteArrayOutputStream();
            dos=new DataOutputStream(baos);
            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.flush();
            bytes=baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.bulletId=new UUID(dis.readLong(),dis.readLong());
            this.id=new UUID(dis.readLong(),dis.readLong());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void handle() {
        System.out.println("we got a tank die: "+id);
        System.out.println("and my tank is "+ TankFrame.INSTANCE.getGm().getMainTank().getId());
        Tank tt=TankFrame.INSTANCE.getGm().findTankByUUID(id);
        System.out.println("i found a tank with this id "+tt);

        Bullet b=TankFrame.INSTANCE.getGm().findBulletByUUID(bulletId);
        if(b!=null){
            b.die();//子弹打中坦克后子弹消失
        }

        if(this.id.equals(TankFrame.INSTANCE.getGm().getMainTank().getId())){
            TankFrame.INSTANCE.getGm().getMainTank().die();//主战坦克被敌方打死
        }else {
            Tank t=TankFrame.INSTANCE.getGm().findTankByUUID(id);
            if(t!=null){
                t.die();//敌方坦克被打死后消失
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName())
                .append("[")
                .append("bulletId=" + bulletId + "|")
                .append("id=" + id + " | ")
                .append("]");
        return builder.toString();
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
