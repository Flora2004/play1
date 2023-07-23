package Tank.net.AllMsg;

import Tank.Enum.MsgType;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-21
 * Time: 16:32
 */
public abstract class Msg {
    public abstract void handle();//收到消息后处理
    public abstract byte[] toBytes();//转化成字节数组
    public abstract void parse(byte[] bytes);
    public abstract MsgType getMsgType();
}
