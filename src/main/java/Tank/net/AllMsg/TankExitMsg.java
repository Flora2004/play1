package Tank.net.AllMsg;

import Tank.Enum.Dir;
import Tank.Enum.Group;
import Tank.Enum.MsgType;
import Tank.GameObject.Tank;
import Tank.TankFrame;
import Tank.net.ServerFrame;

import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-23
 * Time: 20:23
 */

public class TankExitMsg extends Msg {
    public int x,y;
    public Dir dir;
    public boolean moving;
    public Group group;
    public UUID id;
    //    public String name;
    public TankExitMsg(){
    }
    public TankExitMsg(int x, int y,Dir dir,boolean moving,Group group,UUID id){
        super();
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.moving=moving;
        this.group=group;
        this.id=id;
//        this.name=name;
    }

    public TankExitMsg(Tank t){
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
        //是自己的坦克不处理
        if(this.id.equals(TankFrame.INSTANCE.getGm().getMainTank().getId())||
                //若没有要删除的坦克则不处理
                TankFrame.INSTANCE.getGm().findTankByUUID(this.id)==null) {
            return;
        }
        Tank t=new Tank(this, TankFrame.INSTANCE.getGm());
        TankFrame.INSTANCE.getGm().remove(t);
        ServerFrame.INSTANCE.updateServerMsg("delete");
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankExit;
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

