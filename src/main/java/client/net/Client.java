package client.net;

import client.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-20
 * Time: 10:37
 */
public class Client {
    public static final Client INSTANCE = new Client();
    private Channel channel=null;

    private Client(){    }
    public void connect(){
        //创建group线程池,创建线程处理链接和读取
        EventLoopGroup group=new NioEventLoopGroup(1);

        Bootstrap b=new Bootstrap();

        try {//链接服务器
            ChannelFuture f=b.group(group)
                    .channel(NioSocketChannel.class)//链接到服务器的NIO非阻塞版
                    .handler(new ClientChannelInitializer())//处理出现的特殊事件
                    //异步方法，无论有没有连接数都会执行下一行代码
                    .connect("localhost",8888)
                    ;

            f.addListener(new ChannelFutureListener() {//判断客户端是否链接到服务器
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(!future.isSuccess()){
                        System.out.println("not connected!");
                    }else {
                        System.out.println("connected!");
                        //连接成功后初始化channel
                        channel=future.channel();
                    }
                }
            });

            f.sync();

            System.out.println("...");

            f.channel().closeFuture().sync();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
//    public void closeConnect(){
//        //this.send("_bye_");
//        //channel.close();
//    }

    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }
}
class ClientChannelInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch)throws Exception{
        ch.pipeline()
                .addLast(new MsgEncoder())//对传出的信息进行处理
                .addLast(new MsgDecoder())//对传回的信息进行处理
                .addLast(new ClientHandler());//对服务器传回的消息进行处理
    }
}
class  ClientHandler extends SimpleChannelInboundHandler<Msg>{//处理单一的数据类型

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        msg.handle();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //拿到主战坦克
        ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMainTank()));
    }
}
