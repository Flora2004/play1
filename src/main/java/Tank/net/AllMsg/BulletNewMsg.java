package Tank.net.AllMsg;

import Tank.GameObject.Bullet;
import Tank.Enum.Dir;
import Tank.Enum.Group;
import Tank.TankFrame;
import Tank.Enum.MsgType;
import Tank.net.ServerFrame;

import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 22:20
 */
public class BulletNewMsg extends Msg {
    UUID playerID;//子弹的id
    UUID id;
    int x,y;
    Dir dir;
    Group group;
    public BulletNewMsg(Bullet bullet){
        this.playerID = bullet.getPlayerId();
        this.id = bullet.getId();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        this.group = bullet.getGroup();
    }

    public BulletNewMsg(){}
    @Override
    public byte[] toBytes(){
        ByteArrayOutputStream baos=null;
        DataOutputStream dos=null;
        byte[] bytes=null;
        try {
            if(dir==null){
                return bytes;
            }
            baos=new ByteArrayOutputStream();
            dos=new DataOutputStream(baos);

            //输出打出子弹的坦克的uuid
            dos.writeLong(this.playerID.getMostSignificantBits());
            dos.writeLong(this.playerID.getLeastSignificantBits());

            //输出子弹的uuid
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
            dos.flush();
            bytes = baos.toByteArray();
        }catch (Exception e){
            System.out.println("出错");
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
            this.dir=Dir.values()[dis.readInt()];
            this.group=Group.values()[dis.readInt()];
        } catch (EOFException eof){
            ServerFrame.INSTANCE.updateServerMsg("bulletNew parse 出现问题");

        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            try{
                dis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle() {
        //如果传来的坦克信息是自己的则不处理
        if(this.playerID.equals(TankFrame.INSTANCE.getGm().getMainTank().getId()))
            return;
        //传来的是别的坦克的子弹则添加进去
        Bullet bullet=new Bullet(this.playerID,x,y,dir,group,TankFrame.INSTANCE.getGm());
        bullet.setId(this.id);
        TankFrame.INSTANCE.getGm().add(bullet);
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
                .append("dir=" + dir + " | ")
                .append("group=" + group + " | ")
                .append("]");
        return builder.toString();
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}
