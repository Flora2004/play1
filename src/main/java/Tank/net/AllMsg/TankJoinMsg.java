package Tank.net.AllMsg;

import java.io.*;
import  java.util.UUID;

import Tank.Enum.Group;
import Tank.Enum.Dir;
import Tank.GameObject.Tank;
import Tank.TankFrame;
import Tank.net.Client;
import Tank.Enum.MsgType;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 10:31
 */
public class TankJoinMsg extends Msg {
    public int x,y;
    public Dir dir;
    public boolean moving;
    public Group group;
    public UUID id;
//    public String name;
    public TankJoinMsg(){
    }
    public TankJoinMsg(int x, int y,Dir dir,boolean moving,Group group,UUID id){
        super();
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.moving=moving;
        this.group=group;
        this.id=id;
//        this.name=name;
    }

    public TankJoinMsg(Tank t){
        this.x= t.getX();
        this.y=t.getY();
        this.dir=t.getDir();
        this.moving=t.isMoving();
        this.group=t.getGroup();
        this.id=t.getId();
//        this.name=t.getName();
    }
    @Override
    public byte[] toBytes(){
        ByteArrayOutputStream baos=null;//向字节数组中写
        DataOutputStream dos=null;//写数据
        byte[] bytes=null;
        try{
            baos=new ByteArrayOutputStream();
            dos=new DataOutputStream(baos);
//            dos.writeInt(TYPE.ordinal);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
//            dos.writeUTF(name);
            dos.flush();
            bytes= baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(baos!=null){
                    baos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                if(dos!=null){
                    dos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bytes;
    }
    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append(this.getClass().getName())
                .append("[")
                .append("uuid="+id+"|")
//                .append("name="+name+"|")
                .append("x="+x+"|")
                .append("y="+y+"|")
                .append("moving="+moving+"|")
                .append("dir="+dir+"|")
                .append("group="+group+"|")
                .append("]");
        return builder.toString();
    }
    @Override
    public void handle() {
        if(this.id.equals(TankFrame.INSTANCE.getGm().getMainTank().getId())||//判断是否是同一个坦克
                TankFrame.INSTANCE.getGm().findTankByUUID(this.id)!=null)return;//判断在客户端是否有服务器传来的坦克
        System.out.println(this);
        Tank t=new Tank(this,TankFrame.INSTANCE.getGm());
        TankFrame.INSTANCE.getGm().add(t);

        //send a new TankJoinMsg to the new joined tank
        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMainTank()));
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoin;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            //TODO:先读TYPE信息，根据TYPE信息处理不同的消息
            //略过消息类型
            //dis.readInt();

            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = Group.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(), dis.readLong());
//            this.name = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
