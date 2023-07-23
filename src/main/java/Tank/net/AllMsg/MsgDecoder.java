package Tank.net.AllMsg;

import Tank.Enum.*;
import Tank.net.AllMsg.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 10:36
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<8)return;//消息头＋长度必须收到才能处理消息

        in.markReaderIndex();//确认读取的位置

        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();

        //添加异常处理：确保length是一个合法的正整数
        if(length < 0){
            throw new DecoderException("Invalid message length");
        }


        byte[] bytes=new byte[length];
        in.readBytes(bytes);

        Msg msg = null;

        //reflection
        //Class.forName(msgType.toString+"Msg").constructor().newInstance;
        switch (msgType){
            case TankJoin:{
                msg = new TankJoinMsg();
                break;
            }
            case TankStartMoving: {
                msg = new TankStartMovingMsg();
                break;
            }
            case TankStop:{
                msg = new TankStopMsg();
                break;
            }
            case TankDirChange:{
                msg = new TankDirChangeMsg();
                break;
            }
            case TankDie:{
                msg = new TankDieMsg();
                break;
            }
            case BulletNew:{
                msg = new BulletNewMsg();
                break;
            }
            case ExplodeNew:{
                msg = new ExplodeNewMsg();
                break;
            }
            case TankRelive:{
                msg = new TankReliveMsg();
                break;
            }
            //添加其他case处理其他消息类型
            default:
                break;
        }
        msg.parse(bytes);
        out.add(msg);
    }
}
