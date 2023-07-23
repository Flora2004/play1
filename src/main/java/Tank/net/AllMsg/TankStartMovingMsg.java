package Tank.net.AllMsg;

import Tank.Enum.Dir;
import Tank.GameObject.Tank;
import Tank.TankFrame;
import Tank.Enum.MsgType;

import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 18:37
 */
public class TankStartMovingMsg extends Msg {
    UUID id;
    int x,y;
    Dir dir;
    public TankStartMovingMsg(){
    }
    public TankStartMovingMsg(UUID id, int x, int y, Dir dir){
        this.id=id;
        this.x=x;
        this.y=y;
        this.dir=dir;
    }
    public TankStartMovingMsg(Tank t){
        this.id=t.getId();
        this.x= t.getX();
        this.y=t.getY();
        this.dir=t.getDir();
    }

    public UUID getId() {
        return id;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Dir getDir() {
        return dir;
    }


    @Override
    public MsgType getMsgType() {
        return MsgType.TankStartMoving;
    }

    @Override
    public void handle() {
        //如果传来的坦克信息是自己则不处理
        if(this.id.equals(TankFrame.INSTANCE.getGm().getMainTank().getId()))return;

        Tank t=TankFrame.INSTANCE.getGm().findTankByUUID(this.id);

        if(t!=null){
            t.setMoving(true);
            t.setX(this.x);
            t.setY(this.y);
            t.setDir(dir);
        }
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
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
    public void setId(UUID id) {
        this.id = id;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }



    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos=null;//向字节数组中写
        DataOutputStream dos=null;//写数据
        byte[] bytes=null;
        try{
            baos=new ByteArrayOutputStream();
            dos=new DataOutputStream(baos);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
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
}
