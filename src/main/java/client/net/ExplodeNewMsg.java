package client.net;

import client.*;

import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 22:20
 */
public class ExplodeNewMsg extends Msg{
    UUID playerID;//发生爆炸的坦克的id
    UUID id;
    int x,y;
    public ExplodeNewMsg(Explode explode){
        this.playerID = explode.getPlayerId();
        this.id = explode.getId();
        this.x = explode.getX();
        this.y = explode.getY();
    }

    public ExplodeNewMsg(){}
    @Override
    public byte[] toBytes(){
        ByteArrayOutputStream baos=null;
        DataOutputStream dos=null;
        byte[] bytes=null;
        try {
            baos=new ByteArrayOutputStream();
            dos=new DataOutputStream(baos);

            //输出发生爆炸的坦克的uuid
            dos.writeLong(this.playerID.getMostSignificantBits());
            dos.writeLong(this.playerID.getLeastSignificantBits());

            //输出爆炸的uuid
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.flush();
            bytes = baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
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
            this.playerID=new UUID(dis.readLong(),dis.readLong());
            this.id=new UUID(dis.readLong(),dis.readLong());
            this.x=dis.readInt();
            this.y=dis.readInt();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                dis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle() {
        //如果传来的爆炸信息是自己的则不处理
        if(this.playerID.equals(TankFrame.INSTANCE.getGm().getMainTank().getId()))
            return;
        //传来的是别的坦克的爆炸则添加进去
        Explode explode=new Explode(this.playerID,x,y,TankFrame.INSTANCE.getGm());
        explode.setId(this.id);
        TankFrame.INSTANCE.getGm().add(explode);
        }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName())
                .append("[")
                .append("playerid=" + playerID + " | ")
                .append("uuid=" + id + " | ")
                .append("x=" + x + " | ")
                .append("y=" + y + " | ")
                .append("]");
        return builder.toString();
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}
