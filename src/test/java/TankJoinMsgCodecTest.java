import client.Dir;
import client.Group;
import client.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 13:23
 */
public class TankJoinMsgCodecTest {
    public void testEncoder(){
        EmbeddedChannel ch=new EmbeddedChannel();

        UUID id=UUID.randomUUID();
        TankJoinMsg msg=new TankJoinMsg(5,10, Dir.DOWN,true, Group.GOOD,id);
        ch.pipeline()
                .addLast(new MsgEncoder());

        ch.writeOutbound(msg);

        ByteBuf buf=(ByteBuf) ch.readOutbound();
        MsgType msgType=MsgType.values()[buf.readInt()];
        assert (MsgType.TankJoin==msgType);

        int x=buf.readInt();
        int y=buf.readInt();
        int dirOrdinal=buf.readInt();
        Dir dir=Dir.values()[dirOrdinal];
        boolean moving=buf.readBoolean();
        int groupOrdinal=buf.readInt();
        Group group=Group.values()[groupOrdinal];
        UUID uuid=new UUID(buf.readLong(),buf.readLong());

//        assert(msg.x==5&&msg.y==10&&msg.dir==Dir.DOWN&& msg.moving&& msg.group==Group.GOOD&& msg.id==uuid);
        assert(msg.x==5);
        assert (10== y);
        assert (Dir.DOWN== dir);
        assert (true==moving);
        assert (Group.GOOD== group);
        assert (id== uuid);
    }
    public void testDecoder(){
        EmbeddedChannel ch=new EmbeddedChannel();

        UUID id=UUID.randomUUID();
        TankJoinMsg msg=new TankJoinMsg(5,10, Dir.DOWN,true, Group.GOOD,id);
        ch.pipeline()
                .addLast(new MsgDecoder());

        ByteBuf buf= Unpooled.buffer();
        buf.writeInt(MsgType.TankJoin.ordinal());
        byte[]bytes= msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        ch.writeInbound(buf.duplicate());

        TankJoinMsg msgR=(TankJoinMsg) ch.readInbound();

        assert (5== msgR.x);
        assert (10== msgR.y);
        assert (Dir.DOWN== msgR.dir);
        assert (true== msgR.moving);
        assert (Group.GOOD== msgR.group);
        assert (id== msgR.id);

    }
}
