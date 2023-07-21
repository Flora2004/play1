import client.Dir;
import client.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.Enumeration;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 19:18
 */
public class TankStartMovingMsgCodecTest {
    void testEncoder(){
        EmbeddedChannel ch=new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        TankStartMovingMsg msg = new TankStartMovingMsg(id, 5, 10, Dir.LEFT);
        ch.pipeline()
                .addLast(new MsgEncoder());

        ch.writeOutbound(msg);

        ByteBuf buf = (ByteBuf)ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        assert (MsgType.TankStartMoving== msgType);

        int length = buf.readInt();
        assert (28==length);

        UUID uuid = new UUID(buf.readLong(), buf.readLong());
        int x = buf.readInt();
        int y = buf.readInt();
        int dirOrdinal = buf.readInt();
        Dir dir = Dir.values()[dirOrdinal];

        assert (5== x);
        assert (10== y);
        assert (Dir.LEFT== dir);
        assert (id== uuid);
    }

    void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();


        UUID id = UUID.randomUUID();
        TankStartMovingMsg msg = new TankStartMovingMsg(id, 5, 10, Dir.LEFT);
        ch.pipeline()
                .addLast(new MsgDecoder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankStartMoving.ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        ch.writeInbound(buf.duplicate());

        TankStartMovingMsg msgR = (TankStartMovingMsg)ch.readInbound();

        assert (5== msgR.getX());
        assert (10== msgR.getY());
        assert (Dir.LEFT== msgR.getDir());
        assert (id== msgR.getId());
    }
}
